package pt.ipc_app.ui.screens.plan

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.service.ExercisesService
import pt.ipc_app.service.PlansService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.plans.PlanInput
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel
import java.util.*

/**
 * View model for the [CreatePlanActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class CreatePlanViewModel(
    private val plansService: PlansService,
    private val exercisesService: ExercisesService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

    private val _state = MutableStateFlow(ProgressState.IDLE)
    val state
        get() = _state.asStateFlow()

    private val _exercises = MutableStateFlow(listOf<ExerciseInfo>())
    val exercises
        get() = _exercises.asStateFlow()

    /**
     * Attempts to get all the exercises.
     */
    fun getExercises() {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                exercisesService.getExercises(
                    token = sessionManager.userInfo!!.token
                ).also {
                    _state.value = if (it is APIResult.Success) ProgressState.FINISHED else ProgressState.IDLE
                }

            },
            onSuccess = {
                _state.value = ProgressState.FINISHED
                _exercises.value = it.exercises
            }
        )
    }

    /**
     * Attempts to create a plan.
     */
    fun createPlan(
        plan: PlanInput
    ) {
        launchAndExecuteRequest(
            request = {
                _state.value = ProgressState.WAITING
                plansService.createPlan(
                    plan = plan,
                    monitorId = UUID.fromString(sessionManager.userInfo!!.id),
                    token = sessionManager.userInfo!!.token
                ).also {
                    _state.value = if (it is APIResult.Success) ProgressState.FINISHED else ProgressState.IDLE
                }

            },
            onSuccess = {
                _state.value = ProgressState.FINISHED
            }
        )
    }

}