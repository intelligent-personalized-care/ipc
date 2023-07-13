package pt.ipc.domain.exceptions

abstract class NotFound(msg: String) : Exception(msg)

object UserNotExists : NotFound("This User does Not Exists")
object MonitorNotFound : NotFound("Monitor not found")
object PlanNotFound : NotFound("Plan not found")
object RequestNotExists : NotFound("This Request does not exists")
object ClientDontHavePlan : NotFound("This Client does not have a plan assigned")
object ClientNotPostedVideo : NotFound("Client has not posted the video yet")
object ExerciseNotExists : NotFound("This exercise does not exists")
object FileDoesNotExists : NotFound("This file does not exists")
object ClientDontHaveThisExercise : NotFound("You don't have this exercise")
