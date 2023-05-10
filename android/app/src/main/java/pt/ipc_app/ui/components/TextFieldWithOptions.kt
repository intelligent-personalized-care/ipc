package pt.ipc_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/**
 * Use [CustomTextField] to display a list with options and you can select one of them.
 */
@Composable
fun TextFieldWithOptions(
    fieldType: TextFieldType,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    CustomTextField(
        fieldType = fieldType,
        textToDisplay = selectedOption,
        updateText = { onOptionSelected(it) },
        readOnly = true,
        enabled = false,
        modifier = modifier
            .clickable(onClick = { isExpanded = !isExpanded }),
        iconImageVector =
            if (isExpanded) Icons.Filled.KeyboardArrowUp
            else Icons.Filled.KeyboardArrowDown
    )
    Box {
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    isExpanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}

@Preview
@Composable
fun TextFieldWithOptionsPreview() {
    var selectedOption by remember { mutableStateOf("") }
    TextFieldWithOptions(
        selectedOption = selectedOption,
        fieldType = TextFieldType.HEIGHT,
        options = List(150) { (100 + it).toString() },
        onOptionSelected = { selectedOption = it}
    )
}
