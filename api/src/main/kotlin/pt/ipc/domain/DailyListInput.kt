package pt.ipc.domain

data class DailyListInput(val exercises: List<Exercise>)

data class DailyListOutput(val exercises: List<ExerciseTotalInfo>)
