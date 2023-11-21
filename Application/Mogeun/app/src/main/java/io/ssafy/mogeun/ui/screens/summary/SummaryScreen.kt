package io.ssafy.mogeun.ui.screens.summary

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.extensions.isNotNull
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.jaikeerthick.composable_graphs.color.LinearGraphColors
import com.jaikeerthick.composable_graphs.data.GraphData
import com.jaikeerthick.composable_graphs.style.LabelPosition
import com.jaikeerthick.composable_graphs.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.style.LinearGraphVisibility
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.BodyInfo
import io.ssafy.mogeun.model.MostPerformedExercise
import io.ssafy.mogeun.model.MostSetExercise
import io.ssafy.mogeun.model.MostWeightedExercise
import io.ssafy.mogeun.model.PerformedMuscleInfo
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.components.HorizontalPagerArrow
import io.ssafy.mogeun.ui.components.BodyLineGraph
import kotlinx.coroutines.launch

@Composable
fun SummaryScreen(viewModel: SummaryViewModel = viewModel(factory = AppViewModelProvider.Factory), snackbarHostState: SnackbarHostState) {

    var exitCnt = 0
    val activity = (LocalContext.current as? Activity)
    val coroutineScope = rememberCoroutineScope()
    BackHandler {
        if(exitCnt == 0) {
            exitCnt++
            coroutineScope.launch {
                snackbarHostState.showSnackbar("한번 더 누르면 앱이 종료됩니다.")
            }
        } else {
            activity?.finish()
        }
    }

    IndeterminateCircularIndicator(viewModel)

    // 요약 페이지 구성을 위한 api 통신
    val summaryBodyInfoSuccess by viewModel.summaryBodyInfoSuccess.collectAsState()
    val summaryPerformedMuscleSuccess by viewModel.summaryPerformedMuscleSuccess.collectAsState()
    val summaryExerciseMostSuccess by viewModel.summaryExerciseMostSuccess.collectAsState()
    val summaryExerciseWeightSuccess by viewModel.summaryExerciseWeightSuccess.collectAsState()
    val summaryExerciseSetSuccess by viewModel.summaryExerciseSetSuccess.collectAsState()
    val summarySuccess by viewModel.summarySuccess.collectAsState()
    val summaryLoading by viewModel.summaryLoading.collectAsState()
    if (!summarySuccess && !summaryLoading) {
        viewModel.summaryBody()
        viewModel.summaryPerformedMuscle()
        viewModel.summaryExerciseMost()
        viewModel.summaryExerciseWeight()
        viewModel.summaryExerciseSet()
    }
    if (summaryBodyInfoSuccess && summaryPerformedMuscleSuccess && summaryExerciseMostSuccess && summaryExerciseWeightSuccess && summaryExerciseSetSuccess)
        viewModel.updateSummarySuccess()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End
    ) {
        if (summarySuccess) {
            Text("체지방&골격근량 그래프", modifier = Modifier.align(Alignment.Start), fontSize=18.sp, fontWeight = FontWeight.Bold)
            Spacer(
                modifier = Modifier
                    .height(5.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
            BodyInfoSummaryCard(viewModel.summaryBodyInfo)
            Spacer(
                modifier = Modifier
                    .height(30.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
                    .height(2.dp)
            ) {
                Text("")
            }
            Spacer(
                modifier = Modifier
                    .height(30.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text("운동 요약", fontSize=18.sp, fontWeight = FontWeight.Bold)
                SummaryDropdown(viewModel)
            }
            Spacer(
                modifier = Modifier
                    .height(5.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
            ExerciseSummaryCard(
                viewModel.summaryExerciseMost[viewModel.itemIndex.value],
                viewModel.summaryExerciseWeight[viewModel.itemIndex.value],
                viewModel.summaryExerciseSet[viewModel.itemIndex.value]
            )
            Spacer(
                modifier = Modifier
                    .height(10.dp)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
            )
            Text("사용 근육 분포", modifier = Modifier.align(Alignment.Start), fontSize=18.sp, fontWeight = FontWeight.Bold)
            MuscleSummaryCard(viewModel.summaryPerformedMuscle[viewModel.itemIndex.value])
        }
    }
}

data class BodyLog(
    val num: Number,
    val log: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BodyInfoSummaryCard(bodyInfo: BodyInfo?) {
    val bodyFatChangeLog = bodyInfo?.bodyFatChangeLog?.reversed()
    var bodyFatLog: MutableList<BodyLog> = mutableListOf()
    var index = 0
    bodyFatChangeLog?.map {
        val time = it.changedTime.split("T")[0]
        val timeSplit = time.split("-")
        bodyFatLog.add(index, BodyLog(it.bodyFat, timeSplit[0] + "년 " + timeSplit[1] + "월 " + timeSplit[2] + "일"))
        index++
    }

    val muscleMassChangeLog = bodyInfo?.muscleMassChangeLog?.reversed()
    var muscleMassLog: MutableList<BodyLog> = mutableListOf()
    index = 0
    muscleMassChangeLog?.map {
        val time = it.changedTime.split("T")[0]
        val timeSplit = time.split("-")
        muscleMassLog.add(index, BodyLog(it.muscleMass, timeSplit[0] + "년 " + timeSplit[1] + "월 " + timeSplit[2] + "일"))
        index++
    }

    val bodyLogList = listOf(bodyFatLog, muscleMassLog)

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
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val coroutineScope = rememberCoroutineScope()

                HorizontalPagerArrow(
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                // Call scroll to on pagerState
                                pagerState.animateScrollToPage(0)
                            } },
                    size = 30.dp,
                    visible = pagerState.currentPage > 0,
                    direction = true
                )
                Text(nameList[pagerState.currentPage])
                HorizontalPagerArrow(
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                // Call scroll to on pagerState
                                pagerState.animateScrollToPage(1)
                            } },
                    size = 30.dp,
                    visible = pagerState.currentPage < 1,
                    direction = false
                )
            }
            HorizontalPager(state = pagerState) { page ->
                // Our page content
                BodyInfoSummary(bodyLogList[page])
            }
        }
    }
}

@Composable
fun BodyInfoSummary(bodyLog: MutableList<BodyLog>) {
    if (bodyLog.isNullOrEmpty()) {
        Column(Modifier.fillMaxWidth()){
            Text("기록이 없습니다.", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
    else {
        Column(modifier = Modifier.fillMaxWidth()) {
            val style = LineGraphStyle(
                paddingValues = PaddingValues(5.dp),
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
                    .padding(top = 10.dp)
                    .height(25.dp)
            ) {
                clickedValue.value?.let {
                    Text(
                        text = "${it.first}: ${it.second}kg",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            BodyLineGraph(
                xAxisData = bodyLog.map {
                    GraphData.String(it.log)
                },
                yAxisData = bodyLog.map {
                    it.num
                },
                style = style,
                onPointClicked = {
                    clickedValue.value = it
                }
            )
        }
    }
}

data class SummaryCard(
    val execName: String,
    val imagePath: String,
    val num: Number
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseSummaryCard(
    mostPerformedExercise: MostPerformedExercise?,
    mostWeightedExercise: MostWeightedExercise?,
    mostSetExercise: MostSetExercise?
) {
    if (mostPerformedExercise.isNotNull() && mostWeightedExercise.isNotNull() && mostSetExercise.isNotNull()) {
        val SummaryCardList = listOf(
            SummaryCard(
                mostPerformedExercise!!.execName,
                mostPerformedExercise!!.imagePath,
                mostPerformedExercise!!.performed
            ),
            SummaryCard(
                mostWeightedExercise!!.execName,
                mostWeightedExercise!!.imagePath,
                mostWeightedExercise!!.weight.toInt()
            ),
            SummaryCard(mostSetExercise!!.execName,
                mostSetExercise.imagePath,
                mostSetExercise.set)
        )
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

                    HorizontalPagerArrow(
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    if (pagerState.currentPage > 0)
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                } },
                        size = 30.dp,
                        visible = pagerState.currentPage > 0,
                        direction = true
                    )
                    Text(nameList[pagerState.currentPage])
                    HorizontalPagerArrow(
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    if (pagerState.currentPage < 2)
                                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                } },
                        size = 30.dp,
                        visible = pagerState.currentPage < 2,
                        direction = false
                    )
                }
                HorizontalPager(state = pagerState) { page ->
                    // Our page content
                    ExerciseSummary(page, SummaryCardList[page])
                }
            }
        }
    }
    else {
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
            Text("운동 기록이 없습니다.")
        }
    }
}

@Composable
fun ExerciseSummary(
    page: Int,
    summaryCard: SummaryCard
) {
    val cardNameList = listOf("수행한 횟수", "최고 기록", "수행한 세트")
    val unitList = listOf("회", "kg", "회")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val exerciseImage = LocalContext.current.resources.getIdentifier("x_" + summaryCard.imagePath, "drawable", LocalContext.current.packageName)
        Image(
            modifier = Modifier.fillMaxWidth(0.3f),
            painter = painterResource(exerciseImage),
            contentDescription = summaryCard.imagePath
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = summaryCard.execName)
            Text(text = cardNameList[page] + " = " + summaryCard.num.toString() + unitList[page])
        }
    }
}

@Composable
fun MuscleSummaryCard(muscleInfoList: List<PerformedMuscleInfo>?) {
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
        if (muscleInfoList.isNullOrEmpty())
            Text("운동 기록이 없습니다.")
        else
            MuscleSummary(muscleInfoList!!)
    }
}

@Composable
fun MuscleSummary(exercises: List<PerformedMuscleInfo>) {
    val yStepSize = 9
    var map: MutableMap<String, Float> = mutableMapOf(
        "가슴" to 0f, "등" to 0f, "허벅지" to 0f, "어깨" to 0f, "이두" to 0f, "삼두" to 0f, "승모근" to 0f, "종아리" to 0f, "복근" to 0f
    )
    var index = 0
    var barChartdata: MutableList<BarData> = mutableListOf()
    var maxHeight = 0f

    for (exercise in exercises) {
        for (part in exercise.parts) {
            val partDetail = part.split(" ")
            if (partDetail[0] == "주") {
                map[partDetail[1]] = map[partDetail[1]]!!.plus(3f)
            }
            else {
                map[partDetail[1]] = map[partDetail[1]]!!.plus(1f)
            }
        }
    }
    val tmpMap = map.toList()
    var muscleMap = tmpMap.sortedByDescending { it.second }.toMap().toMutableMap()
    muscleMap[""] = 0f

    for (data in muscleMap) {
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
fun SummaryDropdown(viewModel: SummaryViewModel) {
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

@Composable
fun IndeterminateCircularIndicator(viewModel: SummaryViewModel) {
    val summarySuccess by viewModel.summarySuccess.collectAsState()
    if (summarySuccess) return

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.surface,
            trackColor = MaterialTheme.colorScheme.primary,
        )
    }
}