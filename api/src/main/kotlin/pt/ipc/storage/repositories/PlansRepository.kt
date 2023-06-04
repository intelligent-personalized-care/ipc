package pt.ipc.storage.repositories

import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import java.time.LocalDate
import java.util.*

interface PlansRepository {

    fun createPlan(monitorID: UUID, plan: PlanInput): Int

    fun associatePlanToClient(planID: Int, clientID: UUID, startDate: LocalDate)

    fun getPlan(planID: Int): PlanOutput

    fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput?

    fun checkIfPlanIsOfMonitor(monitorID: UUID, planID: Int): Boolean

    fun checkIfExistsPlanOfClientInThisPeriod(clientID: UUID, startDate: LocalDate, endDate: LocalDate): Boolean

    fun checkIfClientAlreadyUploadedVideo(exerciseID: Int) : Boolean

    fun checkIfMonitorHasPrescribedExercise(exerciseID: Int, monitorID: UUID) : Boolean

    fun giveFeedBackOfVideo(exerciseID : Int, feedback: String)
}
