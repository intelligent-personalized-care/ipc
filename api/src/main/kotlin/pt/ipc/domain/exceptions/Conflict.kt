package pt.ipc.domain.exceptions

abstract class Conflict(msg: String) : Exception(msg)

object ClientAlreadyHaveMonitor : Conflict("You already have a monitor")
object ClientAlreadyHavePlanInThisPeriod : Conflict("Client Already have Plan in this Period")
object ExerciseAlreadyUploaded : Conflict("You already uploaded a video with this exercise")

