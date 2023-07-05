package pt.ipc_app.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.ipc_app.domain.APIException
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.utils.ProblemJson
import pt.ipc_app.ui.components.bottomBar.ButtonBarType
import pt.ipc_app.utils.executeRequest

/**
 * Base class for all view models that are used in the application.
 *
 * @property error the error that occurred in the view model
 */
open class AppViewModel : ViewModel() {

    private var _buttonBarClicked by mutableStateOf(ButtonBarType.HOME)
    val buttonBarClicked : ButtonBarType
        get() = _buttonBarClicked

    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error

    fun changeButtonBar(buttonBarType: ButtonBarType) {
        if (buttonBarClicked != buttonBarType) _buttonBarClicked = buttonBarType
    }

    fun <T> launchAndExecuteRequest(
        request: suspend () -> APIResult<T>,
        onSuccess: suspend (T) -> Unit
    ) {
        _error = null
        viewModelScope.launch {
            try {
                onSuccess(executeRequest(request))
            } catch (e: Exception) {
                if (e is APIException) {
                    _error = e.error
                    Log.println(Log.WARN, "ERROR", e.error.title)
                }
            }
        }
    }
}