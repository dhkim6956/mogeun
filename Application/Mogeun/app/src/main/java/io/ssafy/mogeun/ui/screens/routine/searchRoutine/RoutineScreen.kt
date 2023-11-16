package io.ssafy.mogeun.ui.screens.routine.searchRoutine

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.More
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.GetRoutineListResponseBody
import io.ssafy.mogeun.ui.components.MuscleTooltipIcon

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
        Column(
            modifier = Modifier
                .shadow(2.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                    .padding(horizontal = 20.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${viewModel.username}",
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.hello),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f),
                    fontSize = 16.sp
                )
                IconButton(onClick = { navController.navigate("User") }) {
                    Icon(imageVector = Icons.Default.DriveFileRenameOutline, "edit user info")
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                    )
                    .padding(top = 20.dp, bottom = 20.dp, start = 40.dp, end = 40.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.muscle_mass))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "${viewModel.muscleMass.toString()} kg")

                    }
                    Divider(
                        thickness = 1.dp,
                        color = Color.Black.copy(0.2f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.body_fat))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "${viewModel.bodyFat.toString()} kg")

                    }
                    Divider(
                        thickness = 1.dp,
                        color = Color.Black.copy(0.2f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            viewModel.tmp?.let {
                itemsIndexed(it.data) { index, item ->
                    if (item.imagePath.size !== 0){
                        RoutineList(navController, item, index, viewModel)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item() {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.White.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .alpha(0.5f)
                            .height(100.dp)
                            .clickable {
                                navController.navigate("addexercise/${beforeScreen}/3")
                            }
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Row {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "add routine")
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = stringResource(R.string.add_routine), color = MaterialTheme.colorScheme.scrim)
                            }
                        }
                    }
                }
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
    viewModel: RoutineViewModel
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = routine.name ?: "name",
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.More,
                        contentDescription = "about this routine"
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {
                    items(routine.imagePath) { target ->
                        MuscleTooltipIcon(target, 48.dp, 32.dp, 1)
                    }
                }
                Button(
                    onClick = {
                        navController.navigate("Execution/${routine.routineKey}")
                    },
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 0.dp,
                    ),
                    modifier = Modifier
                        .padding(2.dp)
                ) {
                    Text(text = stringResource(R.string.routine_start))
                }
            }
        }
    }

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
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.renaming),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = { navController.navigate("addroutine/${routine.routineKey}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.routine_management),
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
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.delete_routine),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if(openAlertDialog.value) {
        AlertDialogExample(
            onConfirmation = {
                viewModel.updateRoutineName(index, viewModel.newRoutineName.value)
                openAlertDialog.value = false
            },
            dialogTitle = stringResource(R.string.set_routine_name),
            onDismissRequest = {
                openAlertDialog.value = false
            },
            icon = Icons.Default.Info,
        )
    }

}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    icon: ImageVector,
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
                Text(stringResource(R.string.check))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.cancellation))
            }
        }
    )
}

