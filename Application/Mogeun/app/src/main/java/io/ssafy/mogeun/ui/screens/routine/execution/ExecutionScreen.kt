package io.ssafy.mogeun.ui.screens.routine.execution

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ssafy.mogeun.ui.BluetoothViewModel
import io.ssafy.mogeun.ui.screens.routine.execution.components.ExecutionGif
import io.ssafy.mogeun.ui.screens.routine.execution.components.ExerciseProgress
import io.ssafy.mogeun.ui.screens.routine.execution.components.RoutineProgress


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExecutionScreen(viewModel: BluetoothViewModel, routineKey: Int) {
    val emgState by viewModel.emgState.collectAsState()

    val routineState by viewModel.routineState.collectAsState()

    Log.d("execution", "${routineKey ?: "null"}")

    LaunchedEffect(Unit) {
        viewModel.getPlanList(routineKey)
    }

    if(routineState.planList == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                trackColor = MaterialTheme.colorScheme.secondary,
            )
        }
    } else {
        val pagerState = rememberPagerState { routineState.planList!!.data.size }
        Column {
            HorizontalPager(
                pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val imgPath = routineState.planList!!.data[page].imagePath
                Column {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(12.dp)
                    ) {
                        ExecutionGif(imgPath = imgPath)
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        ExerciseProgress(emgState)
                    }
                }
            }
            RoutineProgress()
        }

    }
}



