package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.Exercise
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.storage.repositories.ExerciseRepository
import java.time.Duration
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

    override fun getExercises(skip: Int, limit: Int): List<ExerciseInfo> {
        return handle.createQuery("select * from dbo.exercises_info offset :skip limit :limit")
            .bind("skip", skip)
            .bind("limit", limit)
            .mapTo<ExerciseInfo>()
            .toList()
    }

    override fun getExerciseByType(type: ExerciseType, skip: Int, limit: Int): List<ExerciseInfo> {
        return handle.createQuery("select * from dbo.exercises_info where type = :type offset :skip limit :limit")
            .bind("type", type)
            .bind("skip", skip)
            .bind("limit", limit)
            .mapTo<ExerciseInfo>()
            .toList()
    }

    override fun getAllExercisesOfClient(clientID: UUID, skip: Int, limit: Int): List<Exercise> {
        val sql = """
        SELECT de.ex_id, de.sets, de.reps
        FROM dbo.daily_exercises de
        JOIN dbo.daily_lists dl ON de.daily_list_id = dl.id
        JOIN dbo.plans p ON dl.plan_id = p.id
        JOIN dbo.client_plans cp ON p.id = cp.plan_id
        WHERE cp.client_id = :clientID
        offset :skip 
        limit :limit
    """
        return handle.createQuery(sql)
            .bind("clientID", clientID)
            .bind("skip", skip)
            .bind("limit", limit)
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
        offset :skip 
        limit :limit
    """

        val dtStart = handle.createQuery("SELECT dt_start FROM dbo.client_plans WHERE client_id = :clientID")
            .bind("clientID", clientID)
            .mapTo<LocalDate>()
            .single()

        val dayIndex = Duration.between(dtStart.atStartOfDay(), date.atStartOfDay()).toDays().toInt()

        return handle.createQuery(sql)
            .bind("clientID", clientID)
            .bind("dayIndex", dayIndex)
            .mapTo<Exercise>()
            .list()
    }

    override fun addExerciseInfoPreview(exerciseID: UUID, title: String, description: String, type: ExerciseType) {
        handle.createUpdate("insert into dbo.exercises_info(id, title, description, type) values(:id,:title,:description,:type)")
            .bind("id",exerciseID)
            .bind("title",title)
            .bind("description",description)
            .bind("type",type)
            .execute()
    }
}
