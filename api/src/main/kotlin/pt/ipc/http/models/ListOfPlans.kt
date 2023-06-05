package pt.ipc.http.models

import org.jdbi.v3.core.mapper.reflect.ColumnName

data class ListOfPlans(val plans: List<PlansOutput>)

data class PlansOutput(
    @ColumnName("id")
    val planIDd: Int,
    val title: String
)
