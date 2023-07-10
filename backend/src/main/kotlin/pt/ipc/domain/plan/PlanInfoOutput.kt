package pt.ipc.domain.plan

import org.jdbi.v3.core.mapper.reflect.ColumnName

data class PlanInfoOutput(
    @ColumnName("id")
    val id: Int,
    val title: String,
    val days: Int
)