package pt.ipc_app.ui.screens.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import pt.ipc_app.DependenciesContainer
import pt.ipc_app.domain.Plan
import pt.ipc_app.service.models.users.MonitorOutput
import pt.ipc_app.ui.screens.about.AboutActivity
import pt.ipc_app.ui.screens.exercises.info.ExerciseActivity
import pt.ipc_app.ui.screens.profile.ClientProfileActivity
import pt.ipc_app.ui.screens.search.SearchMonitorsActivity
import pt.ipc_app.ui.screens.details.MonitorDetailsActivity
import pt.ipc_app.ui.screens.exercises.list.ExercisesListActivity
import java.util.*

/**
 * The client home activity.
 */
class ClientHomeActivity : ComponentActivity() {

    private val repo by lazy {
        (application as DependenciesContainer).sessionManager
    }

    companion object {
        const val MONITOR = "MONITOR"
        const val PLAN = "PLAN"
        fun navigate(context: Context, monitor: MonitorOutput? = null, plan: Plan? = null) {
            with(context) {
                val intent = Intent(this, ClientHomeActivity::class.java)
                intent.putExtra(MONITOR, monitor)
                intent.putExtra(PLAN, plan)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mon = monitor
            ClientHomeScreen(
                client = repo.userLoggedIn,
                monitor = mon,
                //planTest = plan,
                onMonitorClick = {
                    if (mon != null)
                        MonitorDetailsActivity.navigate(this, mon)
                    else
                        SearchMonitorsActivity.navigate(this)
                },
                onExerciseSelect = { ExerciseActivity.navigate(this, it) },
                onExercisesClick = { ExercisesListActivity.navigate(this) },
                onUserInfoClick = { ClientProfileActivity.navigate(this) },
                onAboutClick = { AboutActivity.navigate(this) }
            )
        }
    }

    @Suppress("deprecation")
    private val monitor: MonitorOutput? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(MONITOR, MonitorOutput::class.java)
        else
            intent.getParcelableExtra(MONITOR)
    }

    @Suppress("deprecation")
    private val plan: Plan? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(PLAN, Plan::class.java)
        else
            intent.getParcelableExtra(PLAN)
    }
}