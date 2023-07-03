package pt.ipc_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pt.ipc_app.R

private const val IMAGE_SIZE = 110

@Composable
fun ProfilePicture(
    url: String
) {
    var pictureNotFound by remember { mutableStateOf(false) }
    if (!pictureNotFound)
        AsyncImage(
            model = url,
            contentDescription = "Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(IMAGE_SIZE.dp),
            onError = { pictureNotFound = true }
        )
    else
        Image(
            painter = painterResource(R.drawable.default_profile_picture),
            contentDescription = "Picture default",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(IMAGE_SIZE.dp)
        )
}