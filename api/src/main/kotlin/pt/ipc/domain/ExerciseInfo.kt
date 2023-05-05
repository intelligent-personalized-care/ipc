package pt.ipc.domain

import java.util.*

data class ExerciseInfo(val id : UUID, val title : String, val description : String, val type : ExerciseType)

enum class ExerciseType{
    Legs,
    Byceps,
    Shoulders,
    Back,
    Gluts,
    Abdominals
}