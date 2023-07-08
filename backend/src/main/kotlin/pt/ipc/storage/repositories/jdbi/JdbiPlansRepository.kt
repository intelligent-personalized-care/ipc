package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.DailyExercise
import pt.ipc.domain.DailyListOutput
import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.PlanInfoOutput
import pt.ipc.storage.repositories.PlansRepository
import java.time.LocalDate
import java.util.*

class JdbiPlansRepository(
    private val handle: Handle
) : PlansRepository {

    override fun createPlan(monitorID: UUID, plan: PlanInput): Int {
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
                    .bind("exID", exerciseCreation.exerciseInfoID)
                    .bind("dailyListID", dailyListID)
                    .bind("sets", exerciseCreation.sets)
                    .bind("reps", exerciseCreation.reps)
                    .execute()
            }
        }
        return planID
    }

    override fun associatePlanToClient(planID: Int, clientID: UUID, startDate: LocalDate, endDate: LocalDate) {
        handle.createUpdate("insert into dbo.client_plans (plan_id,client_id,dt_start, dt_end) values(:planID, :clientID, :startDate, :endDate)")
            .bind("planID", planID)
            .bind("clientID", clientID)
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .execute()
    }

    override fun getPlan(planID: Int): PlanOutput? {
        val title = handle.createQuery("select title from dbo.plans where id = :planID")
            .bind("planID", planID)
            .mapTo<String>()
            .singleOrNull() ?: return null

        val dailyListsID: List<Int> =
            handle.createQuery("select id from dbo.daily_lists where plan_id = :planID")
                .bind("planID", planID)
                .mapTo<Int>()
                .toList()

        val dailyLists = mutableListOf<DailyListOutput?>()

        dailyListsID.forEachIndexed { index, dailyListID ->

            val exercises: List<DailyExercise>? =
                handle.createQuery(
                    """
                    select de.id, de.ex_id,dl.plan_id as planID, dl.id as dailyListID, ei.title, ei.description, ei.type, de.sets, de.reps,
                        case when count(ev.ex_id) != de.sets then 0 else 1 end as is_done
                    from dbo.daily_exercises de 
                    inner join dbo.daily_lists dl on de.daily_list_id = dl.id
                    inner join dbo.exercises_info ei on ei.id = de.ex_id
                    left join dbo.exercises_video ev on de.id = ev.ex_id
                    where daily_list_id = :dailyListID
                    group by de.id, dl.plan_id, dl.id,ei.title, ei.description, ei.type, de.sets, de.reps
                    """.trimIndent()
                )
                    .bind("dailyListID", dailyListID)
                    .mapTo<DailyExercise>()
                    .toList()
                    .ifEmpty { null }

            dailyLists.add(
                index,
                if (exercises != null) {
                    DailyListOutput(dailyListID, exercises)
                } else {
                    null
                }
            )
        }

        return PlanOutput(
            id = planID,
            title = title,
            dailyLists = dailyLists
        )
    }

    override fun getPlans(monitorID: UUID): List<PlanInfoOutput> {
        return handle.createQuery(
            """
                select p.id, p.title, count(dl.id) as days from dbo.plans p 
                inner join dbo.monitors m on p.monitor_id = m.m_id 
                inner join dbo.daily_lists dl on p.id = dl.plan_id
                where m.m_id = :monitorID
                group by p.id
            """.trimIndent()
        )
            .bind("monitorID", monitorID)
            .mapTo<PlanInfoOutput>()
            .toList()
    }

    private data class PlanStartDate(val planId: Int, val dtStart: LocalDate)

    override fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput? {
        val plan = handle.createQuery(
            """
                select cp.plan_id, cp.dt_start from dbo.client_plans cp 
                where cp.client_id = :clientID and :date between cp.dt_start and cp.dt_end
            """.trimIndent()
        )
            .bind("clientID", clientID)
            .bind("date", date)
            .mapTo<PlanStartDate>()
            .singleOrNull() ?: return null

        return getPlan(plan.planId)?.copy(startDate = plan.dtStart)
    }

    override fun checkIfPlanIsOfMonitor(monitorID: UUID, planID: Int): Boolean =
        handle.createQuery("select count(*) from dbo.plans where id = :planID and monitor_id = :monitorID")
            .bind("planID", planID)
            .bind("monitorID", monitorID)
            .mapTo<Int>()
            .single() == 1

    override fun checkIfExistsPlanOfClientInThisPeriod(clientID: UUID, startDate: LocalDate, endDate: LocalDate): Boolean {
        return handle.createQuery(
            "select count(*) from dbo.client_plans cp where " +
                "(cp.dt_end >= :startDate and :endDate >= cp.dt_start) and cp.client_id = :clientID"
        )
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .bind("clientID", clientID)
            .mapTo<Int>()
            .single() >= 1
    }

    override fun checkIfMonitorHasPrescribedExercise(planID: Int, exerciseID: Int, monitorID: UUID): Boolean {
        return handle.createQuery(
            "select count(*) from dbo.daily_exercises de " +
                "inner join dbo.daily_lists dl on de.daily_list_id = dl.id " +
                "inner join dbo.plans p on dl.plan_id = p.id " +
                "where de.id = :exerciseID and p.monitor_id = :monitorID and p.id = :planID"
        )
            .bind("exerciseID", exerciseID)
            .bind("monitorID", monitorID)
            .bind("planID", planID)
            .mapTo<Int>()
            .single() == 1
    }

    override fun checkIfClientAlreadyUploadedVideo(
        clientID: UUID,
        planID: Int,
        dailyListID: Int,
        exerciseID: Int,
        set: Int
    ): Boolean {
        return handle.createQuery(
            """
                select count(*) from dbo.exercises_video ev
                inner join dbo.daily_exercises de on ev.ex_id = de.id
                inner join dbo.daily_lists dl on dl.id = de.daily_list_id
                where ev.nr_set = :set and ev.ex_id = :exerciseID and dl.id = :dailyListID and dl.plan_id = :planID
            """.trimIndent()
        )
            .bind("set", set)
            .bind("exerciseID", exerciseID)
            .bind("dailyListID", dailyListID)
            .bind("planID", planID)
            .mapTo<Int>()
            .single() == 1
    }

    override fun giveFeedBackOfVideo(clientID: UUID, exerciseID: Int, set: Int, feedBack: String) {
        handle.createUpdate(
            "update dbo.exercises_video set feedback_monitor = :feedBack where ex_id = :exerciseID and client_id = :clientID and nr_set = :set"
        )
            .bind("feedBack", feedBack)
            .bind("exerciseID", exerciseID)
            .bind("clientID", clientID)
            .bind("set", set)
            .execute()
    }
}
