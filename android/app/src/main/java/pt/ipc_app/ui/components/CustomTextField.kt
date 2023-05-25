package pt.ipc_app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipc_app.service.models.Errors
import pt.ipc_app.service.models.ProblemJson
import pt.ipc_app.ui.theme.Grey
import java.net.URI

private const val MAX_INPUT_SIZE = 100

@Composable
fun CustomTextField(
    fieldType: TextFieldType,
    textToDisplay: String,
    updateText: (String) -> Unit,
    maxLength: Int = MAX_INPUT_SIZE,
    isToTrim: Boolean = true,
    readOnly: Boolean = false,
    hide: Boolean = false,
    enabled: Boolean = true,
    iconImageVector: ImageVector? = null,
    keyboardType: KeyboardType? = null,
    error: ProblemJson? = null,
    modifier: Modifier = Modifier.padding(horizontal = 48.dp)
) {
    val text = textToDisplay.take(maxLength)
    val errorToShow = error?.let { fieldType.errorToShow(it) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        OutlinedTextField(
            value = if (isToTrim) text.trim() else text,
            onValueChange = updateText,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(fieldType.labelId)) },
            leadingIcon = { iconImageVector?.let { Icon(it, contentDescription = null) } },
            readOnly = readOnly,
            modifier = Modifier
                .semantics {
                    if (readOnly) this[SemanticsPropertyKey("ReadOnly")] =
                        Unit
                },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                leadingIconColor = Grey,
                focusedBorderColor = if (errorToShow == null) Grey else Color.Red,
                unfocusedBorderColor = if (errorToShow == null) Grey else Color.Red,
                disabledBorderColor = if (errorToShow == null) Grey else Color.Red,
                focusedLabelColor = Grey,
                unfocusedLabelColor = Grey,
                disabledLabelColor = Grey,
                disabledTextColor = Color.Black
            ),
            visualTransformation = if (hide) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if (keyboardType != null) KeyboardOptions(keyboardType = keyboardType) else KeyboardOptions.Default
        )
        errorToShow?.let {
            Text(
                text = it,
                fontSize = 12.sp,
                color = Color.Red
            )
        }
    }
}

@Preview
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        fieldType = TextFieldType.EMAIL,
        textToDisplay = "",
        updateText = { },
        iconImageVector = Icons.Default.Face,
        error = ProblemJson(URI(""), Errors.emailAlreadyExists, 1)
    )
}