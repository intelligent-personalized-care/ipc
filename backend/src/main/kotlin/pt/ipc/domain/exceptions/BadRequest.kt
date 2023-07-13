package pt.ipc.domain.exceptions

abstract class BadRequest(msg: String) : Exception(msg)

object BadEmail : BadRequest("Bad Email")
object WeakPassword : BadRequest("Password too weak")
object AlreadyRatedThisMonitor : BadRequest("You already rated this Monitor")
object BadRating : BadRequest("Rating must be between 1 and 5")
