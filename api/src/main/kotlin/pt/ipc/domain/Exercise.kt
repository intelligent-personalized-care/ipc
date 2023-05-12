package pt.ipc.domain

import org.jdbi.v3.core.mapper.reflect.ColumnName
import java.util.*

data class Exercise(
    @ColumnName("ex_id")
    val exerciseID: UUID,
    val sets: Int,
    val reps: Int)
