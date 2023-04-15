package pt.ipc_app.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import pt.ipc_app.ui.theme.Grey

@Composable
fun CustomTextField(
    labelId: Int,
    textToDisplay: String,
    updateText: (string: String) -> Unit,
    readOnly: Boolean,
    hide: Boolean = false,
    enabled: Boolean = true,
    iconImageVector: ImageVector,
    keyboardType: KeyboardType? = null,
    modifier: Modifier = Modifier.padding(horizontal = 48.dp)
) {
    OutlinedTextField(
        value = textToDisplay,
        onValueChange = updateText,
        enabled = enabled,
        maxLines = 3,
        label = { stringResource(id = labelId) },
        leadingIcon = { Icon(iconImageVector, contentDescription = null) },
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