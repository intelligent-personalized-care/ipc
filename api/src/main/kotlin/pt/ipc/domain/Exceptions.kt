package pt.ipc.domain

abstract class BadRequest(msg: String) : Exception(msg)

abstract class NotFound(msg: String) : Exception(msg)

abstract class Conflit(msg: String) : Exception(msg)

abstract class UnauthorizedRequest(msg: String) : Exception(msg)

abstract class Forbidden(msg: String) : Exception(msg)

object ExerciseNotExists : BadRequest("This exercise does not exists")

object ExerciseVideoNotExists : BadRequest("This exercise video does not exists")

object Unauthenticated : UnauthorizedRequest("Unauthenticated")

object BadEmail : BadRequest("Bad Email")

object WeakPassword : BadRequest("Password too weak")

object Unauthorized : Forbidden("You cannot access this resource")

object UserNotExists : NotFound("This User does Not Exists")

object RequestNotExists : NotFound("This Request does not exists")

object MonitorNotVerified : Forbidden("You have to wait for your document to be verified ")
