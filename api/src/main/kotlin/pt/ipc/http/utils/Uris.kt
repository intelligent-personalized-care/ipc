package pt.ipc.http.utils

object Uris {
    const val HOME = "/"

    const val USER_HOME = "/users/home"

    const val CLIENT_REGISTER = "/users/clients"
    const val CLIENT_PHOTO = "/users/clients/{clientID}/profile/photo"

    const val MONITOR_RATE = "users/monitors/{monitorID}/rate"
    const val MONITOR_REGISTER = "/users/monitors"
    const val MONITOR = "/users/monitors/{monitorID}"
    const val MONITOR_SEARCH_ALL_AVAILABLE = "/users/monitors"
    const val MONITOR_PHOTO = "/users/monitors/{monitorID}/profile/photo"
    const val MONITOR_REQUESTS = "/users/monitors/{monitorID}/requests"
    const val MONITOR_DECIDE_REQUEST = "/users/monitors/{monitorID}/requests/{requestID}"

    const val EXERCISES = "/exercises"
    const val EXERCISES_INFO = "/exercises/{exerciseID}"

    const val PLANS = "/users/monitors/{monitorID}/clients/{clientID}/plans"
    const val PLAN_CURRENT = "/users/clients/{clientID}/plans/current"
    const val EXERCISES_OF_CLIENT = "/users/clients/{clientID}/exercises"
    const val PLANS_OF_MONITOR = "/users/monitors/{monitorID}/plans"
    const val PLAN_BY_ID = "/users/monitors/{monitorID}/plans/{planID}"

    const val VIDEO_OF_EXERCISE = "users/clients/{clientID}/plans/{planID}/daily_lists/{dailyListID}/exercises/{exerciseID}"
    const val VIDEO_OF_EXERCISE_FEEDBACK = "users/clients/{clientID}/plans/{planID}/daily_lists/{dailyListID}/exercises/{exerciseID}"
}
