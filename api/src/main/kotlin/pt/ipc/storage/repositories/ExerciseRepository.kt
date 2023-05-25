package pt.ipc.storage.repositories

import pt.ipc.domain.Exercise
import pt.ipc.domain.ExerciseInfo
import pt.ipc.domain.ExerciseType
import pt.ipc.domain.Plan
import pt.ipc.domain.PlanOutput
import java.time.LocalDate
import java.util.UUID

interface ExerciseRepository {

    fun getExercise(exerciseID: UUID): ExerciseInfo?

    fun getExercises(): List<ExerciseInfo>

    fun getExerciseByType(type: ExerciseType): List<ExerciseInfo>

    fun createPlan(monitorID: UUID, clientID: UUID, plan: Plan): Int

    fun getPlan(planID: Int): PlanOutput

    fun checkIfPlanIsOfMonitor(monitorID: UUID, planID: Int): Boolean

    fun getAllExercisesOfClient(clientID: UUID): List<Exercise>

    fun getExercisesOfDay(clientID: UUID, date: LocalDate): List<Exercise>
}
