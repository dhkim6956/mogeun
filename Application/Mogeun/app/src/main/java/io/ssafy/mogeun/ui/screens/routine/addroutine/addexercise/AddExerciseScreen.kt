package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise


import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import com.skydoves.landscapist.glide.GlideImage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ssafy.mogeun.ui.screens.routine.addroutine.AddRoutineViewModel
import io.ssafy.mogeun.ui.AppViewModelProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    navController: NavHostController,
    viewModel: AddExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val musclePartList = listOf("전체", "chest", "등", "복근", "삼두", "승모근", "어깨", "이두", "종아리", "허벅지")
    var selectedExercises by remember { mutableStateOf(setOf<String>()) }
    val openAlertDialog = remember { mutableStateOf(false) }
    val exercises = viewModel.exerciseList

    val (searchText, setSearchText) = remember {
        mutableStateOf("")
    }
    var selectedMusclePart by remember { mutableStateOf("전체") }
    LaunchedEffect(Unit){
        viewModel.listAllExercise()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)

    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = searchText,
            onValueChange = setSearchText,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                ),
            placeholder  = {Text("검색")}
        )

        Spacer(modifier = Modifier.height(8.dp))

        // muscle-part
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(musclePartList) { musclePart ->
                FilterChip(
                    selected = selectedMusclePart == musclePart,
                    onClick = { selectedMusclePart = musclePart },
                    label = { Text(musclePart) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        //exercise-list
        LazyColumn {
            val filteredExercises = exercises
                .filter { it.mainPart == selectedMusclePart || selectedMusclePart == "전체" }
                .filter {
                    it.name.contains(searchText, ignoreCase = true) || it.engName.contains(searchText, ignoreCase = true)
                }
            items(filteredExercises) { exercise ->
                val isSelected = exercise.name in selectedExercises
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                        .clickable {
                            if (isSelected) {
                                selectedExercises = selectedExercises - exercise.name
                            } else {
                                selectedExercises = selectedExercises + exercise.name
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GlideImage(
                            imageModel = exercise.imagePath.toString(),
                            contentDescription = "GIF Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(60.dp)
                                .width(60.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exercise.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
//                            Text(text = exercise.mainPart)
                        }
                        Icon(
                            imageVector = if (isSelected) Icons.Outlined.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Localized description"
                        )
                        IconButton(onClick = { navController.navigate("explainexercise/${exercise.imagePath.toString()}") }) {
                            Icon(Icons.Outlined.ErrorOutline,
                                contentDescription = "Localized description",
                                modifier = Modifier.graphicsLayer(rotationZ = 180f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd,

        ) {
            if (selectedExercises.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = { openAlertDialog.value = true },
                    ) {
                        Text("선택된 운동 추가")
                    }
                }
                when {
                    openAlertDialog.value -> {
                        io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise.AlertDialogExample(
                            onDismissRequest = { openAlertDialog.value = false },
                            onConfirmation = {
                                openAlertDialog.value = false
                                println("Confirmation registered") // Add logic here to handle confirmation.
                            },
                            dialogTitle = "루틴 이름을 설정해 주세요.",
                            dialogText = "This is an example of an alert dialog with buttons.",
                            icon = Icons.Default.Info,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun receiveExerciseData(
    adjacentMonths: Long = 500,
    navController: NavHostController,
    viewModel: AddExerciseViewModel = viewModel(factory = AppViewModelProvider.Factory)
){}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    navController: NavHostController,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    val (userKey, setName) = remember { mutableStateOf("") }
    val viewModel: AddRoutineViewModel = viewModel(factory = AddRoutineViewModel.Factory)
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            TextField(value = userKey, onValueChange = setName )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val ret = viewModel.addRoutine("11", "mogun1234")
                    Log.d("addRoutine", "$ret")
                    // 운동에 대한 정보를 post
                    navController.popBackStack()
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