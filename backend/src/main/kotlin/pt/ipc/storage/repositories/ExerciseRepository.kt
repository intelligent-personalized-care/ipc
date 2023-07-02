package pt.ipc.storage.repositories

import pt.ipc.domain.Exercise
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.http.models.VideoFeedBack
import java.time.LocalDate
import java.util.UUID

interface ExerciseRepository {

    fun getExercise(exerciseID: UUID): ExerciseInfo?

    fun getExercises(skip: Int, limit: Int): List<ExerciseInfo>

    fun getExerciseByType(type: ExerciseType, skip: Int, limit: Int): List<ExerciseInfo>

    fun getAllExercisesOfClient(clientID: UUID, skip: Int, limit: Int): List<Exercise>

    fun getExercisesOfDay(clientID: UUID, date: LocalDate): List<Exercise>

    fun addExerciseInfoPreview(exerciseID: UUID, title: String, description: String, type: ExerciseType)

    fun getClientVideoID(clientID: UUID, planID: Int, dailyListID: Int, dailyExerciseID: Int, set: Int): UUID?

    fun getVideoFeedback(videoID: UUID): VideoFeedBack
}