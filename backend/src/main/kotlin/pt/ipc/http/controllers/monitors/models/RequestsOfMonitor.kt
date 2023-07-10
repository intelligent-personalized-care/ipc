package pt.ipc.http.controllers.monitors.models

import pt.ipc.domain.monitor.RequestInformation

data class RequestsOfMonitor(
    val requests: List<RequestInformation>
)