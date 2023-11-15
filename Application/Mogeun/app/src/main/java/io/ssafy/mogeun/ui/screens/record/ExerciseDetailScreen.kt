package io.ssafy.mogeun.ui.screens.record

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.jaikeerthick.composable_graphs.color.LinearGraphColors
import com.jaikeerthick.composable_graphs.composables.LineGraph
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.style.LabelPosition
import com.jaikeerthick.composable_graphs.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.style.LinearGraphVisibility
import io.ssafy.mogeun.model.Exercise
import io.ssafy.mogeun.model.SetResult
import kotlinx.coroutines.launch

data class MuscleFatigue(
    val set: String,
    val num: Float
)

@Composable
fun ExerciseDetailScreen(navController: NavHostController) {
    var exercise: Exercise
    try {
        exercise = navController.previousBackStackEntry
            ?.savedStateHandle?.get<Exercise>("exerciseDetail")!!
    } catch (e: NullPointerException) {
        exercise = Exercise("", "", 0, listOf(""), listOf(""), listOf(SetResult(0, 0f, 0, 0, listOf(0f), listOf(0f))))
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp,
                vertical = 10.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val exerciseImage = LocalContext.current.resources.getIdentifier("z_" + exercise.imagePath, "drawable", LocalContext.current.packageName)
        var leftMuscleFatigueList: MutableList<MuscleFatigue> = mutableListOf()
        var rightMuscleFatigueList: MutableList<MuscleFatigue> = mutableListOf()
        var muscleFatigueList: List<MutableList<MuscleFatigue>> = mutableListOf()

        var set = 1
        for (setResult in exercise.setResults) {
            if (setResult.muscleFatigue!!.size > 1) {
                Log.d("setResult.muscleFatigue", setResult.muscleFatigue.toString())
                leftMuscleFatigueList.add(
                    MuscleFatigue(
                        set.toString() + "set",
                        setResult.muscleFatigue[0]
                    )
                )
                rightMuscleFatigueList.add(
                    MuscleFatigue(
                        set.toString() + "set",
                        setResult.muscleFatigue[1]
                    )
                )
            }
        }
        if (!leftMuscleFatigueList.isNullOrEmpty() || !rightMuscleFatigueList.isNullOrEmpty())
            muscleFatigueList = listOf(leftMuscleFatigueList, rightMuscleFatigueList)
        Log.d("muscleFatigueList", muscleFatigueList.toString())

        var expanded by remember { mutableStateOf(false) }


        Text(exercise.execName, modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        GifImage(modifier = Modifier.fillMaxWidth(), imageId = exerciseImage)
        if (!expanded) {
            ClickableText(
                text = AnnotatedString("피로도 그래프 보기"),
                onClick = {
                    expanded = !expanded
                },
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        else {
            ClickableText(
                text = AnnotatedString("피로도 그래프 닫기"),
                onClick = {
                    expanded = !expanded
                },
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (!muscleFatigueList.isNullOrEmpty() && expanded) {
                MuscleFatigueCard(muscleFatigueList, exercise.parts)
            }
            repeat(exercise.sets) {
                SetDetail(it + 1, exercise.setResults[it], exercise.parts)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MuscleFatigueCard(
    muscleFatigueList: List<MutableList<MuscleFatigue>>,
    parts: List<String>
) {
    val pagerState = rememberPagerState(pageCount = {
        2
    })
    val nameList = listOf("왼쪽 " + parts[0].split(" ")[1], "오른쪽 " + parts[0].split(" ")[1])

    Column {
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val coroutineScope = rememberCoroutineScope()

            Text(
                text = "<",
                modifier = Modifier
                    .clickable { coroutineScope.launch {
                        // Call scroll to on pagerState
                        pagerState.animateScrollToPage(0)
                    } }
            )
            Text(nameList[pagerState.currentPage])
            Text(
                text = ">",
                modifier = Modifier
                    .clickable { coroutineScope.launch {
                        // Call scroll to on pagerState
                        pagerState.animateScrollToPage(1)
                    } }
            )
        }
        HorizontalPager(state = pagerState) { page ->
            // Our page content
            MuscleFatigueChart(muscleFatigueList[page])
        }
    }
}

@Composable
fun MuscleFatigueChart(
    muscleFatigueList: MutableList<MuscleFatigue>
) {
    if (muscleFatigueList.isNullOrEmpty()) {
        Column(Modifier.fillMaxWidth()){
            Text("운동 기록이 없습니다.", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
    else {
        Column(modifier = Modifier.fillMaxWidth()) {
            val style = LineGraphStyle(
                paddingValues = PaddingValues(5.dp),
                visibility = LinearGraphVisibility(
                    isHeaderVisible = true,
                    isXAxisLabelVisible = true,
                    isYAxisLabelVisible = true,
                    isCrossHairVisible = false
                ),
                colors = LinearGraphColors(
                    lineColor = MaterialTheme.colorScheme.primary,
                    pointColor = MaterialTheme.colorScheme.primary,
                    clickHighlightColor = MaterialTheme.colorScheme.inversePrimary,
                    fillGradient = null
                ),
                height = 200.dp,
                yAxisLabelPosition = LabelPosition.LEFT
            )

            if (muscleFatigueList.size == 1) {
                val xAxisDataList = listOf(GraphData.String(""), GraphData.String("1세트"))
                val yAxisDataList = listOf(0f, muscleFatigueList[0].num)
                LineGraph(
                    xAxisData = xAxisDataList,
                    yAxisData = yAxisDataList,
                    style = style
                )
            }
            else {
                LineGraph(
                    xAxisData = muscleFatigueList.map {
                        GraphData.String(it.set)
                    },
                    yAxisData = muscleFatigueList.map {
                        it.num
                    },
                    style = style
                )
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
    setDetail: SetResult,
    parts: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    val part = parts.get(0).split(" ")[1]

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
        if (expanded && !setDetail.muscleActivity.isNullOrEmpty() && setDetail.muscleActivity?.get(0) != 0.0f && setDetail.muscleActivity?.get(1) != 0.0f)
            MuscleActivity(setDetail.muscleActivity, part)
    }
}

@Composable
fun MuscleActivity(
    muscleActivityList: List<Float>?,
    part: String
) {
    val left = muscleActivityList!![0]
    val right = muscleActivityList[1]
    var balanceValue = try {
        left / (left + right)
    } catch(e: IllegalArgumentException) {
        0f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 30.dp,
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BalanceBar(balanceValue)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("왼쪽 $part")
                    if (muscleActivityList?.size!! > 0)
                        Text("근활성도: " + muscleActivityList[0].toString())
                }
                Column {
                    Text("오른쪽 $part")
                    if (muscleActivityList?.size!! > 0)
                        Text("근활성도: " + muscleActivityList[1].toString())
                }
            }
        }
    }

}

@Composable
fun BalanceBar(balanceValue: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(balanceValue)
                .background(
                    color = when (balanceValue) {
                        in 0.45f..0.55f -> MaterialTheme.colorScheme.tertiary
                        in 0.3f..0.7f -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.error
                    }
                )
        ) {
            Text("")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = when (balanceValue) {
                        in 0.45f..0.55f -> MaterialTheme.colorScheme.tertiaryContainer
                        in 0.3f..0.7f -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    }
                )
        ) {
            Text("")
        }
    }
}