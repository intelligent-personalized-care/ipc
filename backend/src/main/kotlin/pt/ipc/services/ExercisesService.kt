package pt.ipc.services

import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import java.util.UUID

interface ExercisesService {

    fun getExercisesInfo(exerciseID: UUID): ExerciseInfo

    fun getExercises(exerciseType: ExerciseType?, skip: Int, limit: Int): List<ExerciseInfo>

    fun getExerciseVideo(exerciseID: UUID): ByteArray
}
