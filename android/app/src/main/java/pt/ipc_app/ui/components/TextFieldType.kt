package pt.ipc_app.ui.components

import pt.ipc_app.R
import pt.ipc_app.service.models.Errors
import pt.ipc_app.service.models.ProblemJson

enum class TextFieldType(val labelId: Int) {
    NAME(R.string.register_screen_label_name),
    EMAIL(R.string.register_screen_label_email),
    PASSWORD(R.string.register_screen_label_password),
    DATE(R.string.register_screen_label_birthDate),
    WEIGHT(R.string.register_screen_label_weight),
    HEIGHT(R.string.register_screen_label_height),
    PHYSICAL_CONDITION(R.string.register_screen_label_physicalCondition)
}

fun TextFieldType.errorToShow(error: ProblemJson): String? =
    when {
        this == TextFieldType.EMAIL && error.title == Errors.emailAlreadyExists -> error.title
        else -> null
    }
