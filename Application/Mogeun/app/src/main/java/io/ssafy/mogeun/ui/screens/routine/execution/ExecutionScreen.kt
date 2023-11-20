package io.ssafy.mogeun.ui.screens.routine.execution

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.extensions.isNotNull
import io.ssafy.mogeun.R
import io.ssafy.mogeun.data.AppContainer
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.components.AlertDialogCustom
import io.ssafy.mogeun.ui.components.ElevatedGif
import io.ssafy.mogeun.ui.screens.routine.execution.components.ExerciseProgress
import io.ssafy.mogeun.ui.screens.routine.execution.components.RoutineProgress
import io.ssafy.mogeun.ui.screens.routine.execution.components.SensorBottomSheet
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExecutionScreen(viewModel: ExecutionViewModel = viewModel(factory = AppViewModelProvider.Factory), routineKey: Int, navController: NavHostController, snackbarHostState: SnackbarHostState) {
    val emgState by viewModel.emgState.collectAsState()
    val sensorState by viewModel.sensorState.collectAsState()
    val routineState by viewModel.routineState.collectAsState()
    val elapsedTime by viewModel.elaspedTime.collectAsState()

    val muscleavg by viewModel.muscleavg.collectAsState()

    var openEndDialog by remember { mutableStateOf(false) }
    var openNoSensorDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        openEndDialog = true
    }

    LaunchedEffect(Unit) {
        viewModel.getUserKey()
    }

    LaunchedEffect(viewModel.userKey) {
        if (!routineState.routineInProgress && viewModel.userKey.isNotNull()) {
            viewModel.startRoutine(routineKey)
            snackbarHostState.showSnackbar("루틴을 시작합니다.")
        }

        viewModel.getSensorVal()
    }

    DisposableEffect(Unit) {
        coroutineScope.launch {
            val ret = async {
                viewModel.getPlanList(routineKey)
            }.await()
            viewModel.getSetOfRoutine()
        }
        this.onDispose {
            viewModel.resetRoutine()
        }
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
        val routineSize = routineState.planList!!.data.size
        val pagerState = rememberPagerState { routineSize }

        if(routineState.planDetails.isNotEmpty()) {
            Column {
                HorizontalPager(
                    pagerState,
                    userScrollEnabled = !routineState.setInProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { page ->
                    val plan = routineState.planList!!.data[page]
                    val imgPath = plan.imagePath

                    val scrollState = rememberScrollState()

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(12.dp)
                        ) {
                            ElevatedGif(imgPath = imgPath,
                                Modifier
                                    .width(300.dp)
                                    .height(200.dp))
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(horizontal = 40.dp)
                        ) {
                            Text("${plan.name}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            FilledTonalIconButton(
                                enabled = !routineState.setInProgress,
                                onClick = viewModel::showBottomSheet,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .width(44.dp)
                                    .height(44.dp)
                            ){
                                Icon(painter = painterResource(id = R.drawable.heart_rate), null, modifier = Modifier.fillMaxSize(0.8f))
                            }
                        }
                        ExerciseProgress(
                            emgState,
                            routineState.planDetails[page].setOfRoutineDetail,
                            {viewModel.addSet(plan.planKey)},
                            {idx -> viewModel.removeSet(plan.planKey, idx)},
                            {idx, weight -> viewModel.setWeight(plan.planKey, idx, weight)},
                            {idx, rep -> viewModel.setRep(plan.planKey, idx, rep)},
                            {idx -> viewModel.startSet(plan.planKey, idx)},
                            {idx -> viewModel.addCnt(plan.planKey, idx)},
                            {idx -> viewModel.endSet(plan.planKey, idx)},
                            routineState.setInProgress,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                RoutineProgress(pagerState.currentPage + 1, routineSize, elapsedTime, {openEndDialog = true}, routineState.setInProgress, {coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage - 1) }}, {coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) }}, routineSize, pagerState.currentPage)

                SensorBottomSheet(state = routineState.showBottomSheet, hide = viewModel::hideBottomSheet, navToConnection = {navController.navigate("Connection")}, sensorState = sensorState, sensingPart = routineState.planList!!.data[pagerState.currentPage].mainPart.imagePath)
            }
        }
    }
    when {
        openEndDialog -> {
            AlertDialogCustom(
                onDismissRequest = {
                    openEndDialog = false
                                   },
                onConfirmation = {
                    openEndDialog = false
                    viewModel.endRoutine()
                    if (routineState.hasValidSet) {
                        navController.navigate("RecordDetail/${routineState.reportKey}",) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("루틴이 종료되었습니다.")
                        }
                    } else {
                        navController.popBackStack()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("루틴이 취소되었습니다.")
                        }
                    }

                                 },
                dialogTitle = if (routineState.hasValidSet) {"오늘의 루틴 종료"} else {"루틴 진행 취소"},
                dialogText = if (routineState.hasValidSet) {"현재까지의 진행상황을 기록하고 운동을 종료합니다."} else {"측정된 횟수가 없어 진행상황을 기록하지 않고 돌아갑니다"},
                icon = Icons.Default.PauseCircle
            )
        }
    }
    when {
        openNoSensorDialog -> {
            AlertDialogCustom(
                onDismissRequest = {
                    openNoSensorDialog = false
                },
                onConfirmation = {
                    openNoSensorDialog = false
                    viewModel.endRoutine()
                    if (routineState.hasValidSet) {
                        navController.navigate("RecordDetail/${routineState.reportKey}",) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("루틴이 종료되었습니다.")
                        }
                    } else {
                        navController.popBackStack()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("루틴이 취소되었습니다.")
                        }
                    }

                },
                dialogTitle = "센서가 연결되지 않았습니다.",
                dialogText = "센서를 연결하지 않고 진행하시겠습니까?",
                icon = Icons.Default.PauseCircle
            )
        }
    }
}