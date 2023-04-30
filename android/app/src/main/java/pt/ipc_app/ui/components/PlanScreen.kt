package pt.ipc_app.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import pt.ipc_app.domain.user.Plan

@Composable
fun PlanScreen(
    plan: Plan
) {

    Text(plan.title)

}