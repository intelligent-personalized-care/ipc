package pt.ipc_app.domain.user

data class Plan(
    val id: Int,
    val title: String,
    val dailyLists: List<DailyList>
) {
    val duration = dailyLists.size
}