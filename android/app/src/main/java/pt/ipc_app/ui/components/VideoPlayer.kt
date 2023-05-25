package pt.ipc_app.ui.components

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun VideoPlayer(url: String) {
    val context = LocalContext.current

    val exoPlayer = ExoPlayer.Builder(context).build()
    val mediaItem = MediaItem.fromUri(Uri.parse(url))
    exoPlayer.setMediaItem(mediaItem)


    val playerView = StyledPlayerView(context)
    playerView.player = exoPlayer

    DisposableEffect(AndroidView(factory = {playerView})){

        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        onDispose {
            exoPlayer.release()
        }
    }
}