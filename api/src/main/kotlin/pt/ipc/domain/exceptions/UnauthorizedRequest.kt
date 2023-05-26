package pt.ipc.domain.exceptions

abstract class UnauthorizedRequest(msg: String) : Exception(msg)

object Unauthenticated : UnauthorizedRequest("Unauthenticated")
object NotMonitorOfClient : UnauthorizedRequest("You are not the Monitor of this Client")
object NotPlanOfMonitor : UnauthorizedRequest("This plan does not belong to you")
