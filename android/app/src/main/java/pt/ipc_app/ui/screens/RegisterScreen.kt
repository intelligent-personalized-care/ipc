package pt.ipc_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import pt.ipc_app.R
import pt.ipc_app.domain.UserInfo
import pt.ipc_app.domain.UserInfo.Companion.userInfoOrNull
import pt.ipc_app.utils.DatePicker
import pt.ipc_app.utils.MyDatePicker
import pt.ipc_app.utils.CustomTextField
import kotlin.math.min


/**
 * Register screen.
 *
 * @param onSaveRequest callback to be invoked when the register button is clicked
 */
@Composable
fun RegisterScreen(
    onSaveRequest: (UserInfo) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background
    ) { innerPadding ->
        DisplayPrompts(
            innerPadding = innerPadding,
            onSaveRequest = onSaveRequest,
        )
    }
}

@Composable
fun DisplayPrompts(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onSaveRequest: (UserInfo) -> Unit = { }
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var physicalCondition by remember { mutableStateOf("") }

    val validationInfo = userInfoOrNull(name, email, password, birthDate, weight, height, physicalCondition)

    var editing by remember { mutableStateOf(validationInfo == null) }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues = innerPadding)
    ) {
        Row {
            Text(
                text = stringResource(id = R.string.register_screen_title),
                style = MaterialTheme.typography.h3,
                color = Color.Black,
            )
        }
        Column(
            modifier = modifier
        ) {
            CustomTextField(
                labelId = R.string.register_screen_label_name,
                textToDisplay = name,
                updateText = { name = ensureInputBounds(it, false) },
                readOnly = !editing,
                iconImageVector = Icons.Default.Face
            )
            CustomTextField(
                labelId = R.string.register_screen_label_email,
                textToDisplay = email,
                updateText = { email = ensureInputBounds(it) },
                readOnly = !editing,
                iconImageVector = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )
            CustomTextField(
                labelId = R.string.register_screen_label_password,
                textToDisplay = password,
                updateText = { password = ensureInputBounds(it) },
                readOnly = !editing,
                iconImageVector = Icons.Default.Password,
                hide = true,
                keyboardType = KeyboardType.Password
            )
            val dt = DatePicker(
                onDateSelected = { birthDate = it }
            )
            MyDatePicker(
                labelId = R.string.register_screen_label_birthDate,
                value = birthDate,
                onValueChange = { birthDate = ensureInputBounds(it) },
                onClick = { dt.show() }
            )
            Row {
                CustomTextField(
                    labelId = R.string.register_screen_label_weight,
                    textToDisplay = if (weight != 0) "$weight kg" else "",
                    updateText = { weight = ensureInputBounds(it).split(" ")[0].toInt() },
                    readOnly = !editing,
                    iconImageVector = Icons.Default.MonitorWeight,
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 48.dp)
                )
                CustomTextField(
                    labelId = R.string.register_screen_label_height,
                    textToDisplay = if (height != 0) "$height cm" else "",
                    updateText = { height = ensureInputBounds(it).split(" ")[0].toInt() },
                    readOnly = !editing,
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
                updateText = { physicalCondition = ensureInputBounds(it) },
                readOnly = !editing,
                iconImageVector = Icons.Default.Edit
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                if (!editing) { editing = true }
                else if (validationInfo != null) { onSaveRequest(validationInfo) }
            },
            enabled = validationInfo != null,
            shape = CircleShape,
            modifier = Modifier
                .defaultMinSize(minWidth = 56.dp, minHeight = 56.dp)
        ){
            Icon(imageVector = Icons.Default.Login, contentDescription = "")
        }
    }
}

private const val MAX_INPUT_SIZE = 50
private fun ensureInputBounds(input: String, isToTrim: Boolean = true): String {
    val out = if (isToTrim) input.trim() else input
    return out.substring(range = 0 until min(a = input.length, b = MAX_INPUT_SIZE))
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(
        onSaveRequest = {}
    )
}
