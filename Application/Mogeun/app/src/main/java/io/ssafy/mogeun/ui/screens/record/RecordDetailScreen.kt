package io.ssafy.mogeun.ui.screens.record

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.Exercise
import io.ssafy.mogeun.model.SetResult
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.screens.routine.searchRoutine.muscleIcon

@Composable
fun RecordDetailScreen(
    navController: NavHostController,
    viewModel: RecordViewModel = viewModel(factory = AppViewModelProvider.Factory),
    reportKey: String?
) {
    val recordMonthlySuccess by viewModel.recordRoutineSuccess.collectAsState()
    if (!recordMonthlySuccess) {
        LaunchedEffect(viewModel.userKey) {
            Log.d("reportKey", reportKey.toString())
            viewModel.recordRoutine(reportKey.toString())
        }
    }

    val routineInfo = viewModel.routineInfo

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp,
                vertical = 10.dp
            )
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (routineInfo != null) {
            RoutineInfoCard(routineInfo.name, routineInfo.calorie, routineInfo.totalSets, routineInfo.performTime)
        }
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { viewModel.routineInfo?.let { RoutineGraphIconCard(it.exercises) } }
            if (routineInfo != null) {
                itemsIndexed(routineInfo.exercises) {index, item ->
                    RoutineExerciseCard(navController, item.execName, item.sets, item.imagePath, item.setResults)
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
fun RoutineGraphIconCard(
    exercises: List<Exercise>
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 20.dp
            )
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column (
            modifier = Modifier.fillMaxWidth()
        ) {
            GraphCard()
            IconCard(exercises)
        }
    }
}

@Composable
fun GraphCard() {
    Box (modifier = Modifier
        .fillMaxWidth()
    ) {
        val producer = ChartEntryModelProducer(getRandomEntries())
        Chart(
            chart = lineChart(),
            chartModelProducer = producer,
            startAxis = startAxis(),
            bottomAxis = bottomAxis()
        )
    }
}

fun getRandomEntries() = List(9) { entryOf(it, it * 10) }

@Composable
fun IconCard(
    exercises: List<Exercise>
) {
    Box (
        modifier = Modifier.fillMaxWidth()
    ) {
        var parts: List<String> = emptyList()
        for (exercise in exercises) {
            parts = parts.union(exercise.parts).toList()
        }

        Column {
            Text("사용근육")
            MuscleGrid(
                columns = 5,
                itemCount = parts.size,
                modifier = Modifier
                    .padding(start = 7.5.dp, end = 7.5.dp)
            ) {
                Log.d("part", parts[it])
                muscleIcon(parts[it])
            }
        }
    }
}

@Composable
fun RoutineExerciseCard(
    navController: NavHostController,
    name: String,
    sets: Int,
    imagePath: String,
    setResult: List<SetResult>
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
                    val exerciseImage = LocalContext.current.resources.getIdentifier("z_" + imagePath, "drawable", LocalContext.current.packageName)
//                    Image(
//                        painter = painterResource(id = exerciseImage),
//                        contentDescription = "logo",
//                    )
                    GifImage(imageId = exerciseImage)
                    Text(name)
                }
                WeightGrid(
                    columns = 4,
                    itemCount = sets,
                    modifier = Modifier
                        .padding(start = 7.5.dp, end = 7.5.dp)
                ) {
                    SetWeightIcon(setResult[it].weight.toInt())
                }
            }
            ClickableText(
                modifier = Modifier.align(Alignment.End),
                text = AnnotatedString("자세히 보기"),
                onClick = { navController.navigate("ExerciseDetail") },
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
                Column () {
                    Image(
                        modifier = Modifier.fillMaxSize(0.3f),
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "logo",
                    )
                    Text("test")
                }
                WeightGrid(
                    columns = 5,
                    itemCount = 6,
                    modifier = Modifier
                        .padding(start = 7.5.dp, end = 7.5.dp)
                ) {
                    SetWeightIcon(it)
                }
            }
            ClickableText(
                modifier = Modifier.align(Alignment.End),
                text = AnnotatedString("자세히 보기"),
                onClick = { },
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
