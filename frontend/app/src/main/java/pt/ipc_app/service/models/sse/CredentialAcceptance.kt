package pt.ipc_app.service.models.sse

data class CredentialAcceptance(
    val acceptance: Boolean
): SseEvent()