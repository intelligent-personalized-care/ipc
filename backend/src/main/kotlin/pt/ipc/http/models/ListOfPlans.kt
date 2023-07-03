package pt.ipc.http.models

import org.jdbi.v3.core.mapper.reflect.ColumnName

data class ListOfPlans(val plans: List<PlanInfoOutput>)

data class PlanInfoOutput(
    @ColumnName("id")
    val id: Int,
    val title: String,
    val days: Int
)
