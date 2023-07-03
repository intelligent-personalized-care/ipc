package pt.ipc_app.service.models.plans

data class ListOfPlans(val plans: List<PlanInfoOutput>)

data class PlanInfoOutput(
    val id: Int,
    val title: String,
    val days: Int
)