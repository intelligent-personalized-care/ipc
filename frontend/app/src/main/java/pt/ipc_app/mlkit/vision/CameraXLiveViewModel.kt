package pt.ipc_app.mlkit.vision

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.ipc_app.ui.screens.AppViewModel

/**
 * View model for the [CameraXLivePreviewActivity].
 */
class CameraXLiveViewModel : AppViewModel() {

    companion object {
        private const val MAX_RECORD_TIME = 180
        private const val MAX_REST_TIME = 30
        private const val ONE_SECOND_DELAY = 1000L
    }

    private var _nrRepsDone = MutableStateFlow(0)
    val nrRepsDone
        get() = _nrRepsDone.asStateFlow()

    private var _nrSetsDone = MutableStateFlow(1)
    val nrSetsDone
        get() = _nrSetsDone.asStateFlow()

    private var _restTime = MutableStateFlow(MAX_REST_TIME)
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
    fun incrementReps() = _nrRepsDone.value++

    /**
     * After a set is completed resets the reps
     */
    fun resetRepsCount() { _nrRepsDone.value = 0 }

    /**
     * Increments the current set of an exercise
     */
    fun incrementSets() = _nrSetsDone.value++

    /**
     * Returns true if client is recording
     */
    fun isRecording() =
        recordTime.value != 0

    /**
     * Returns true if client is resting
     */
    fun isResting() =
        restTime.value in 1 until MAX_REST_TIME

    /**
     * Decrements the rest time
     */
    fun decrementRestTime(onFinish: () -> Unit = {} ) {
        viewModelScope.launch{
            while (restTime.value > 0 ){
                delay(ONE_SECOND_DELAY)
                _restTime.value--
            }
            onFinish()
        }
    }

    /**
     * After a rest period is completed resets the timer
     */
    fun resetRestTime() { _restTime.value = MAX_REST_TIME }

    /**
     * Increment the record time
     */
    fun incrementRecordTime(onStop: () -> Unit) {
        _stopRecordTime.value = false
        viewModelScope.launch{
            //max time allowed of a video
            while (recordTime.value < MAX_RECORD_TIME && !stopRecordTime.value){
                delay(ONE_SECOND_DELAY)
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
}