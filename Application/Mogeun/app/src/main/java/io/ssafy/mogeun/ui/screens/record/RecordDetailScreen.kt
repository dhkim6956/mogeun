package io.ssafy.mogeun.ui.screens.record

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryOf
import io.ssafy.mogeun.R
import kotlin.random.Random

data class routineInfo(
    val title: String,
    val detail: String
)

@Composable
fun RecordDetailScreen(navController: NavHostController) {
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
        RoutineInfoCard()
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { RoutineGraphIconCard() }
            itemsIndexed(listOf(1, 2, 3)) {index, item ->
                RoutineExerciseCard(navController)
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
        RoutineInfoCard()
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { RoutineGraphIconCard() }
            itemsIndexed(listOf(1, 2, 3)) {index, item ->
                RoutineExerciseCardPreview()
            }
        }
    }
}

@Composable
fun RoutineInfoCard() {
    var routineInfoList: List<routineInfo> = listOf(routineInfo("운동일자", "2023년 10월 6일"), routineInfo("소모 칼로리", "72kcal"), routineInfo("수행한 세트", "22set"), routineInfo("운동한 시간", "71분"))
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
            Text("test", fontSize = 24.sp)
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
            ) {
                routineInfoList.forEach{
                    RoutineInfo(it)
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
    routineInfo: routineInfo
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
        Text(routineInfo.title)
        Text(routineInfo.detail)
    }
}

@Preview
@Composable
fun RoutineGraphIconCard() {
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
            IconCard()
        }
    }
}

@Composable
fun GraphCard() {
    Box (modifier = Modifier
        .fillMaxWidth()
    ) {
//        Image(
//            modifier = Modifier.fillMaxWidth(),
//            painter = painterResource(id = R.drawable.logo),
//            contentDescription = "logo",
//        )
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
fun IconCard() {
    Box (
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text("사용근육")
            NonlazyGrid(
                columns = 5,
                itemCount = 9,
                modifier = Modifier
                    .padding(start = 7.5.dp, end = 7.5.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                )
            }
        }
    }
}

@Composable
fun RoutineExerciseCard(navController: NavHostController) {
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
                NonlazyGrid(
                    columns = 5,
                    itemCount = 4,
                    modifier = Modifier
                        .padding(start = 7.5.dp, end = 7.5.dp)
                ) {
                    SetWeightIcon()
                }
            }
            ClickableText(
                modifier = Modifier.align(Alignment.End),
                text = AnnotatedString("자세히 보기"),
                onClick = { navController.navigate("exercisedetail") },
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
                NonlazyGrid(
                    columns = 5,
                    itemCount = 6,
                    modifier = Modifier
                        .padding(start = 7.5.dp, end = 7.5.dp)
                ) {
                    SetWeightIcon()
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
fun SetWeightIcon() {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // This is important for square-sizing!
            .padding(6.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "100",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
        )
    }
}

@Composable
fun NonlazyGrid(
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
