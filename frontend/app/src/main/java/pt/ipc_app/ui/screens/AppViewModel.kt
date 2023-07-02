package pt.ipc_app.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ipc_app.domain.APIException
import pt.ipc_app.service.connection.APIResult
import pt.ipc_app.service.utils.ProblemJson
import pt.ipc_app.utils.executeRequest

/**
 * Base class for all view models that are used in the application.
 *
 * @property error the error that occurred in the view model
 */
abstract class AppViewModel : ViewModel() {

    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val _messages = MutableLiveData<Pair<Boolean, String>>()
    val messages: LiveData<Pair<Boolean, String>> = _messages

    private var _error by mutableStateOf<ProblemJson?>(null)
    val error : ProblemJson?
        get() = _error

    fun addMessage(message: Pair<Boolean, String>) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            _messages.value = message
        }
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
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