package pt.ipc.http.utils

object Uris {

    const val USERS_SUBSCRIBE = "/users/subscribe"
    const val USERS_UNSUBSCRIBE = "/users/unsubscribe"

    const val USERS_LOGIN = "/users/login"
    const val USER_PHOTO = "/users/{userID}/photo"
    const val REFRESH_TOKEN = "/users/refresh"

    const val CLIENT_REGISTER = "/users/clients"
    const val CLIENT_PROFILE = "/users/clients/{clientID}/profile"
    const val CLIENT_PHOTO = "/users/clients/{clientID}/profile/photo"
    const val CLIENT_MONITOR = "/users/clients/{clientID}/monitor"

    const val MONITOR_BY_ID = "/users/monitors/{monitorID}"
    const val MONITOR_REQUESTS = "/users/monitors/{monitorID}/requests"
    const val MONITOR_DECIDE_REQUEST = "/users/monitors/{monitorID}/requests/{requestID}"
    const val MONITOR_RATE = "users/monitors/{monitorID}/rate"
    const val MONITOR_CREDENTIAL = "/users/monitors/{monitorID}/credential"
    const val MONITORS = "/users/monitors"
    const val MONITOR_PHOTO = "/users/monitors/{monitorID}/profile/photo"
    const val CLIENTS_OF_MONITOR = "/users/monitors/{monitorID}/clients"
    const val CLIENT_OF_MONITOR = "/users/monitors/{monitorID}/clients/{clientID}"
    const val EXERCISES_OF_CLIENTS = "/users/monitors/{monitorID}/clients/exercises"
    const val MONITOR_PROFILE = "/users/monitors/{monitorID}/profile"

    const val EXERCISES = "/exercises"
    const val EXERCISES_INFO = "/exercises/{exerciseID}"
    const val EXERCISES_INFO_VIDEO = "/exercises/{exerciseID}/video"

    const val PLANS_OF_CLIENT = "/users/monitors/{monitorID}/clients/{clientID}/plans"
    const val PLAN_CURRENT = "/users/clients/{clientID}/plans"
    const val EXERCISES_OF_CLIENT = "/users/clients/{clientID}/exercises"
    const val PLANS_OF_MONITOR = "/users/monitors/{monitorID}/plans"
    const val PLAN_BY_ID = "/users/monitors/{monitorID}/plans/{planID}"

    const val VIDEO_OF_EXERCISE = "/users/clients/{clientID}/plans/{planID}/daily_lists/{dailyListID}/exercises/{exerciseID}"
    const val EXERCISE_FEEDBACK = "/users/clients/{clientID}/plans/{planID}/daily_lists/{dailyListID}/exercises/{exerciseID}/feedback"

    const val ADMIN_CREATION = "/admin"
    const val UNVERIFIED_MONITORS = "/admin/unverified_monitors"
    const val UNVERIFIED_MONITOR = "/admin/unverified_monitors/{monitorID}"
    const val ADD_VIDEO_PREVIEW = "/admin/video_preview"
}
