package pt.ipc_app.ui.screens.exercises

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.ui.screens.exercises.info.ExerciseActivity
import pt.ipc_app.ui.screens.exercises.list.ExercisesListActivity
import pt.ipc_app.service.ExercisesService
import pt.ipc_app.service.models.sse.MonitorFeedBack
import pt.ipc_app.service.models.sse.SseEvent
import pt.ipc_app.service.sse.EventBus
import pt.ipc_app.service.sse.SseEventListener
import pt.ipc_app.preferences.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.util.*

/**
 * View model for the [ExerciseActivity] and [ExercisesListActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class ExercisesViewModel(
    private val exercisesService: ExercisesService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel(), SseEventListener {

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    private val _exercises = MutableStateFlow(listOf<ExerciseInfo>())
    val exercises
        get() = _exercises.asStateFlow()

    private var _nrSetToSee = MutableStateFlow(1)
    val nrSetToSee
        get() = _nrSetToSee.asStateFlow()

    private var _exerciseVideoFeedBack = MutableStateFlow<String?>(null)
    val exerciseVideoFeedBack
        get() = _exerciseVideoFeedBack.asStateFlow()

    /**
     * Attempts to get a preview url of an exercise.
     */
    fun getExercisePreviewUrl(
        exerciseInfoId: UUID
    ): String =
        exercisesService.getExercisePreviewUrl(exerciseInfoId)

    /**
     * Attempts to get a client video url of an exercise.
     */
    fun getExerciseVideoOfClientUrl(
        clientId: String,
        planId: Int,
        dailyListId: Int,
        exerciseId: Int
    ): String =
        exercisesService.getExerciseVideoOfClientUrl(
            clientId = clientId,
            planId = planId,
            dailyListId = dailyListId,
            exerciseId = exerciseId,
            set = nrSetToSee.value
        )

    fun selectSetToSee(set: Int) { _nrSetToSee.value = set }

    fun getFeedbackOfMonitor(
        clientId: String,
        planId: Int,
        dailyListId: Int,
        exerciseId: Int
    ) {
        launchAndExecuteRequest(
            request = {
                exercisesService.getFeedbackOfMonitor(
                    clientId = clientId,
                    planId = planId,
                    dailyListId = dailyListId,
                    exerciseId = exerciseId,
                    set = nrSetToSee.value,
                    token = sessionManager.userLoggedIn.accessToken
                )
            },
            onSuccess = {
                _exerciseVideoFeedBack.value = it.monitorFeedBack
            }
        )
    }

    fun sendFeedbackToExerciseDone(
        clientId: String,
        planId: Int,
        dailyListId: Int,
        exerciseId: Int,
        feedback: String,
    ) {
        launchAndExecuteRequest(
            request = {
                exercisesService.sendFeedbackToExerciseDone(
                    clientId = clientId,
                    planId = planId,
                    dailyListId = dailyListId,
                    exerciseId = exerciseId,
                    set = nrSetToSee.value,
                    feedback = feedback,
                    token = sessionManager.userLoggedIn.accessToken
                )
            }
        )
    }

    fun getExercises(
        skip: Int = 0
    ) {
        launchAndExecuteRequest(
            request = {
                exercisesService.getExercises(
                    skip = skip,
                    token = sessionManager.userLoggedIn.accessToken
                )
            },
            onSuccess = {
                _exercises.value = it.exercises
            }
        )
    }

    init {
        EventBus.registerListener(this)
    }

    override fun onCleared() {
        EventBus.unregisterListener(this)
        super.onCleared()
    }

    override fun onEventReceived(eventData: SseEvent) {
        if (eventData is MonitorFeedBack)
            _exerciseVideoFeedBack.value = eventData.feedBack
    }
}