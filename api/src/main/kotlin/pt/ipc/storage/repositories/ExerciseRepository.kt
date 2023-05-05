package pt.ipc.storage.repositories

import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import java.util.UUID

interface ExerciseRepository {

    fun getExercise(exerciseID : UUID) : ExerciseInfo?

    fun getExercises() : List<ExerciseInfo>

    fun getExerciseByType(type : ExerciseType) : List<ExerciseInfo>

}