package io.ssafy.mogeun.ui.screens.summary

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.Exercise

@Composable
fun SummaryScreen() {
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
        BodyInfoSummaryCard()
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

@Composable
fun BodyInfoSummaryCard() {
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
        BodyInfoSummary()
    }
}

@Composable
fun BodyInfoSummary() {
    val pointsData: List<Point> =
        listOf(Point(0f, 40f), Point(1f, 90f), Point(2f, 0f), Point(3f, 60f), Point(4f, 10f))
    val steps = pointsData.size

    val xAxisData = AxisData.Builder()
        .axisStepSize(50.dp)
        .steps(steps - 1)
        .labelData { "2023-11-12" }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = 100 / steps
            (i * yScale).toString()
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(lineType = LineType.Straight()),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        backgroundColor = Color.White
    )

    LineChart(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        lineChartData = lineChartData
    )
}

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
        BodyInfoSummary()
    }
}

@Composable
fun ExerciseSummary() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

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
        BodyInfoSummary()
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