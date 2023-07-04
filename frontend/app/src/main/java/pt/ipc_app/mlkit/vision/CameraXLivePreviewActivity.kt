package pt.ipc_app.mlkit.vision

/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.R
import pt.ipc_app.domain.exercise.Exercise
import pt.ipc_app.domain.exercise.ExerciseTotalInfo
import pt.ipc_app.mlkit.CameraXViewModel
import pt.ipc_app.mlkit.GraphicOverlay
import pt.ipc_app.mlkit.VisionImageProcessor
import pt.ipc_app.mlkit.posedetector.PoseDetectorProcessor
import pt.ipc_app.mlkit.preference.PreferenceUtils
import pt.ipc_app.mlkit.preference.SettingsActivity
import pt.ipc_app.ui.screens.exercises.ExercisesViewModel
import pt.ipc_app.utils.viewModelInit
import java.io.File
import java.util.*

/** Live preview demo app for ML Kit APIs using CameraX. */
@KeepName
@RequiresApi(VERSION_CODES.LOLLIPOP)
class CameraXLivePreviewActivity :
    AppCompatActivity(), OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private var previewView: PreviewView? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var previewUseCase: Preview? = null
    private var analysisUseCase: ImageAnalysis? = null
    private var imageProcessor: VisionImageProcessor? = null
    private var needUpdateGraphicOverlayImageSourceInfo = false
    private var selectedModel = POSE_DETECTION
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var cameraSelector: CameraSelector? = null

    private var isRecording = false
    private lateinit var outputDirectory: File
    private lateinit var camera: Camera
    private lateinit var videoCapture: VideoCapture

    private val viewModel by viewModels<ExercisesViewModel> {
        viewModelInit {
            val app = (application as DependenciesContainer)
            ExercisesViewModel(app.services.exercisesService, app.sessionManager)
        }
    }

    @SuppressLint("MissingPermission", "RestrictedApi", "UnsafeExperimentalUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        if (savedInstanceState != null)
            selectedModel = savedInstanceState.getString(STATE_SELECTED_MODEL, POSE_DETECTION)

        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        setContentView(R.layout.activity_vision_camerax_live_preview)
        previewView = findViewById(R.id.preview_view)
        if (previewView == null)
            Log.d(TAG, "previewView is null")

        graphicOverlay = findViewById(R.id.graphic_overlay)
        if (graphicOverlay == null)
            Log.d(TAG, "graphicOverlay is null")

        val options: MutableList<String> = ArrayList()
        options.add(POSE_DETECTION)

        val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
        facingSwitch.setOnCheckedChangeListener(this)
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(CameraXViewModel::class.java)
            .processCameraProvider
            .observe(
                this,
                Observer { provider: ProcessCameraProvider? ->
                    cameraProvider = provider
                    bindAllCameraUseCases()
                }
            )

        val settingsButton = findViewById<ImageView>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent = Intent(applicationContext, SettingsActivity::class.java)
            intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.CAMERAX_LIVE_PREVIEW)
            startActivity(intent)
        }

        //verifies all permissions before acessing camera
        if (!allRuntimePermissionsGranted())
            getRuntimePermissions()

        //Creates a file directory for the video to be saved locally
        //creates a video capture and the record button action
        outputDirectory = getOutputDirectory()
        createVideoCapture()

        setupRecordingButton {
            if (exercise is ExerciseTotalInfo) {
                val exe = exercise as ExerciseTotalInfo
                viewModel.submitExerciseVideo(it, exe.planId, exe.dailyListId, exe.exercise.id, 1) // TODO(what is the set)
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { file ->
            File(file, "Videos").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }
    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putString(STATE_SELECTED_MODEL, selectedModel)
    }

    @Synchronized
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedModel = parent?.getItemAtPosition(pos).toString()
        Log.d(TAG, "Selected model: $selectedModel")
        bindAnalysisUseCase()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing.
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (cameraProvider == null)
            return

        val newLensFacing =
            if (lensFacing == CameraSelector.LENS_FACING_FRONT)
                CameraSelector.LENS_FACING_BACK
            else
                CameraSelector.LENS_FACING_FRONT

        val newCameraSelector = CameraSelector.Builder().requireLensFacing(newLensFacing).build()
        try {
            if (cameraProvider!!.hasCamera(newCameraSelector)) {
                Log.d(TAG, "Set facing to " + newLensFacing)
                lensFacing = newLensFacing
                cameraSelector = newCameraSelector
                bindAllCameraUseCases()
                return
            }
        } catch (e: CameraInfoUnavailableException) {
            // Falls through
        }
        Toast.makeText(
            applicationContext,
            "This device does not have lens with facing: $newLensFacing",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    public override fun onResume() {
        super.onResume()
        bindAllCameraUseCases()
    }

    override fun onPause() {
        super.onPause()
        imageProcessor?.run { this.stop() }
    }

    public override fun onDestroy() {
        super.onDestroy()
        imageProcessor?.run { this.stop() }
    }

    private fun bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider!!.unbindAll()
            bindPreviewUseCase()
            bindAnalysisUseCase()
            bindVideoCaptureUseCase()
        }
    }

    private fun bindPreviewUseCase() {
        if (!PreferenceUtils.isCameraLiveViewportEnabled(this))
            return

        if (cameraProvider == null)
            return

        if (previewUseCase != null)
            cameraProvider!!.unbind(previewUseCase)

        val builder = Preview.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        if (targetResolution != null)
            builder.setTargetResolution(targetResolution)

        previewUseCase = builder.build()
        previewUseCase!!.setSurfaceProvider(previewView!!.surfaceProvider)
        cameraProvider!!.bindToLifecycle(this, cameraSelector!!, previewUseCase)
    }

    private fun bindAnalysisUseCase() {
        if (cameraProvider == null)
            return

        if (analysisUseCase != null)
            cameraProvider!!.unbind(analysisUseCase)

        if (imageProcessor != null)
            imageProcessor!!.stop()

        imageProcessor =
            try {
                when (selectedModel) {
                    POSE_DETECTION -> {
                        val poseDetectorOptions = PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
                        PoseDetectorProcessor(
                            context = this,
                            options = poseDetectorOptions as PoseDetectorOptions,
                            exercise = exercise
                        )
                    }

                    else -> throw IllegalStateException("Invalid model name")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Can not create image processor: $selectedModel", e)
                Toast.makeText(
                    applicationContext,
                    "Can not create image processor: " + e.localizedMessage,
                    Toast.LENGTH_LONG
                )
                    .show()
                return
            }

        val builder = ImageAnalysis.Builder()
        val targetResolution = PreferenceUtils.getCameraXTargetResolution(this, lensFacing)
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution)
        }
        analysisUseCase = builder.build()

        needUpdateGraphicOverlayImageSourceInfo = true

        analysisUseCase?.setAnalyzer(
            // imageProcessor.processImageProxy will use another thread to run the detection underneath,
            // and because of that we can just run the analyzer itself on main thread.
            ContextCompat.getMainExecutor(this),
            ImageAnalysis.Analyzer { imageProxy: ImageProxy ->
                if (needUpdateGraphicOverlayImageSourceInfo) {
                    val isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    if (rotationDegrees == 0 || rotationDegrees == 180) {
                        graphicOverlay!!.setImageSourceInfo(imageProxy.width, imageProxy.height, isImageFlipped)
                    } else {
                        graphicOverlay!!.setImageSourceInfo(imageProxy.height, imageProxy.width, isImageFlipped)
                    }
                    needUpdateGraphicOverlayImageSourceInfo = false
                }
                try {
                    imageProcessor!!.processImageProxy(imageProxy, graphicOverlay)
                } catch (e: MlKitException) {
                    Log.e(TAG, "Failed to process image. Error: " + e.localizedMessage)
                    Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        )
        cameraProvider!!.bindToLifecycle(this, cameraSelector!!, analysisUseCase)
    }

    //-------------------------------- Recording video ------------------------------------------------------------
    private fun bindVideoCaptureUseCase() {
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        val cameraProvider = cameraProvider ?: return

        try {
            // Unbind any previously bound VideoCapture use case
            cameraProvider.unbind(videoCapture)

            // Bind the VideoCapture use case to the camera
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, videoCapture)
        } catch (e: Exception) {
            Log.e(TAG, "Error binding VideoCapture", e)
            Toast.makeText(applicationContext, "Error binding VideoCapture", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission", "RestrictedApi", "UnsafeExperimentalUsageError")
    private fun startRecording(onSubmission: (File) -> Unit) {
        val videoName =
            if (exercise is ExerciseTotalInfo)
                (exercise as ExerciseTotalInfo).exercise.id
            else UUID.randomUUID()

        val file = File(outputDirectory, "$videoName.mp4")
        val outputFileOptions = VideoCapture.OutputFileOptions.Builder(file).build()

        videoCapture.startRecording(
            outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : VideoCapture.OnVideoSavedCallback {
                override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
                    Log.d(TAG, "Video saved: ${file.absolutePath}")
                    onSubmission(file)
                }

                override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                    Log.e(TAG, "Video recording error: $message", cause)
                }
            }
        )
    }

    @SuppressLint("MissingPermission", "RestrictedApi", "UnsafeExperimentalUsageError")
    private fun stopRecording() {
        videoCapture.stopRecording()
    }

    @SuppressLint("MissingPermission", "RestrictedApi", "UnsafeExperimentalUsageError")
    private fun createVideoCapture() {
        videoCapture = VideoCapture.Builder().apply {
            setTargetAspectRatio(AspectRatio.RATIO_16_9)
            //setVideoFrameRate(30)
            //setTargetRotation(previewView!!.display.rotation)
            lensFacing
        }.build()

    }

    private fun setupRecordingButton(onSubmission: (File) -> Unit) {
        val recordButton: Button = findViewById(R.id.camera_button)
        recordButton.setOnClickListener {
            if (isRecording) {
                stopRecording()
                recordButton.text = "R"//"Start Recording"
            } else {
                startRecording(onSubmission)
                recordButton.text = "NR"//Stop Recording
            }

            isRecording = !isRecording
        }
    }


    //----------------------------------- Checking Permissions ----------------------------------
    private fun allRuntimePermissionsGranted(): Boolean {
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val permissionsToRequest = ArrayList<String>()
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    permissionsToRequest.add(permission)
                }
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }


    //--------------------------- for different exercices -------------------------------

    @Suppress("deprecation")
    private val exercise: Exercise by lazy {
        val exe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(EXERCISE, Exercise::class.java)
        else
            intent.getParcelableExtra(EXERCISE)
        checkNotNull(exe)
    }

    companion object {
        private const val TAG = "CameraXLivePreview"

        private const val POSE_DETECTION = "Pose Detection"

        private const val STATE_SELECTED_MODEL = "selected_model"

        private const val PERMISSION_REQUESTS = 1

        private val REQUIRED_RUNTIME_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            )

        const val EXERCISE = "EXERCISE_TO_DO"

        fun navigate(context: Context, exercise: Exercise) {
            with(context) {
                val intent = Intent(this, CameraXLivePreviewActivity::class.java)
                intent.putExtra(EXERCISE, exercise)
                startActivity(intent)
            }
        }
    }
}