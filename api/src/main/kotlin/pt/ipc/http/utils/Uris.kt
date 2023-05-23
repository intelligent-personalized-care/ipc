package pt.ipc.http.utils

object Uris {
    const val HOME = "/"

    const val USER_HOME = "/users/home"
    const val REGISTER_CLIENT = "/users/clients"
    const val CLIENT_PHOTO = "/users/clients/{client_id}/profile-photo"
    const val CLIENT_REQUESTS = "/users/clients/{client_id}/requests"
    const val REQUEST_DECISION = "/users/clients/{client_id}/requests/{request_id}"


    const val REGISTER_MONITOR = "/users/monitors"
    const val MONITOR_PHOTO = "users/monitors/{monitor_id}/profile-photo"
    const val REQUEST_CLIENT = "users/clients/{client_id}"
    const val MONITOR_REQUESTS = "users/monitors/{monitor_id}/requests"
    const val RATE_MONITOR = "users/monitors/{monitor_id}/rate"

    const val EXERCISES = "/exercises"
    const val EXERCISES_INFO = "/exercises/{exercise_id}"

    const val PLANS = "users/monitors/{monitor_id}/clients/{client_id}/plans"
    const val EXERCISES_OF_CLIENT = "users/clients/{client_id}/exercises"
    const val PLANS_OF_MONITOR = "users/monitors/{monitor_id}/plans"
    const val PLAN_BY_ID = "users/monitors/{monitor_id}/plans/{plan_id}"

    const val VIDEO_OF_EXERCISE = "users/clients/{client_id}/plans/{plan_id}/daily_lists/{daily_list_id}/exercises/{exercise_id}"
}
