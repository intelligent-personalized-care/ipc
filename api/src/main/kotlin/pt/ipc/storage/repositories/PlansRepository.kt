package pt.ipc.storage.repositories

import pt.ipc.domain.Plan
import pt.ipc.domain.PlanOutput
import java.time.LocalDate
import java.util.*

interface PlansRepository {

    fun createPlan(monitorID: UUID, clientID: UUID, plan: Plan): Int

    fun getPlan(planID: Int): PlanOutput

    fun getCurrentPlanOfClient(clientID: UUID, date : LocalDate): PlanOutput?

    fun checkIfPlanIsOfMonitor(monitorID: UUID, planID: Int): Boolean

    fun checkIfExistsPlanInThisPeriod(period: List<LocalDate>): Boolean
}
