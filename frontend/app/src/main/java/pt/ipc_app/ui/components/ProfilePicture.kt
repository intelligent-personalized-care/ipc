package pt.ipc_app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pt.ipc_app.R

private const val IMAGE_SIZE = 110

@Composable
fun ProfilePicture(
    imageRequest: ImageRequest
) {
    var pictureLoading by remember { mutableStateOf(false) }
    var pictureNotFound by remember { mutableStateOf(false) }
    Row {

        if (!pictureNotFound)
            AsyncImage(
                model = imageRequest,
                contentDescription = "Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(IMAGE_SIZE.dp),
                onLoading = { pictureLoading = true },
                onSuccess = { pictureLoading = false },
                onError = {
                    pictureNotFound = true
                    pictureLoading = false
                }
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
        if (pictureLoading)
            CircularProgressIndicator()
    }
}