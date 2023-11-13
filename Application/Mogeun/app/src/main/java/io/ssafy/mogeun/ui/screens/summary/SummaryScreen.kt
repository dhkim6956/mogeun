package io.ssafy.mogeun.ui.screens.summary

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.jaikeerthick.composable_graphs.color.LinearGraphColors
import com.jaikeerthick.composable_graphs.composables.LineGraph
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.style.LabelPosition
import com.jaikeerthick.composable_graphs.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.style.LinearGraphVisibility
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.BodyInfo
import io.ssafy.mogeun.model.Exercise
import io.ssafy.mogeun.model.SetResult
import io.ssafy.mogeun.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun SummaryScreen(viewModel: SummaryViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    // 요약 페이지 구성을 위한 api 통신
    val summaryBodyInfoSuccess by viewModel.summaryBodyInfoSuccess.collectAsState()
    if (!summaryBodyInfoSuccess) {
        LaunchedEffect(viewModel.userKey) {
            viewModel.summaryBody()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp
            )
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End
    ) {
        BodyInfoSummaryCard(viewModel.summaryBodyInfo)
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        )
        Dropdown()
        Spacer(
            modifier = Modifier
                .height(5.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        )
        ExerciseSummaryCard()
        Spacer(
            modifier = Modifier
                .height(10.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer)
        )
        MuscleSummaryCard()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyInfoSummaryCard(bodyInfo: BodyInfo?) {
    Box(
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
        val pagerState = rememberPagerState(pageCount = {
            2
        })
        val nameList = listOf("체지방 변화량", "골격근 변화량")

        Column {
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
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
                BodyInfoSummary(page)
            }
        }
    }
}

@Composable
fun BodyInfoSummary(index: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val style = LineGraphStyle(
            visibility = LinearGraphVisibility(
                isHeaderVisible = true,
                isXAxisLabelVisible = false,
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
        val clickedValue: MutableState<Pair<Any, Any>?> =
            remember { mutableStateOf(null) }

        Row(
            modifier = Modifier
                .padding(10.dp)
                .height(20.dp)
        ) {
            clickedValue.value?.let {
                Text(
                    text = "${it.first}: ${it.second}kg",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        LineGraph(
            xAxisData = listOf("2023-11-13", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat").map {
                GraphData.String(it)
            }, // xAxisData : List<GraphData>, and GraphData accepts both Number and String types
            yAxisData = listOf(200, 40, 60, 450, 700, 30, 50),
            style = style,
            onPointClicked = {
                clickedValue.value = it
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseSummaryCard() {
    Box(
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
        val pagerState = rememberPagerState(pageCount = {
            3
        })
        val nameList = listOf("가장 많이 한 운동", "가장 높은 무게를 기록한 운동", "가장 많은 세트를 수행한 운동")

        Column {
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val coroutineScope = rememberCoroutineScope()

                Text(
                    text = "<",
                    modifier = Modifier
                        .clickable { coroutineScope.launch {
                            // Call scroll to on pagerState
                            if (pagerState.currentPage > 0)
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        } }
                )
                Text(nameList[pagerState.currentPage])
                Text(
                    text = ">",
                    modifier = Modifier
                        .clickable { coroutineScope.launch {
                            // Call scroll to on pagerState
                            if (pagerState.currentPage < 2)
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } }
                )
            }
            HorizontalPager(state = pagerState) { page ->
                // Our page content
                ExerciseSummary(nameList[page])
            }
        }
    }
}

@Composable
fun ExerciseSummary(
    name: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(0.3f),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = "test")
            Text(text = name + " = " + "test")
        }
    }
}

@Composable
fun MuscleSummaryCard() {
    Box(
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
        MuscleSummary(listOf(Exercise("test", "test", 1, listOf("주 등", "부 어깨"), listOf("test"), listOf(
            SetResult(1,1f, 1, 1, null)
        ))))
    }
}

@Composable
fun MuscleSummary(exercises: List<Exercise>) {
    val yStepSize = 9
    var map: MutableMap<String, Float> = mutableMapOf(
        "가슴" to 0f, "등" to 0f, "허벅지" to 0f, "어깨" to 0f, "이두" to 0f, "삼두" to 0f, "승모근" to 0f, "종아리" to 0f, "복근" to 0f, "" to 0f
    )
    var index = 0
    var barChartdata: MutableList<BarData> = mutableListOf()
    var maxHeight = 0f

    for (exercise in exercises) {
        var prevPart = ""
        for (part in exercise.parts) {
            val partDetail = part.split(" ")
            if (prevPart == "") prevPart = partDetail[1]
            else if (prevPart == partDetail[1]) continue
            if (partDetail[0] == "주") {
                map[partDetail[1]] = map[partDetail[1]]!!.plus(3f)
            }
            else {
                map[partDetail[1]] = map[partDetail[1]]!!.plus(1f)
            }
        }
    }

    for (data in map) {
        if (maxHeight < data.value) maxHeight = data.value
        barChartdata.add(index, BarData(Point(index.toFloat(), data.value, data.key), MaterialTheme.colorScheme.primary, data.key))
        index++
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barChartdata.size - 1)
        .topPadding(10.dp)
        .startDrawPadding(20.dp)
        .shouldDrawAxisLineTillEnd(true)
        .labelData { index -> barChartdata[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(maxHeight.toInt())
        .labelAndAxisLinePadding(20.dp)
        .labelData { index -> (index * (maxHeight / yStepSize)).toString() }
        .axisOffset(20.dp)
        .build()

    Log.d("yAxisData", yAxisData.toString())

    val barChartData = BarChartData(
        chartData = barChartdata,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        showYAxis = false,
        barStyle = BarStyle(selectionHighlightData = null),
        barChartType = BarChartType.VERTICAL,
        paddingEnd = 0.dp
    )

    Box (modifier = Modifier
        .fillMaxWidth()
    ) {
        BarChart(
            modifier = Modifier
                .height(200.dp),
            barChartData = barChartData
        )
    }
}

@Composable
fun Dropdown() {
    val listItems = arrayOf("전체", "올해", "이번 달")

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
                color = Color.Black
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