package pt.ipc.domain.exceptions

abstract class Forbidden(msg: String) : Exception(msg)

object Forbbiden : Forbidden("You cannot access this resource")
object MonitorNotVerified : Forbidden("You have to wait for your document to be verified")
