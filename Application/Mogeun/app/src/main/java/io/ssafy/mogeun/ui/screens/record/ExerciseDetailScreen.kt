package io.ssafy.mogeun.ui.screens.record

import android.os.Build.VERSION.SDK_INT
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.Exercise
import io.ssafy.mogeun.model.SetResult
import io.ssafy.mogeun.ui.AppViewModelProvider
import kotlinx.coroutines.launch

data class MuscleFatigue(
    val set: String,
    val num: Float
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseDetailScreen(
    navController: NavHostController,
    index: Int?
) {
    var exercises: List<Exercise>
    try {
        exercises = navController.previousBackStackEntry
            ?.savedStateHandle?.get<List<Exercise>>("exercises")!!
    } catch (e: NullPointerException) {
        exercises = emptyList()
    }

    val exerciseIndex = index!!

    if (!exercises.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 10.dp)
        ) {
            val pagerState = rememberPagerState(
                initialPage = exerciseIndex,
                pageCount = { exercises.size }
            )

            Column {
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val coroutineScope = rememberCoroutineScope()

                    Text(
                        text = "<",
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    if (pagerState.currentPage > 0)
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        exercises[pagerState.currentPage].execName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = ">",
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    if (pagerState.currentPage < exercises.size)
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalPager(state = pagerState) { page ->
                    // Our page content
                    ExerciseDetail(exercises[page])
                }
            }
        }
    }
}

@Composable
fun ExerciseDetail(
    exercise: Exercise,
    viewModel: RecordViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val exerciseImage = LocalContext.current.resources.getIdentifier(
            "z_" + exercise.imagePath,
            "drawable",
            LocalContext.current.packageName
        )
        var leftMuscleFatigueList: MutableList<MuscleFatigue> = mutableListOf()
        var rightMuscleFatigueList: MutableList<MuscleFatigue> = mutableListOf()
        var muscleFatigueList: List<MutableList<MuscleFatigue>> = mutableListOf()
        var set = 1
        for (setResult in exercise.setResults) {
            if (setResult.muscleFatigue!!.size > 1) {
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

        GifImage(modifier = Modifier.fillMaxWidth(), imageId = exerciseImage)
        ExerciseDropdown(viewModel)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),) {
            if (viewModel.itemIndex.value == 1) {
                if (!muscleFatigueList.isNullOrEmpty())
                    MuscleFatigueCard(muscleFatigueList, exercise.parts)
                else {
                    Text("No Data")
                }
            }
            else {
                repeat(exercise.sets) {
                    SetDetail(it + 1, exercise.setResults[it], exercise.muscleImagePaths)
                }
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

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(5.dp)
    ) {
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
    muscleImagePaths: List<String>
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
        if (expanded && !setDetail.muscleActivity.isNullOrEmpty() && (setDetail.muscleActivity?.get(0) != 0.0f || setDetail.muscleActivity?.get(1) != 0.0f))
            MuscleActivity(setDetail.muscleActivity, muscleImagePaths[0])
    }
}

@Composable
fun MuscleActivity(
    muscleActivityList: List<Float>?,
    muscleImagePath: String
) {
    val left = muscleActivityList!![0]
    val right = muscleActivityList[1]
    var balanceValue = try {
        left / (left + right)
    } catch(e: IllegalArgumentException) {
        0f
    }

    val leftMuscleImage = LocalContext.current.resources.getIdentifier(muscleImagePath + "_l", "drawable", LocalContext.current.packageName)
    val rightMuscleImage = LocalContext.current.resources.getIdentifier(muscleImagePath + "_r", "drawable", LocalContext.current.packageName)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Image(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        painter = painterResource(leftMuscleImage),
                        contentDescription = muscleImagePath
                    )
                }
                BalanceBar(balanceValue)
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Image(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        painter = painterResource(rightMuscleImage),
                        contentDescription = muscleImagePath
                    )
                }
            }
        }
    }

}

@Composable
fun BalanceBar(balanceValue: Float) {
    Row(
        modifier = Modifier
            .width(150.dp)
            .height(30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(balanceValue)
                .height(30.dp)
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
        Spacer(
            modifier = Modifier
                .width(2.dp)
                .height(30.dp)
                .background(color = Color.Black)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
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

@Composable
fun ExerciseDropdown(viewModel: RecordViewModel) {
    val listItems = arrayOf("세트 정보", "피로도 그래프")

    // state of the menu
    var expanded by remember { mutableStateOf(false) }
    var disabledItem by remember { mutableIntStateOf(0) }
    var item by remember { mutableStateOf(listItems[0]) }
    var itemIconIndex by remember { mutableIntStateOf(0) }
    var itemIconList = listOf(R.drawable.baseline_arrow_drop_down_24, R.drawable.baseline_arrow_drop_up_24)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(5.dp)
            .clickable {
                expanded = true
                itemIconIndex = if (itemIconIndex == 0) 1
                else 0
            }
    ) {
        Row {
            Text(item)
            Spacer(
                modifier = Modifier
                    .width(5.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
            Image(
                painter = painterResource(id = itemIconList[itemIconIndex]),
                contentDescription = "dropdown",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(20.dp)
            )
        }

        // drop down menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                itemIconIndex = if (itemIconIndex == 0) 1
                else 0
            }
        ) {
            // adding items
            listItems.forEachIndexed { itemIndex, itemValue ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        disabledItem = itemIndex
                        item = itemValue
                        viewModel.itemIndex.value = itemIndex
                        itemIconIndex = if (itemIconIndex == 0) 1
                        else 0
                    },
                    enabled = (itemIndex != disabledItem),
                    text = { Text(text = itemValue) }
                )
            }
        }
    }
}