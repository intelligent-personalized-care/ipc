package pt.ipc.storage.repositories

import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.PlansOutput
import java.time.LocalDate
import java.util.*

interface PlansRepository {

    fun createPlan(monitorID: UUID, plan: PlanInput): Int

    fun associatePlanToClient(planID: Int, clientID: UUID, startDate: LocalDate)

    fun getPlan(planID: Int): PlanOutput?

    fun getPlans(monitorID: UUID): List<PlansOutput>

    fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput?

    fun checkIfPlanIsOfMonitor(monitorID: UUID, planID: Int): Boolean

    fun checkIfExistsPlanOfClientInThisPeriod(clientID: UUID, startDate: LocalDate, endDate: LocalDate): Boolean

    fun checkIfClientAlreadyUploadedVideo(clientID: UUID, planID: Int, dailyListID: Int ,exerciseID: Int, set : Int): Boolean

    fun checkIfMonitorHasPrescribedExercise(planID : Int,exerciseID: Int, monitorID: UUID): Boolean

    fun giveFeedBackOfVideo(clientID: UUID, exerciseID: Int, set: Int, feedBack: String)
}
