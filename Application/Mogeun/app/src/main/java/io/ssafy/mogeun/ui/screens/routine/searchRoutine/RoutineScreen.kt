package io.ssafy.mogeun.ui.screens.routine.searchRoutine

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.GetRoutineListResponseBody
import kotlinx.coroutines.launch

@Composable
fun RoutineScreen(
    viewModel: RoutineViewModel = viewModel(factory = RoutineViewModel.Factory),
    navController: NavHostController
) {
    val beforeScreen = 1
    LaunchedEffect(Unit) {
        viewModel.getUserKey()
    }
    LaunchedEffect(viewModel.userKey) {
        if (viewModel.userKey !== null) {
            viewModel.getInbody()
            viewModel.getRoutineList()
        }
    }
    Column(modifier = Modifier.padding(10.dp)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .width(280.dp)
                        .padding(start = 24.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = Modifier.width(144.dp),
                            text = "${viewModel.username}",
                            fontSize = 24.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = " 님 안녕하세요.",
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 16.sp
                        )
                    }
                }
                Button(
                    onClick = { navController.navigate("User") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "edit",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                    )
            ) {
                Row(modifier = Modifier
                    .padding(top = 20.dp, start = 40.dp, end = 40.dp)
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2f,
                        )
                    }
                ) {
                    Text(text = "골격근량")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${viewModel.muscleMass.toString()} kg")
                }
                Row(modifier = Modifier
                    .padding(
                        top = 20.dp,
                        start = 40.dp,
                        bottom = 20.dp,
                        end = 40.dp
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
                ) {
                    Text(text = "체지방량")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${ viewModel.bodyFat.toString() } kg")
                }
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            viewModel.tmp?.let {
                itemsIndexed(it.data) { index, item ->
                    if (item.imagePath.size !== 0){
                        RoutineList(navController, item, index)
                    }
                }
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 30.dp, bottom = 30.dp), contentAlignment = Alignment.BottomStart) {
        Button(
            onClick = { navController.navigate("addexercise/${beforeScreen}/3") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            shape = RoundedCornerShape(10.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp,
                pressedElevation = 0.dp,
            ),
        ) {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.add_routine),
                    contentDescription = "add_routine",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "루틴추가", color = MaterialTheme.colorScheme.scrim)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineList(
    navController: NavHostController,
    routine: GetRoutineListResponseBody,
    index: Int,
    viewModel: RoutineViewModel = viewModel(factory = RoutineViewModel.Factory)
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.getUserKey()
    }
    LaunchedEffect(viewModel.userKey) {
        if (viewModel.userKey !== null) {
            viewModel.getInbody()
            viewModel.getRoutineList()
        }
    }
    val beforeScreen = 1
    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.onPrimary)
        .padding(top = 20.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = routine.name?: "name",
                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            var showBottomSheet by remember { mutableStateOf(false) }
            Button(
                onClick = { showBottomSheet = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.symbol_more),
                    contentDescription = "dotdotdot",
                    contentScale = ContentScale.Crop,
                )
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = sheetState
                    ) {
                        Button(
                            onClick = {
                                showBottomSheet = false
                                openAlertDialog.value = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "이름 변경",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = { navController.navigate("addroutine/${routine.routineKey}") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "루틴 관리",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = {
                                showBottomSheet = false
                                viewModel.deleteRoutine(index)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "루틴 삭제",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
        when {
            openAlertDialog.value -> {
                AlertDialogExample(
                    onConfirmation = {
                        Log.d("routineName", "${viewModel.newRoutineName.value}")
                        viewModel.updateRoutineName(index, viewModel.newRoutineName.value)
                        openAlertDialog.value = false
                    },
                    dialogTitle = "루틴 이름을 설정해 주세요.",
                    onDismissRequest = {
                        openAlertDialog.value = false
                    },
                    icon = Icons.Default.Info,
                    index = index,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(modifier = Modifier.width(200.dp)) {
                LazyRow() {
                    items(routine.imagePath) { target ->
                        muscleIcon(target)
                    }
                }
            }
            Button(
                onClick = { navController.navigate("Execution/${routine.routineKey}") },
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 10.dp,
                    pressedElevation = 0.dp,
                ),
            ) {
                Text(text = "루틴시작")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}
@Composable
fun muscleIcon(imagePath: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                RoundedCornerShape(15.dp)
            )
            .width(48.dp)
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        val image = LocalContext.current.resources.getIdentifier(imagePath, "drawable", LocalContext.current.packageName)
        Image(
            painter = painterResource(id = image),
            contentDescription = imagePath,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(32.dp)
                .width(32.dp)
        )
    }
    Spacer(modifier = Modifier.width(10.dp))
}
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    icon: ImageVector,
    index : Int,
) {
    val viewModel: RoutineViewModel = viewModel(factory = RoutineViewModel.Factory)
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            TextField(
                value = viewModel.newRoutineName.value,
                onValueChange = {
                    viewModel.updateRoutineName(it)
                }
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()

                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}