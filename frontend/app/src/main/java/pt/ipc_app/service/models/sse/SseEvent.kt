package pt.ipc_app.service.models.sse

open class SseEvent

fun SseEvent.getMessage(): Pair<String, String>? {
    val message = when (this) {
        is CredentialAcceptance -> "Your credential was " + if (this.acceptance) "accepted." else "declined."
        is MonitorFeedBack -> "Your monitor has already seen your video and commented \"${this.feedBack}\"."
        is PlanAssociation -> "Your monitor has associated you with a plan starting on ${this.startDate}."
        is PostedVideo -> "${this.name} just finished an exercise. You can watch his video."
        is RequestAcceptance -> "The ${this.monitor.name} monitor has accepted your connection request."
        is RequestMonitor -> "${this.name} sent you a request."
        else -> return null
    }

    return Pair(this::class.java.simpleName.replace(Regex("(\\p{Lu})"), " $1"), message)
}