package pt.ipc.http.models.emitter

import java.time.LocalDate

data class PlanAssociation(val planTitle: String, val startDate: LocalDate)
    : EmitterModel(eventID = "PlanAssociation",
    obj = object {
                val planTitle = planTitle
                val startDate = startDate
            }
    )

