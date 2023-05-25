package pt.ipc.http.models

import pt.ipc.domain.MonitorDetails

data class AllMonitorsAvailableOutput(
    val monitors: List<MonitorDetails>
)