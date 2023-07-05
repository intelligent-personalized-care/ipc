package pt.ipc_app.ui.screens.about

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.ipc_app.R
import pt.ipc_app.ui.components.AuthorInfo
import pt.ipc_app.ui.components.AuthorInfoView

/**
 * About screen.
 *
 * Information shown for each author:
 * - First and last name
 * - Personal github profile link
 * - Email contact
 *
 * Also shows the github link of the app's repository.
 *
 * @param onOpenUrl callback to be invoked when a link is clicked
 * @param onSendEmail callback to be invoked when an email is clicked
 */
@Composable
fun AboutScreen(
    onOpenUrl: (Uri) -> Unit = { },
    onSendEmail: (String) -> Unit = { }
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.about_title),
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(top = 60.dp, bottom = 20.dp)
        )

        authors.forEach { author ->
            AuthorInfoView(
                author = author,
                onSendEmail = onSendEmail,
                onOpenUrl = onOpenUrl
            )
        }

        Text(text = stringResource(R.string.about_repo_github))
        Image(
            painter = painterResource(R.drawable.ic_github_dark),
            contentDescription = "Github Logo",
            modifier = Modifier
                .clickable { onOpenUrl(githubRepoUrl) }
                .padding(8.dp)
        )
    }
}

private val githubRepoUrl = Uri.parse("https://github.com/intelligent-personalized-care/ipc")

private val authors = listOf(
    AuthorInfo(
        name = "Guilherme Cepeda",
        githubLink = Uri.parse("https://github.com/bodeborder"),
        email = "47531@alunos.isel.pt"
    ),
    AuthorInfo(
        name = "Rodrigo Neves",
        githubLink = Uri.parse("https://github.com/RodrigoNevesWork"),
        email = "46536@alunos.isel.pt"
    ),
    AuthorInfo(
        name = "Tiago Martinho",
        githubLink = Uri.parse("https://github.com/tiagomartinhoo"),
        email = "48256@alunos.isel.pt"
    )
)

@Preview
@Composable
private fun AboutScreenPreview() {
    AboutScreen()
}
