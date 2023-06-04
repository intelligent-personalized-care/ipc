package pt.ipc_app.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

@Composable
fun VideoPlayer(
    url: String,
    playing: Boolean
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    val mediaItem = remember {
        MediaItem.fromUri(Uri.parse(url))
    }
    exoPlayer.setMediaItem(mediaItem)

    val playerView = remember {
        StyledPlayerView(context).apply {
            player = exoPlayer
        }
    }

    DisposableEffect(Unit) {
        exoPlayer.prepare()
        exoPlayer.playWhenReady = playing

        onDispose {
            if (!playing) exoPlayer.stop()
            else exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        AndroidView(
            factory = { playerView },
            modifier = Modifier.matchParentSize()
        )
    }
}
