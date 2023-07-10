package pt.ipc.http.controllers.admin.models

import pt.ipc.services.dtos.MonitorInfo

data class ListOfUnverifiedMonitors(val monitors: List<MonitorInfo>)
