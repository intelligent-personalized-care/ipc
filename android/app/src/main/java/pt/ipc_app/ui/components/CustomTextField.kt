package pt.ipc_app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
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
import pt.ipc_app.R
import pt.ipc_app.domain.user.User
import pt.ipc_app.ui.theme.Grey

private const val MAX_INPUT_SIZE = 100

@Composable
fun CustomTextField(
    labelId: Int,
    textToDisplay: String,
    updateText: (string: String) -> Unit,
    maxLength: Int = MAX_INPUT_SIZE,
    isToTrim: Boolean = true,
    readOnly: Boolean = false,
    hide: Boolean = false,
    enabled: Boolean = true,
    iconImageVector: ImageVector? = null,
    keyboardType: KeyboardType? = null,
    modifier: Modifier = Modifier.padding(horizontal = 48.dp)
) {
    val text =  textToDisplay.take(maxLength)

    OutlinedTextField(
        value = if (isToTrim) text.trim() else text,
        onValueChange = updateText,
        enabled = enabled,
        singleLine = true,
        label = { Text(stringResource(id = labelId)) },
        leadingIcon = { iconImageVector?.let { Icon(it, contentDescription = null) } },
        readOnly = readOnly,
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                if (readOnly) this[SemanticsPropertyKey("ReadOnly")] =
                    Unit
            },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            leadingIconColor = Grey,
            focusedBorderColor = Grey,
            unfocusedBorderColor = Grey,
            disabledBorderColor = Grey,
            focusedLabelColor = Grey,
            unfocusedLabelColor = Grey,
            disabledLabelColor = Grey,
            disabledTextColor = Color.Black,
        ),
        visualTransformation = if (hide) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (keyboardType != null) KeyboardOptions(keyboardType = keyboardType) else KeyboardOptions.Default
    )
}

@Preview
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        labelId = R.string.register_screen_label_name,
        textToDisplay = "",
        updateText = { },
        iconImageVector = Icons.Default.Face
    )
}