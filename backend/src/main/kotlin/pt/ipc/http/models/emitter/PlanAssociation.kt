package pt.ipc.http.models.emitter

import java.time.LocalDate

data class PlanAssociation(val title: String, val startDate: LocalDate) : EmitterModel()
