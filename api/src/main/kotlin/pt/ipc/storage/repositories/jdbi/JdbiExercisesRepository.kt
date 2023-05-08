package pt.ipc.storage.repositories.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.storage.repositories.ExerciseRepository
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
}
