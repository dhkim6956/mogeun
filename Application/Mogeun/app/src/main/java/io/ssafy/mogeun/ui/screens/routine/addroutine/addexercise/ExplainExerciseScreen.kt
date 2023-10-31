package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import com.skydoves.landscapist.glide.GlideImage
import io.ssafy.mogeun.R


@Composable
fun ExplainExerciseScreen(navController: NavHostController, data: String?) {
    val imageResId: Int? = data?.let {
        it.split(".").lastOrNull()?.toIntOrNull()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            GlideImage(
                imageModel = imageResId,
                contentDescription = "GIF Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(350.dp).width(350.dp)
            )
        }
    }
}