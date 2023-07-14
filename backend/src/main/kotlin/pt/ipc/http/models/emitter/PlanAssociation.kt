package pt.ipc.http.models.emitter

import pt.ipc.domain.plan.PlanOutput

data class PlanAssociation(val planOutput : PlanOutput) :
    EmitterModel(
        eventID = "PlanAssociation",
        obj = planOutput
    )
