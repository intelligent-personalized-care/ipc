package pt.ipc.http.utils

object Uris {
    const val HOME = "/"

    const val USER_HOME = "/users/home"

    const val CLIENT_REGISTER = "/users/clients"
    const val CLIENT_PHOTO = "/users/clients/{clientId}/profile/photo"
    const val CLIENT_REQUESTS = "/users/{clientId}/clients/requests"
    const val CLIENT_DECIDE_REQUEST = "/users/clients/{clientId}/requests/{requestId}"

    const val MONITOR_REGISTER = "/users/monitors"
    const val MONITOR_PHOTO = "/users/monitors/{monitorId}/profile/photo"
    const val MONITOR_REQUESTS = "/users/monitors/{monitorId}/requests"

    const val EXERCISES = "/exercises"
    const val EXERCISES_INFO = "/exercises/{exercise_id}"

    const val PLANS = "/users/monitors/{monitorId}/clients/{clientId}/plans"
    const val EXERCISES_OF_CLIENT = "/users/clients/{clientId}/exercises"
    const val PLANS_OF_MONITOR = "/users/monitors/{monitor_id}/plans"
    const val PLAN_BY_ID = "/users/monitors/{monitorId}/plans/{planId}"
}
