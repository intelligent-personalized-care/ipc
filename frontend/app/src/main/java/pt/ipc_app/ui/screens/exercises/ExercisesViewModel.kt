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

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    private val _exercises = MutableStateFlow(listOf<ExerciseInfo>())
    val exercises
        get() = _exercises.asStateFlow()

    private var _nrSet = MutableStateFlow(1)
    val nrSet
        get() = _nrSet.asStateFlow()

    private var _restTime = MutableStateFlow(30)
    val restTime
        get() = _restTime .asStateFlow()

    /**
     * Increments the current set of an exercise
     */
    fun incrementSet() = _nrSet.value++

    /**
     * After an exercise is completed resets the number of sets value
     */
    fun resetSet() { _nrSet.value = 1}

    /**
     * Decrements the rest time
     */
    fun decrementRestTime(onFinish: () -> Unit = {} ) {
        CoroutineScope(Dispatchers.IO).launch{
            while (_restTime.value > 0 ){
                delay(1000)
                _restTime.value--
            }
            onFinish()
        }
    }

    /**
     * After a rest period is completed resets the timer
     */
    fun resetRestTime() { _restTime.value = 30}

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