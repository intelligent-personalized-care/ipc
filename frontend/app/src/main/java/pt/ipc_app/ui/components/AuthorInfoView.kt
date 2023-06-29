package pt.ipc_app.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pt.ipc_app.R

private const val DEV_INFO_PADDING = 16
private const val DEV_INFO_MAX_WIDTH_FACTOR = 0.8f
private const val DEV_INFO_CORNER_RADIUS = 8
private const val DEV_INFO_HEIGHT = 140

/**
 * Shows the information of a specific author.
 * Since the email contacts are the ones from our college, ISEL, the email addresses follow a
 * specific format that only depends on the student number.
 */
@Composable
fun AuthorInfoView(
    author: AuthorInfo,
    onSendEmail: (String) -> Unit,
    onOpenUrl: (Uri) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(DEV_INFO_MAX_WIDTH_FACTOR),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(DEV_INFO_PADDING.dp)
                .fillMaxWidth()
                .height(DEV_INFO_HEIGHT.dp)
                .clip(RoundedCornerShape(DEV_INFO_CORNER_RADIUS.dp))
                .background(Color.LightGray)
        ) {
            Text(
                text = author.name,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center
            )

            Row {
                Image(
                    painter = painterResource(R.drawable.ic_github_dark),
                    contentDescription = "Github",
                    modifier = Modifier
                        .clickable { onOpenUrl(author.githubLink) }
                        .padding(8.dp)
                )

                Image(
                    painter = painterResource(R.drawable.ic_email),
                    contentDescription = "Email",
                    modifier = Modifier
                        .clickable { onSendEmail(author.email) }
                        .padding(8.dp)
                )
            }
        }
    }
}
