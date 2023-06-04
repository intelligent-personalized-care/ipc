package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.DailyListOutput
import pt.ipc.domain.ExerciseTotalInfo
import pt.ipc.domain.PlanInput
import pt.ipc.domain.PlanOutput
import pt.ipc.http.models.ListOfPlans
import pt.ipc.http.models.PlansOutput
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

    override fun associatePlanToClient(planID: Int, clientID: UUID, startDate: LocalDate) {
        handle.createUpdate("insert into dbo.client_plans (plan_id,client_id,dt_start) values(:planID, :clientID, :startDate)")
            .bind("planID",planID)
            .bind("clientID",clientID)
            .bind("startDate",startDate)
            .execute()
    }

    override fun getPlan(planID: Int): PlanOutput {
        val title = handle.createQuery("select title from dbo.plans where id = :planID")
            .bind("planID", planID)
            .mapTo<String>()
            .single()

        val dtStart = handle.createQuery("select dt_start from dbo.client_plans where plan_id = :planID ")
            .bind("planID", planID)
            .mapTo<LocalDate>()
            .singleOrNull()

        val dailyListsID: List<Int> =
            handle.createQuery("select id from dbo.daily_lists where plan_id = :planID")
                .bind("planID", planID)
                .mapTo<Int>()
                .toList()

        val dailyLists = mutableListOf<DailyListOutput?>()

        dailyListsID.forEachIndexed { index, dailyListID ->

            val exercises: List<ExerciseTotalInfo>? =
                handle.createQuery("""
                    select de.id, de.ex_id, title, description, type, sets, reps from dbo.daily_exercises de inner join dbo.exercises_info ei on ei.id = de.ex_id
                    where daily_list_id = :dailyListID
                    """.trimIndent())
                    .bind("dailyListID", dailyListID)
                    .mapTo<ExerciseTotalInfo>()
                    .toList()
                    .ifEmpty { null }

            exercises?.let { dailyLists.add(index, DailyListOutput(it)) } ?: dailyLists.add(index, null)
        }

        return PlanOutput(
            id = planID,
            title = title,
            startDate = dtStart,
            dailyLists = dailyLists
        )
    }

    override fun getPlans(monitorID: UUID) : List<PlansOutput>{
        return handle.createQuery("select p.id,p.title from dbo.plans p inner join dbo.monitors m on p.monitor_id = m.m_id where m.m_id = :monitorID")
            .bind("monitorID",monitorID)
            .mapTo<PlansOutput>()
            .toList()
    }

    override fun getPlanOfClientContainingDate(clientID: UUID, date: LocalDate): PlanOutput? {
        val planId = handle.createQuery(
            """
                select distinct p.id
                from dbo.plans p
                inner join dbo.client_plans cp on p.id = cp.plan_id
                inner join dbo.daily_lists dl on dl.plan_id = p.id
                where cp.client_id = :clientID and :date >= cp.dt_start
                and :date <= (cp.dt_start + dl.index * interval '1 day')
            """.trimIndent()
        )
            .bind("clientID", clientID)
            .bind("date", date)
            .mapTo<Int>()
            .singleOrNull() ?: return null

        return getPlan(planId)
    }

    override fun checkIfPlanIsOfMonitor(monitorID: UUID, planID: Int): Boolean =
        handle.createQuery("select count(*) from dbo.plans where id = :planID and monitor_id = :monitorID")
            .bind("planID", planID)
            .bind("monitorID", monitorID)
            .mapTo<Int>()
            .single() == 1

    override fun checkIfExistsPlanOfClientInThisPeriod(clientID: UUID, startDate: LocalDate, endDate: LocalDate): Boolean {
        return handle.createQuery(
            """
               select exists (
               select 1
               from dbo.plans p inner join dbo.client_plans cp on p.id = cp.plan_id
               where cp.client_id = :clientID
                   and
                     (((:startDate between cp.dt_start and (cp.dt_start + (select max(dl.index) from dbo.daily_lists dl where dl.plan_id = p.id)))
                       or (:endDate between cp.dt_start and (cp.dt_start + (select max(dl.index) from dbo.daily_lists dl where dl.plan_id = p.id))))
                  or (cp.dt_start between :startDate and :endDate
                   and (cp.dt_start + (select max(dl.index) from dbo.daily_lists dl where dl.plan_id = p.id)) between :startDate and :endDate)
           ))
            """.trimIndent()
        )
            .bind("clientID", clientID)
            .bind("startDate", startDate)
            .bind("endDate", endDate)
            .mapTo<Boolean>()
            .single()
    }

    override fun checkIfMonitorHasPrescribedExercise(exerciseID: Int, monitorID: UUID) : Boolean{
         return handle.createQuery(
                                "select count(*) from dbo.daily_exercises de " +
                                    "inner join dbo.daily_lists dl on de.daily_list_id = dl.id " +
                                    "inner join dbo.plans p on dl.plan_id = p.id " +
                                    "where de.ex_id = :exerciseID and p.monitor_id = :monitorID"
         )
             .bind("exerciseID",exerciseID)
             .bind("monitorID",monitorID)
             .mapTo<Int>()
             .single() == 1

    }

    override fun checkIfClientAlreadyUploadedVideo(exerciseID: Int) : Boolean{
        return handle.createQuery("select count(*) from dbo.exercises_video ev " +
                                      "inner join dbo.daily_exercises de on ev.ex_id = de.id " +
                                      "where de.ex_id = :exerciseID ")
            .bind("exerciseID",exerciseID)
            .mapTo<Int>()
            .single() == 1
    }

    override fun giveFeedBackOfVideo(exerciseID: Int, feedback: String){
        handle.createUpdate("update dbo.exercises_video set monitor_feedback = :feedback where id = :exerciseID")
              .bind("feedback",feedback)
              .bind("exerciseID",exerciseID)
    }
}
