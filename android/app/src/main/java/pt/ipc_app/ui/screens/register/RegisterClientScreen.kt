package pt.ipc_app.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.domain.user.Client
import pt.ipc_app.domain.user.User
import pt.ipc_app.ui.screens.AppScreen
import pt.ipc_app.ui.components.CustomTextField
import pt.ipc_app.ui.components.ProgressState
import pt.ipc_app.ui.components.RegisterButton
import pt.ipc_app.utils.DatePicker
import pt.ipc_app.utils.MyDatePicker

private const val WEIGHT_METRIC = " kg"
private const val HEIGHT_METRIC = " cm"

/**
 * Register user screen.
 *
 * @param onSaveRequest callback to be invoked when the register button is clicked
 */
@Composable
fun RegisterClientScreen(
    progressState: ProgressState = ProgressState.Idle,
    onSaveRequest: (Client) -> Unit = { }
) {
    var userInfo: User? by remember { mutableStateOf(null) }

    var birthDate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var physicalCondition by remember { mutableStateOf("") }

    val clientValidation = userInfo?.let {
        Client.clientOrNull(
            it.name, it.email, it.password, birthDate, weight, height, physicalCondition
        )
    }

    AppScreen {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Row {
                Text(
                    text = stringResource(id = R.string.register_client_screen_title),
                    style = MaterialTheme.typography.h5,
                    color = Color.Black,
                )
            }
            Column {
                UserRegister(
                    userValidation = { userInfo = it }
                )

                val dt = DatePicker(
                    onDateSelected = { birthDate = it }
                )
                MyDatePicker(
                    labelId = R.string.register_screen_label_birthDate,
                    value = birthDate,
                    onValueChange = { birthDate = it },
                    onClick = { dt.show() }
                )
                Row {
                    CustomTextField(
                        labelId = R.string.register_screen_label_weight,
                        textToDisplay = if (weight != 0) "$weight$WEIGHT_METRIC" else "",
                        updateText = { weight = it.toInteger(WEIGHT_METRIC, Client.WEIGHT_LENGTH_MAX) },
                        iconImageVector = Icons.Default.MonitorWeight,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(start = 48.dp)
                    )
                    CustomTextField(
                        labelId = R.string.register_screen_label_height,
                        textToDisplay = if (height != 0) "$height$HEIGHT_METRIC" else "",
                        updateText = { height = it.toInteger(HEIGHT_METRIC, Client.HEIGHT_LENGTH_MAX) },
                        iconImageVector = Icons.Default.Height,
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(end = 48.dp)
                    )
                }
                CustomTextField(
                    labelId = R.string.register_screen_label_physicalCondition,
                    textToDisplay = physicalCondition,
                    updateText = { physicalCondition = it },
                    maxLength = Client.PHYSICAL_CONDITION_LENGTH_RANGE.last,
                    isToTrim = false,
                    iconImageVector = Icons.Default.Edit
                )
            }
            RegisterButton(
                validationInfo = clientValidation,
                state = progressState,
                onClick = { if (clientValidation != null) onSaveRequest(clientValidation) }
            )
        }
    }
}

private fun String.toInteger(toSplit: String, maxLength: Int): Int {
    val value = split(toSplit)[0]

    return if (value.isEmpty() || value.toIntOrNull() == null || value.length > maxLength) 0
    else value.toInt()
}
