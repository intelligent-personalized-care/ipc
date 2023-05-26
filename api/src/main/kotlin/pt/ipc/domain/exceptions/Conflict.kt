package pt.ipc.domain.exceptions

abstract class Conflict(msg: String) : Exception(msg)

object ClientAlreadyHaveMonitor : Conflict("This client already have a monitor")
