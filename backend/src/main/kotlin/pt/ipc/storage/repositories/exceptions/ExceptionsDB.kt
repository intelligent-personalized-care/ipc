package pt.ipc.storage.repositories.exceptions

object ExceptionsDB {
    val map = hashMapOf(
        "users_email_key" to "This email already exists",
        "name_length" to "Name must be bigger than 3",
        "email_is_valid" to "This email is not valid",
        "age_is_valid" to "User Age must be bigger than 7",
        "weight_is_valid" to "Weight must be between 30 and 300 Kg",
        "height_is_valid" to "Height must be between 100 and 250 cm",
        "physical_condition_length" to "The description must be bigger than 5 characters",
        "client_diff_monitor" to "You cannot be your own monitor",
        "request_yourself" to "You cannot be your own monitor",
        "rate_yourself" to "You cannot rate yourself",
        "stars_are_valid" to "Number of stars must be between 1 and 5",
        "state_check" to "You cannot input this state",
        "title_length" to "The title must be bigger than 5",
        "duration_is_valid" to "The plan must be bigger than a day",
        "date_greater_than_current" to "The start date must be after today",
        "index_is_valid" to "The day index must be bigger or equal than 1",
        "title_length" to "The exercise title must be bigger than 5",
        "duration_is_valid" to "The plan must be bigger than a day",
        "description_length" to "The description must be bigger than or the same as 10",
        "type_length" to "The type of exercise must be bigger than 5",
        "sets_is_valid" to "The number of sets must be between 1 and 10",
        "reps_is_valid" to "The number of reps must be between 1 and 50",
        "users_pkey" to "This ID is already in use",
        "client_to_monitor_client_id_key" to "Can't have more than a monitor",
        "daily_exercises_ex_id_fkey" to "Exercise not found",
        "client_requests_monitor_id_client_id_key" to "You have already sent a request to this monitor",
        "monitor_requests_monitor_id_client_id_key" to "You have already sent a request to this monitor",
        "monitor_requests_monitor_id_fkey" to "This monitor doesn't exists",
        "unique_nr_set" to "you already post a video for this set",
        "docs_authenticity_pkey" to "You already inserted a credential",
        "valid_nr_set" to "This set does not exists",
        "client_requests_monitor_id_fkey" to "This monitor does not exists",
        "org.postgresql.util.PSQLException: ERROR: Invalid nr_set\n" +
                "  Where: função PL/pgSQL validate_exercises_video_nr_set() linha 8 em RAISE" to "This set does not exists"
    )
}
