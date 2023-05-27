package pt.ipc_app.service.models.register

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.ipc_app.domain.Plan

@Parcelize
data class PlanOutput(
    val planID: Int,
    val plan: Plan
) : Parcelable
