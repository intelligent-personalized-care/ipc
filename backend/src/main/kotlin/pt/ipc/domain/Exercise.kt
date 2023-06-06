package pt.ipc.domain

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.*

data class Exercise(
    @ColumnName("ex_id")
    val exerciseInfoID: UUID,
    val sets: Int,
    val reps: Int
)

data class ExerciseTotalInfo(
    val id: Int,
    @ColumnName("ex_id")
    val exerciseInfoID: UUID,
    val title: String,
    val description: String,
    val type: String,
    val sets: Int,
    val reps: Int,
    val isDone: Boolean
)
