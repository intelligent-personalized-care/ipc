package pt.ipc_app.service.models.sse

data class PlanAssociation(
    val title: String,
    val startDate: String
): SseEvent()