package pt.ipc_app.ui.screens.exercise

import pt.ipc_app.service.ExercisesService
import pt.ipc_app.session.SessionManagerSharedPrefs
import pt.ipc_app.ui.screens.AppViewModel

/**
 * View model for the [ExerciseActivity].
 *
 * @param sessionManager the manager used to handle the user session
 */
class ExerciseViewModel(
    private val exercisesService: ExercisesService,
    private val sessionManager: SessionManagerSharedPrefs
) : AppViewModel() {

}