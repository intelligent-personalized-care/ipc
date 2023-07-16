package pt.ipc_app.ui.screens.plan

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pt.ipc_app.domain.exercise.ExerciseInfo
import pt.ipc_app.service.ExercisesService
import pt.ipc_app.service.PlansService
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.plans.PlanInput
import pt.ipc_app.preferences.SessionManagerSharedPrefs
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.screens.AppViewModel

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

    private val _planState = MutableStateFlow(ProgressState.IDLE)
    val planState
        get() = _planState.asStateFlow()

    private val _exercisesState = MutableStateFlow(ProgressState.IDLE)
    val exercisesState
        get() = _exercisesState.asStateFlow()

    private val _exercises = MutableStateFlow(listOf<ExerciseInfo>())
    val exercises
        get() = _exercises.asStateFlow()

    /**
     * Attempts to get all the exercises.
     */
    fun getExercises(
        skip: Int = 0
    ) {
        launchAndExecuteRequest(
            request = {
                _exercisesState.value = ProgressState.WAITING
                exercisesService.getExercises(
                    skip = skip,
                    token = sessionManager.userLoggedIn.accessToken
                ).also {
                    _exercisesState.value = if (it is APIResult.Success) ProgressState.FINISHED else ProgressState.IDLE
                }

            },
            onSuccess = {
                _exercisesState.value = ProgressState.FINISHED
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
                _planState.value = ProgressState.WAITING
                plansService.createPlan(
                    plan = plan,
                    monitorId = sessionManager.userUUID,
                    token = sessionManager.userLoggedIn.accessToken
                ).also {
                    _planState.value = if (it is APIResult.Success) ProgressState.FINISHED else ProgressState.IDLE
                }

            },
            onSuccess = {
                _planState.value = ProgressState.FINISHED
            }
        )
    }

}