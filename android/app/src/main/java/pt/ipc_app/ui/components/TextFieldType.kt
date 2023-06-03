package pt.ipc_app.ui.components

import pt.ipc_app.R
import pt.ipc_app.service.utils.Errors
import pt.ipc_app.service.utils.ProblemJson

enum class TextFieldType(val labelId: Int) {
    NAME(R.string.register_screen_label_name),
    EMAIL(R.string.register_screen_label_email),
    PASSWORD(R.string.register_screen_label_password),

    BIRTH_DATE(R.string.register_screen_label_birthDate),
    WEIGHT(R.string.register_screen_label_weight),
    HEIGHT(R.string.register_screen_label_height),
    PHYSICAL_CONDITION(R.string.register_screen_label_physicalCondition),

    SEARCH(R.string.search_label),

    PLAN_NAME(R.string.create_plan_screen_label_name),
    PLAN_START_DATE(R.string.create_plan_screen_label_startDate),
}

fun TextFieldType.errorToShow(error: ProblemJson): String? =
    when {
        this == TextFieldType.EMAIL && error.title == Errors.emailAlreadyExists -> error.title
        else -> null
    }
