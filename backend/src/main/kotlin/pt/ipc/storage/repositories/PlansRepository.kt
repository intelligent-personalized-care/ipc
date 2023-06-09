package pt.ipc.storage.repositories

import pt.ipc.domain.plan.PlanInfoOutput
import pt.ipc.domain.plan.PlanInput
import pt.ipc.domain.plan.PlanOutput
import java.time.LocalDate
import java.util.*

interface PlansRepository {

    fun createPlan(monitorID: UUID, plan: PlanInput): Int

    fun associatePlanToClient(planID: Int, clientID: UUID, startDate: LocalDate, endDate: LocalDate)

    fun getPlan(planID: Int): PlanOutput?

    fun getPlanOfMonitor(planID: Int): PlanOutput?

    fun getPlans(monitorID: UUID): List<PlanInfoOutput>

    fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput?

    fun checkIfPlanIsOfMonitor(monitorID: UUID, planID: Int): Boolean

    fun checkIfExistsPlanOfClientInThisPeriod(clientID: UUID, startDate: LocalDate, endDate: LocalDate): Boolean

    fun checkIfClientAlreadyUploadedVideo(clientID: UUID, planID: Int, dailyListID: Int, exerciseID: Int, set: Int): Boolean

    fun checkIfMonitorHasPrescribedExercise(planID: Int, exerciseID: Int, monitorID: UUID): Boolean

    fun giveFeedBackOfVideo(clientID: UUID, exerciseID: Int, set: Int, feedBack: String)
}
