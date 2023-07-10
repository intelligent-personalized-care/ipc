package pt.ipc.services.exercisesService

import pt.ipc.domain.exercises.ExerciseInfo
import pt.ipc.domain.exercises.ExerciseType
import pt.ipc.domain.plan.PlanOutput
import pt.ipc.domain.plan.VideoFeedBack
import java.time.LocalDate
import java.util.UUID

interface ExercisesService {

    fun getExercisesInfo(exerciseID: UUID): ExerciseInfo

    fun getExercises(exerciseType: ExerciseType?, skip: Int, limit: Int): List<ExerciseInfo>

    fun getExercisePreviewVideo(exerciseID: UUID): ByteArray

    fun getClientVideo(clientID: UUID, /*userID: UUID,*/ planID: Int, dailyList: Int, dailyExercise: Int, set: Int): ByteArray

    fun getVideoFeedback(clientID: UUID, userID: UUID, planID: Int, dailyList: Int, dailyExercise: Int, set: Int): VideoFeedBack

    fun getPlanOfClientContainingDate(userID: UUID, clientID: UUID, date: LocalDate): PlanOutput
}
