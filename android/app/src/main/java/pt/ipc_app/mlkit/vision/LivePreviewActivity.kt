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

package pt.ipc_app.mlkit.vision

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.annotation.KeepName
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import pt.ipc_app.R
import pt.ipc_app.domain.Exercise
import pt.ipc_app.mlkit.CameraSource
import pt.ipc_app.mlkit.CameraSourcePreview
import pt.ipc_app.mlkit.GraphicOverlay
import pt.ipc_app.mlkit.posedetector.PoseDetectorProcessor
import pt.ipc_app.mlkit.preference.PreferenceUtils
import pt.ipc_app.mlkit.preference.SettingsActivity
import java.io.File
import java.io.IOException
import java.util.*

/** Live preview demo for ML Kit APIs. */
@KeepName
class LivePreviewActivity :
  AppCompatActivity(), OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

  private var cameraSource: CameraSource? = null
  private var preview: CameraSourcePreview? = null
  private var graphicOverlay: GraphicOverlay? = null
  private var selectedModel = POSE_DETECTION

  private lateinit var videoCapture: VideoCapture
  private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

  private lateinit var btnCamera: Button
  private var recording = false

  @SuppressLint("RestrictedApi")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate")
    setContentView(R.layout.activity_vision_live_preview)

    preview = findViewById(R.id.preview_view)
    if (preview == null)
      Log.d(TAG, "Preview is null")

    graphicOverlay = findViewById(R.id.graphic_overlay)
    if (graphicOverlay == null)
      Log.d(TAG, "graphicOverlay is null")


    btnCamera = findViewById(R.id.camera_button)

    btnCamera.setOnClickListener {
      if (!recording) {
        recording = true

        val file = File(
          externalMediaDirs.first(),
          "${UUID.randomUUID()}.mp4"
        )

        val outputFileOptions = VideoCapture.OutputFileOptions.Builder(file).build()

        if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
          ) != PackageManager.PERMISSION_GRANTED
        ) { }

        videoCapture.startRecording(
          outputFileOptions,
          ContextCompat.getMainExecutor(this),

          object : VideoCapture.OnVideoSavedCallback {
            override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
              Log.d("Check:", "On Video Saved")
            }

            override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
              Log.d("Check:", "On Video Error $message")
            }

          }
        )
      } else {
        recording = false
        videoCapture.stopRecording()
        finish()
      }
    }

    val options: MutableList<String> = ArrayList()
    options.add(POSE_DETECTION)

    val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
    facingSwitch.setOnCheckedChangeListener(this)

    val settingsButton = findViewById<ImageView>(R.id.settings_button)
    settingsButton.setOnClickListener {
      val intent = Intent(applicationContext, SettingsActivity::class.java)
      intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, SettingsActivity.LaunchSource.LIVE_PREVIEW)
      startActivity(intent)
    }

    //verifies all permissions before acessing camera
    if (!allRuntimePermissionsGranted()) {
      getRuntimePermissions()
    }

    createCameraSource(selectedModel)
  }

  @Synchronized
  override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent?.getItemAtPosition(pos).toString()
    Log.d(TAG, "Selected model: $selectedModel")
    preview?.stop()
    createCameraSource(selectedModel)
    startCameraSource()
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    // Do nothing.
  }

  override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
    Log.d(TAG, "Set facing")
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
      } else {
        cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
      }
    }
    preview?.stop()
    startCameraSource()
  }

  private fun createCameraSource(model: String) {
    // If there's no existing cameraSource, create one.
    if (cameraSource == null) {
      cameraSource = CameraSource(this, graphicOverlay)
    }
    try {
      when (model) {

        POSE_DETECTION -> {
          val poseDetectorOptions = PreferenceUtils.getPoseDetectorOptionsForLivePreview(this)
          Log.i(TAG, "Using Pose Detector with options $poseDetectorOptions")
          val shouldShowInFrameLikelihood =
            PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this)
          val visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this)
          val rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this)
          val runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this)
          cameraSource!!.setMachineLearningFrameProcessor(
            PoseDetectorProcessor(
              this,
              poseDetectorOptions as PoseDetectorOptions,
              shouldShowInFrameLikelihood,
              exercise
              //visualizeZ,
              //rescaleZ,
              //runClassification,
              /* isStreamMode = */ //true
            )
          )
        }
        else -> Log.e(TAG, "Unknown model: $model")
      }
    } catch (e: Exception) {
      Log.e(TAG, "Can not create image processor: $model", e)
      Toast.makeText(
          applicationContext,
          "Can not create image processor: " + e.message,
          Toast.LENGTH_LONG
        )
        .show()
    }
  }

  /**
   * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  private fun startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null")
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null")
        }
        preview!!.start(cameraSource, graphicOverlay)
      } catch (e: IOException) {
        Log.e(TAG, "Unable to start camera source.", e)
        cameraSource!!.release()
        cameraSource = null
      }
    }
  }

  public override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume")
    createCameraSource(selectedModel)
    startCameraSource()
  }

  /** Stops the camera. */
  override fun onPause() {
    super.onPause()
    preview?.stop()
  }

  public override fun onDestroy() {
    super.onDestroy()
    if (cameraSource != null) {
      cameraSource?.release()
    }
  }

  //------------- Checking Permissions
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

  @Suppress("deprecation")
  private val exercise: Exercise by lazy {
    val exe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
      intent.getParcelableExtra(EXERCISE, Exercise::class.java)
    else
      intent.getParcelableExtra(EXERCISE)
    checkNotNull(exe)
  }

  companion object {
    private const val POSE_DETECTION = "Pose Detection"

    private const val TAG = "LivePreviewActivity"
    private const val PERMISSION_REQUESTS = 1

    private val REQUIRED_RUNTIME_PERMISSIONS =
      arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,//see if is necessary
        Manifest.permission.READ_EXTERNAL_STORAGE//see if is necessary
      )
    const val EXERCISE = "EXERCISE_TYPE"
    fun navigate(context: Context, exercise: Exercise) {
      with(context) {
        val intent = Intent(this, LivePreviewActivity::class.java)
        intent.putExtra(EXERCISE, exercise)
        startActivity(intent)
      }
    }
  }
}

private fun ContentResolver.saveVideoToGallery(file: File) {
  val values = ContentValues().apply {
    put(MediaStore.Video.Media.DISPLAY_NAME, file.name)
    put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
  }

  insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
}
