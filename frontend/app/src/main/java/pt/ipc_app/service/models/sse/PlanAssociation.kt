package pt.ipc_app.service.models.sse

import pt.ipc_app.domain.Plan

data class PlanAssociation(
    val planOutput: Plan
): SseEvent()