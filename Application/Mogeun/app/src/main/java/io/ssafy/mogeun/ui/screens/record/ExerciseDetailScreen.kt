package io.ssafy.mogeun.ui.screens.record

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.Exercise
import io.ssafy.mogeun.model.SetResult

@Composable
fun ExerciseDetailScreen(navController: NavHostController) {
    var exercise: Exercise
    try {
        exercise = navController.previousBackStackEntry
            ?.savedStateHandle?.get<Exercise>("exerciseDetail")!!
    } catch (e: NullPointerException) {
        exercise = Exercise("", "", 0, listOf(""), listOf(""), listOf(SetResult(0, 0f, 0, 0, listOf(0))))
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp,
                vertical = 10.dp
            )
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val exerciseImage = LocalContext.current.resources.getIdentifier("z_" + exercise.imagePath, "drawable", LocalContext.current.packageName)
        Box (modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(
                vertical = 10.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(exercise.execName, fontWeight = FontWeight.Bold)
        }
        GifImage(modifier = Modifier.fillMaxWidth(), imageId = exerciseImage)
//        Text("test")
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            repeat(exercise.sets) {
                SetDetail(it + 1, exercise.setResults[it])
            }
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    imageId: Int
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = imageId).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
fun SetDetail(
    setNum: Int,
    setDetail: SetResult
) {
    var expanded by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Row (
            modifier = Modifier
                .clickable { expanded = !expanded },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(90.dp, 0.dp, 0.dp, 90.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(90.dp, 0.dp, 0.dp, 90.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(setNum.toString() + "set")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Black
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(setDetail.weight.toInt().toString() + "kg")
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(0.dp, 90.dp, 90.dp, 0.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(0.dp, 90.dp, 90.dp, 0.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(setDetail.successRep.toString() + '/' + setDetail.targetRep.toString() + "rep")
            }
        }
        if (expanded)
            MuscleActivity(setDetail.muscleActivity)
    }
}

@Composable
fun MuscleActivity(
    muscleActivityList: List<Int>?
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 30.dp,
                vertical = 10.dp
            )
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        MuscleActivityGrid(
            columns = 2,
            itemCount = 4
        ) {
            SetWeightIcon(muscleActivityList?.get(it) ?: 0)
        }
    }
}

@Composable
fun MuscleActivityGrid(
    columns: Int,
    itemCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable() (Int) -> Unit
) {
    Column(modifier = modifier) {
        var rows = (itemCount / columns)
        if (itemCount.mod(columns) > 0) {
            rows += 1
        }

        for (rowId in 0 until rows) {
            val firstIndex = rowId * columns

            Row {
                for (columnId in 0 until columns) {
                    val index = firstIndex + columnId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        if (index < itemCount) {
                            content(index)
                        }
                    }
                }
            }
        }
    }
}