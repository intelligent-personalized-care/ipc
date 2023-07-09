package pt.ipc_app.ui.screens.exercises

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.ui.screens.exercises.info.ExerciseActivity
import pt.ipc_app.ui.screens.exercises.list.ExercisesListActivity
import pt.ipc_app.service.ExercisesService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.io.File
import java.util.*

/**
 * View model for the [ExerciseActivity] and [ExercisesListActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class ExercisesViewModel(
    private val exercisesService: ExercisesService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val defaultRestTime = 30
    private val defaultDelay = 1000
    private val defaultNofSets = 1
    private val defaultMaxRecordTimeLimit = 180

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    private val _exercises = MutableStateFlow(listOf<ExerciseInfo>())
    val exercises
        get() = _exercises.asStateFlow()

    private var _nrSet = MutableStateFlow(defaultNofSets)
    val nrSet
        get() = _nrSet.asStateFlow()

    private var _restTime = MutableStateFlow(defaultRestTime)
    val restTime
        get() = _restTime .asStateFlow()

    private var _recordTime = MutableStateFlow(0)
    val recordTime
        get() = _recordTime .asStateFlow()

    private var _stopRecordTime = MutableStateFlow(false)
    val stopRecordTime
        get() = _stopRecordTime .asStateFlow()


    /**
     * Increments the current set of an exercise
     */
    fun incrementSet() = _nrSet.value++

    /**
     * After an exercise is completed resets the number of sets value
     */
    fun resetSet() { _nrSet.value = defaultNofSets}

    /**
     * Decrements the rest time
     */
    fun decrementRestTime(onFinish: () -> Unit = {} ) {
        CoroutineScope(Dispatchers.IO).launch{
            while (restTime.value > 0 ){
                delay(defaultDelay.toLong())
                _restTime.value--
            }
            onFinish()
        }
    }

    /**
     * After a rest period is completed resets the timer
     */
    fun resetRestTime() { _restTime.value = defaultRestTime }

    /**
     * Returns true if client is resting
     */
    fun isResting() =
        restTime.value in 1 until defaultRestTime

    /**
     * Increment the record time
     */
    fun incrementRecordTime(onStop: () -> Unit) {
        _stopRecordTime.value = false
        CoroutineScope(Dispatchers.IO).launch{
            //max time allowed of a video
            while (recordTime.value < defaultMaxRecordTimeLimit && !stopRecordTime.value){
                delay(defaultDelay.toLong())
                _recordTime.value++
            }
            onStop()
        }
    }

    /**
     * Changes the stopRecordTime var to allow the stoppage of a video and resets the timer
     */
    fun stopRecordTime() {
        _stopRecordTime.value = true
        _recordTime.value = 0
    }

    /**
     * Returns true if client is recording
     */
    fun isRecording() =
        recordTime.value != 0


    /**
     * Attempts to get a preview url of an exercise.
     */
    fun getExercisePreviewUrl(
        exerciseInfoId: UUID
    ): String =
        exercisesService.getExercisePreviewUrl(exerciseInfoId)

    fun getExercises(
        skip: Int = 0
    ) {
        launchAndExecuteRequest(
            request = {
                exercisesService.getExercises(
                    skip = skip,
                    token = sessionManager.userLoggedIn.token
                )
            },
            onSuccess = {
                _exercises.value = it.exercises
            }
        )
    }

    /**
     * Attempts to submit an exercise of client.
     */
    fun submitExerciseVideo(
        video: File,
        planId: Int,
        dailyListId: Int,
        exerciseId: Int,
        set: Int
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                exercisesService.submitExerciseVideo(
                    video = video,
                    clientId = sessionManager.userUUID,
                    planId = planId,
                    dailyListId = dailyListId,
                    exerciseId = exerciseId,
                    set = set,
                    token = sessionManager.userLoggedIn.token
                ).also {
                    if (it !is APIResult.Success) _state.value = ProgressState.IDLE
                }
            },
            onSuccess = {
                _state.value = ProgressState.FINISHED
            }
        )
    }
}