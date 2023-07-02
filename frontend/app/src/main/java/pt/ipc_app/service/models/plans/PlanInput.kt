package pt.ipc_app.service.models.plans

import pt.ipc_app.service.models.exercises.ExerciseInput
import pt.ipc_app.service.models.dailyList.DailyListInput

data class PlanInput(
    val title: String,
    val dailyLists: List<DailyListInput?> = listOf()
) {
    fun addDailyList(dailyList: DailyListInput) =
        copy(
            dailyLists = dailyLists + dailyList
        )

    fun addExerciseInDailyList(day: Int, exercise: ExerciseInput): PlanInput {
        var dayCounter = 0
        return this.copy(
            dailyLists = dailyLists.map {
                if (dayCounter++ == day) it?.addExercise(exercise)
                else it
            }
        )
    }
}