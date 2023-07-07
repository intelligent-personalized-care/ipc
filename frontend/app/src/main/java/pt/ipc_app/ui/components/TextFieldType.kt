package pt.ipc_app.ui.components

import pt.ipc_app.R
import pt.ipc_app.service.utils.Errors
import pt.ipc_app.service.utils.ProblemJson

enum class TextFieldType(val labelId: Int, val required: Boolean = false) {
    NAME(R.string.register_screen_label_name, true),
    EMAIL(R.string.register_screen_label_email, true),
    PASSWORD(R.string.register_screen_label_password, true),

    BIRTH_DATE(R.string.birthDate),
    WEIGHT(R.string.weight),
    HEIGHT(R.string.height),
    PHYSICAL_CONDITION(R.string.physicalCondition),

    SEARCH(R.string.search_label),

    PLAN_NAME(R.string.create_plan_screen_label_name),
    PLAN_START_DATE(R.string.create_plan_screen_label_startDate),

    EXERCISE_SETS(R.string.create_plan_screen_label_exercise_sets),
    EXERCISE_REPS(R.string.create_plan_screen_label_exercise_reps)
}

fun TextFieldType.errorToShow(error: ProblemJson): String? =
    when {
        this == TextFieldType.EMAIL && error.title == Errors.emailAlreadyExists -> error.title
        else -> null
    }
