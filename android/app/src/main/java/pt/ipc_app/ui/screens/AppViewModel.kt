package pt.ipc_app.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipc_app.domain.user.APIException
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.models.ProblemJson
import pt.ipc_app.utils.executeRequest

/**
 * Base class for all view models that are used in the application.
 *
 * @property error the error that occurred in the view model
 */
abstract class AppViewModel : ViewModel() {

    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error


    fun <T> launchAndExecuteRequest(
        request: suspend () -> APIResult<T>,
        onSuccess: suspend (T) -> Unit
    ) {
        _error = null
        viewModelScope.launch {
            try {
                onSuccess(executeRequest(request))
            } catch (e: Exception) {
                if (e is APIException) _error = e.error
            }
        }
    }
}