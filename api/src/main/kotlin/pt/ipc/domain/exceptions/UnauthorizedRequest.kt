package pt.ipc.domain.exceptions

abstract class UnauthorizedRequest(msg: String) : Exception(msg)

object Unauthenticated : UnauthorizedRequest("Unauthenticated")
object NotMonitorOfClient : UnauthorizedRequest("You are not the Monitor of this Client")
object HasNotUploadedVideo : UnauthorizedRequest("The client has not uploaded the video")
object NotPlanOfMonitor : UnauthorizedRequest("This plan does not belong to you")
