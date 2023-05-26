package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.Exercise
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.storage.repositories.ExerciseRepository
import java.time.LocalDate
import java.util.*

class JdbiExercisesRepository(
    private val handle: Handle
) : ExerciseRepository {

    override fun getExercise(exerciseID: UUID): ExerciseInfo? {
        return handle.createQuery("select * from dbo.exercises_info where id = :exerciseID")
            .bind("exerciseID", exerciseID)
            .mapTo<ExerciseInfo>()
            .singleOrNull()
    }

    override fun getExercises(): List<ExerciseInfo> {
        return handle.createQuery("select * from dbo.exercises_info")
            .mapTo<ExerciseInfo>()
            .toList()
    }

    override fun getExerciseByType(type: ExerciseType): List<ExerciseInfo> {
        return handle.createQuery("select * from dbo.exercises_info where type = :type")
            .bind("type", type)
            .mapTo<ExerciseInfo>()
            .toList()
    }

    override fun getAllExercisesOfClient(clientID: UUID): List<Exercise> {
        val sql = """
        SELECT de.ex_id, de.sets, de.reps
        FROM dbo.daily_exercises de
        JOIN dbo.daily_lists dl ON de.daily_list_id = dl.id
        JOIN dbo.plans p ON dl.plan_id = p.id
        JOIN dbo.client_plans cp ON p.id = cp.plan_id
        WHERE cp.client_id = :clientID
    """
        return handle.createQuery(sql)
            .bind("clientID", clientID)
            .mapTo<Exercise>()
            .list()
    }

    override fun getExercisesOfDay(clientID: UUID, date: LocalDate): List<Exercise> {
        val sql = """
        SELECT de.ex_id, de.sets, de.reps 
        FROM dbo.daily_exercises de
        JOIN dbo.daily_lists dl ON de.daily_list_id = dl.id
        JOIN dbo.plans p ON dl.plan_id = p.id
        JOIN dbo.client_plans cp ON p.id = cp.plan_id
        WHERE cp.client_id = :clientID
        AND dl.index = :dayIndex
    """

        val dtStart = handle.createQuery("SELECT dt_start FROM dbo.client_plans WHERE client_id = :clientID")
            .bind("clientID", clientID)
            .mapTo<LocalDate>()
            .single()

        val dayIndex = java.time.temporal.ChronoUnit.DAYS.between(dtStart, date).toInt()

        return handle.createQuery(sql)
            .bind("clientID", clientID)
            .bind("dayIndex", dayIndex)
            .mapTo<Exercise>()
            .list()
    }
}
