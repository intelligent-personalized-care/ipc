package pt.ipc.http.controllers.clients.models

import pt.ipc.domain.monitor.MonitorAvailable

data class AllMonitorsAvailableOutput(
    val monitors: List<MonitorAvailable>
)
