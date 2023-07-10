package pt.ipc.domain.exercises

import com.fasterxml.jackson.annotation.JsonInclude
import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.*

data class Exercise(
    @ColumnName("ex_id")
    val exerciseInfoID: UUID,
    val sets: Int,
    val reps: Int
)

data class ExerciseTotalInfo(
    val planId: Int,
    val dailyListId: Int,
    val exercise: DailyExercise
)

data class DailyExercise(
    val id: Int,
    @ColumnName("ex_id")
    val exerciseInfoID: UUID,
    val title: String,
    val description: String,
    val type: String,
    val sets: Int,
    val reps: Int,
    @JsonInclude(JsonInclude.Include.NON_NULL) val isDone: Boolean? = null
)
