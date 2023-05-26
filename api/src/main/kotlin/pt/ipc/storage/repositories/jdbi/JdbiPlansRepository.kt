package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.DailyList
import pt.ipc.domain.Exercise
import pt.ipc.domain.Plan
import pt.ipc.domain.PlanOutput
import pt.ipc.storage.repositories.PlansRepository
import java.time.LocalDate
import java.util.*

class JdbiPlansRepository(
    private val handle: Handle
) : PlansRepository {

    override fun createPlan(monitorID: UUID, clientID: UUID, plan: Plan): Int {
        val planID = handle.createQuery("insert into dbo.plans (monitor_id, title) values(:monitorID, :title) returning id")
            .bind("monitorID", monitorID)
            .bind("title", plan.title)
            .mapTo<Int>()
            .first()

        plan.dailyLists.forEachIndexed { index, dailyListCreation ->
            val dailyListID =
                handle.createQuery("insert into dbo.daily_lists (index, plan_id) values (:index,:planID) returning id")
                    .bind("index", index)
                    .bind("planID", planID)
                    .mapTo<Int>()
                    .first()

            dailyListCreation?.exercises?.forEach { exerciseCreation ->
                handle.createUpdate("insert into dbo.daily_exercises (ex_id, daily_list_id, sets, reps) values (:exID,:dailyListID,:sets,:reps)")
                    .bind("exID", exerciseCreation.exerciseID)
                    .bind("dailyListID", dailyListID)
                    .bind("sets", exerciseCreation.sets)
                    .bind("reps", exerciseCreation.reps)
                    .execute()
            }
        }

        handle.createUpdate("insert into dbo.client_plans (plan_id, client_id, dt_start) values(:planID,:clientID,:dtStart)")
            .bind("planID", planID)
            .bind("clientID", clientID)
            .bind("dtStart", plan.startDate)
            .execute()

        return planID
    }

    override fun getPlan(planID: Int): PlanOutput {
        val title = handle.createQuery("select title from dbo.plans where id = :planID")
            .bind("planID", planID)
            .mapTo<String>()
            .single()

        val dtStart = handle.createQuery("select dt_start from dbo.client_plans where plan_id = :planID ")
            .bind("planID", planID)
            .mapTo<LocalDate>()
            .single()

        val dailyListsID: List<Int> =
            handle.createQuery("select id from dbo.daily_lists where plan_id = :planID")
                .bind("planID", planID)
                .mapTo<Int>()
                .toList()

        val dailyLists = mutableListOf<DailyList?>()

        dailyListsID.forEachIndexed { index, dailyListID ->

            val exercises: List<Exercise>? =
                handle.createQuery("select ex_id,sets,reps from dbo.daily_exercises where daily_list_id = :dailyListID")
                    .bind("dailyListID", dailyListID)
                    .mapTo<Exercise>()
                    .toList()
                    .ifEmpty { null }

            exercises?.let { dailyLists.add(index, DailyList(it)) } ?: dailyLists.add(index, null)
        }

        return PlanOutput(
            planID = planID,
            plan = Plan(
                title = title,
                startDate = dtStart,
                dailyLists = dailyLists
            )
        )
    }

    override fun checkIfPlanIsOfMonitor(monitorID: UUID, planID: Int): Boolean =
        handle.createQuery("select count(*) from dbo.plans where id = :planID and monitor_id = :monitorID")
            .bind("planID", planID)
            .bind("monitorID", monitorID)
            .mapTo<Int>()
            .single() == 1

    override fun getCurrentPlanOfClient(clientID: UUID): Plan? {
        val today = LocalDate.now()

        val planId = handle.createQuery(
            """
                
            """.trimIndent()
        )
            .bind("clientID", clientID)
            .mapTo<Int>()
            .singleOrNull()

        return null
    }
}
