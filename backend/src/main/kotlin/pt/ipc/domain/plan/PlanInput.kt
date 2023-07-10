package pt.ipc.domain.plan

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDate

data class PlanInput(
    val title: String,
    val dailyLists: List<DailyListInput?>
)

data class PlanOutput(
    val id: Int,
    val title: String,
    @JsonInclude(JsonInclude.Include.NON_NULL) val startDate: LocalDate? = null,
    val dailyLists: List<DailyListOutput?>
)
