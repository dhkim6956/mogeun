package io.ssafy.mogeun.ui.screens.routine.execution.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ExecutionGif(imgPath: String) {
    val context = LocalContext.current
    val gifResId = context.resources.getIdentifier("z_${imgPath}", "drawable", context.packageName)
    Box(
        modifier = Modifier
//            .clip(RoundedCornerShape(20.dp))
//            .shadow(12.dp, shape = RoundedCornerShape(20.dp))
            .width(300.dp)
            .height(200.dp)
    ) {
        GlideImage(
            imageModel = gifResId,
            contentDescription = "GIF Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}