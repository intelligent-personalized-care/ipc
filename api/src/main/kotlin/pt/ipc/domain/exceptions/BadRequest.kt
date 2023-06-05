package pt.ipc.domain.exceptions

abstract class BadRequest(msg: String) : Exception(msg)

object ExerciseNotExists : BadRequest("This exercise does not exists")
object ExerciseVideoNotExists : BadRequest("This exercise video does not exists")
object BadEmail : BadRequest("Bad Email")
object WeakPassword : BadRequest("Password too weak")
object AlreadyRatedThisMonitor : BadRequest("You already rated this Monitor")
object ClientDontHaveThisExercise : BadRequest("You don't have this exercise")
