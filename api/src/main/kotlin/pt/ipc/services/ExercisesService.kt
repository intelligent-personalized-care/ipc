package pt.ipc.services

import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.services.dtos.ExerciseVideo
import java.util.UUID

interface ExercisesService {

    fun getExercisesInfo(exerciseID: UUID): ExerciseVideo

    fun getExercises(exerciseType: ExerciseType?, skip : Int, limit : Int): List<ExerciseInfo>
}
