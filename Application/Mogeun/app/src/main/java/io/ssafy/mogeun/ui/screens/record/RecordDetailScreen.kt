package io.ssafy.mogeun.ui.screens.record

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.extensions.isNotNull
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.Exercise
import io.ssafy.mogeun.model.RoutineInfoData
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.screens.routine.searchRoutine.muscleIcon
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordDetailScreen(
    navController: NavHostController,
    viewModel: RecordViewModel = viewModel(factory = AppViewModelProvider.Factory),
    reportKey: String?
) {
    var reportKeyList: List<Int>
    try {
        reportKeyList = navController.previousBackStackEntry
            ?.savedStateHandle?.get<List<Int>>("reportKeyList")!!
    } catch (e: NullPointerException) {
        reportKeyList = emptyList()
    }

    var routineTimeList: List<String>
    try {
        routineTimeList = navController.previousBackStackEntry
            ?.savedStateHandle?.get<List<String>>("routineTimeList")!!
    } catch (e: NullPointerException) {
        routineTimeList = emptyList()
    }
    Log.d("routineTimeList", routineTimeList.toString())

    if (reportKeyList.isNotNull() && routineTimeList.isNotEmpty()) {
        val recordRoutineSuccess by viewModel.recordRoutineSuccess.collectAsState()
        if (!recordRoutineSuccess) {
            viewModel.recordAllRoutine(reportKeyList)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 10.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            val pagerState = rememberPagerState(
                initialPage = reportKeyList.indexOf(reportKey?.toInt()),
                pageCount = { reportKeyList.size }
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
                    if (recordRoutineSuccess) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = viewModel.routineInfoList[pagerState.currentPage].date,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (routineTimeList.isNotEmpty())
                                Text(routineTimeList[pagerState.currentPage])
                        }
                    }
                    Text(
                        text = ">",
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    if (pagerState.currentPage < reportKeyList.size)
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalPager(state = pagerState) { page ->
                    // Our page content
                    if (recordRoutineSuccess)
                        RecordDetail(navController, viewModel.routineInfoList[page])
                }
            }
        }
    }
    else {
        val recordRoutineSuccess by viewModel.recordRoutineSuccess.collectAsState()
        if (!recordRoutineSuccess) {
            viewModel.recordRoutine(reportKey!!)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 10.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            if (recordRoutineSuccess)
                RecordDetail(navController, viewModel.routineInfoList[0])
        }
    }
}

@Composable
fun RecordDetail(
    navController: NavHostController,
    routineInfo: RoutineInfoData
) {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        if (routineInfo != null) {
            RoutineInfoCard(routineInfo.name, routineInfo.calorie, routineInfo.totalSets, routineInfo.performTime)
        }
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { routineInfo?.let { IconCard(it.exercises) } }
            if (routineInfo != null) {
                itemsIndexed(routineInfo.exercises) {index, item ->
                    if (item.sets > 0)
                        RoutineExerciseCard(navController, item)
                }
            }
        }
    }
}

@Preview
@Composable
fun RecordDetailScreenPreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp,
                vertical = 10.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoutineInfoCard("test",1.3F, 3, 3)
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
//            item { RoutineGraphIconCard() }
            itemsIndexed(listOf(1, 2, 3)) {index, item ->
                RoutineExerciseCardPreview()
            }
        }
    }
}

@Composable
fun RoutineInfoCard(
    name: String,
    calorie: Float,
    totalSets: Int,
    performTime: Int
) {
    data class RoutineInfoData(
        val title: String,
        val detail: String
    )

    var routineInfoList: List<RoutineInfoData> =
        listOf(RoutineInfoData("소모 칼로리", calorie.toString() + "kcal"), RoutineInfoData("수행한 세트", totalSets.toString() + "set"), RoutineInfoData("운동한 시간", performTime.toString() + "분"))

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(name, fontSize = 24.sp)
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
            ) {
                routineInfoList.forEach{
                    RoutineInfo(it.title, it.detail)
                }
                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }
}

@Composable
fun RoutineInfo(
    title: String,
    detail: String
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 5.dp
            )
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f,
                )
            }
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title)
        Text(detail)
    }
}

@Composable
fun IconCard(
    exercises: List<Exercise>
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 20.dp
            )
    ) {
        var parts: List<String> = emptyList()
        for (exercise in exercises) {
            parts = parts.union(exercise.muscleImagePaths).toList()
        }

        Column {
            Text("사용근육")
            MuscleGrid(
                columns = 5,
                itemCount = parts.size,
                modifier = Modifier
                    .padding(start = 7.5.dp, end = 7.5.dp)
            ) {
                muscleIcon(parts[it])
            }
        }
    }
}

@Composable
fun RoutineExerciseCard(
    navController: NavHostController,
    exercise: Exercise
) {
    Box (
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
            .padding(
                vertical = 10.dp,
                horizontal = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (modifier = Modifier.fillMaxWidth()) {
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    val exerciseImage = LocalContext.current.resources.getIdentifier("x_" + exercise.imagePath, "drawable", LocalContext.current.packageName)

                    Image(
                        painter = painterResource(id = exerciseImage),
                        contentDescription = exercise.execName,
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = exercise.execName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                WeightGrid(
                    columns = 4,
                    itemCount = exercise.sets,
                    modifier = Modifier
                        .padding(start = 7.5.dp, end = 7.5.dp)
                ) {
                    SetWeightIcon(exercise.setResults[it].weight.toInt())
                }
            }
            ClickableText(
                modifier = Modifier.align(Alignment.End),
                text = AnnotatedString("자세히 보기"),
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("exerciseDetail", exercise)
                    navController.navigate("ExerciseDetail")
                },
                style = TextStyle(color = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

@Preview
@Composable
fun RoutineExerciseCardPreview() {
    Box (
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
            .padding(
                vertical = 10.dp,
                horizontal = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (modifier = Modifier.fillMaxWidth()) {
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Column (
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
//                    val exerciseImage = LocalContext.current.resources.getIdentifier("x_" + exercise.imagePath, "drawable", LocalContext.current.packageName)

                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "test",
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "test",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                WeightGrid(
                    columns = 4,
                    itemCount = 43,
                    modifier = Modifier
                        .padding(start = 7.5.dp, end = 7.5.dp)
                ) {
                    SetWeightIcon(55)
                }
            }
            ClickableText(
                modifier = Modifier.align(Alignment.End),
                text = AnnotatedString("자세히 보기"),
                onClick = {

                },
                style = TextStyle(color = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

@Composable
fun SetWeightIcon(weight: Int) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .padding(6.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = weight.toString(),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun WeightGrid(
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

@Composable
fun MuscleGrid(
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
