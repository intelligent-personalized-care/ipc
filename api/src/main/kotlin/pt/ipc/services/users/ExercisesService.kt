package pt.ipc.services.users

import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.services.users.dtos.ExerciseVideo
import java.util.UUID

interface ExercisesService {

    fun getExercisesInfo(exerciseID : UUID) : ExerciseVideo

    fun getExercises(exerciseType: ExerciseType? ) : List<ExerciseInfo>

}