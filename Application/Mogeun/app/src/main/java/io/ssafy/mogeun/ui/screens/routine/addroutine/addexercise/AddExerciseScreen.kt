package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise


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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(navController: NavHostController) {
    val musclePartList = listOf("전체", "가슴", "등", "복근", "삼두", "승모근", "어깨", "이두", "종아리", "허벅지")
    var selectedExercises by remember { mutableStateOf(setOf<String>()) }
    val openAlertDialog = remember { mutableStateOf(false) }
    data class Exercise(val name: String, val main_part: String, val image: Int, val engName: String)
    val exerciseArray = arrayOf(
        Exercise("바벨 벤치 프레스", "가슴", R.drawable.z_barbell_bench_press, "barbell bench press"),
        Exercise("덤벨 벤치 프레스", "가슴", R.drawable.z_dumbbell_bench_press, "dumbbell bench press"),
        Exercise("딥스", "가슴", R.drawable.z_chest_dips, "chest dips"),
        Exercise("덤벨 슈러그", "승모근", R.drawable.z_dumbbell_shrug, "dumbbell shrug"),
        Exercise("바벨 슈러그", "승모근", R.drawable.z_barbell_shrug, "barbell shrug"),
        Exercise("레버 슈러그", "승모근", R.drawable.z_lever_shrug, "lever shrug"),
        Exercise("케이블 풀다운", "등", R.drawable.z_cable_pulldown, "cable pulldown"),
        Exercise("풀업", "등", R.drawable.z_pull_up, "pull up"),
        Exercise("케이블 시티드 로우", "등", R.drawable.z_cable_seated_row, "cable seated row"),
        Exercise("레버 피쳐 컬", "이두", R.drawable.z_lever_preacher_curl, "lever preacher curl"),
        Exercise("바벨 컬", "이두", R.drawable.z_barbell_drag_curl, "barbell drag curl"),
        Exercise("덤벨 컬", "이두", R.drawable.z_dumbbell_biceps_curl, "dumbbell biceps curl"),
        Exercise("덤벨 시티드 트라이셉스 익스텐션", "삼두", R.drawable.z_dumbbell_seated_triceps_extension, "dumbbell seated triceps extension"),
        Exercise("케이블 트라이셉스 푸쉬다운", "삼두", R.drawable.z_cable_triceps_pushdown, "cable triceps pushdown"),
        Exercise("바벨 클로우스 그립 벤치프레스", "삼두", R.drawable.z_barbell_close_grip_bench_press, "barbell close grip bench press"),
        Exercise("레버 레그 익스텐션", "허벅지", R.drawable.z_lever_leg_extension, "lever leg extension"),
        Exercise("레그프레스", "허벅지", R.drawable.z_leg_press, "leg press"),
        Exercise("스쿼트", "허벅지", R.drawable.z_squat, "squat"),
        Exercise("스미스 카프 레이즈", "종아리", R.drawable.z_smith_calf_raise, "smith calf raise"),
        Exercise("레버 스탠딩 카프 레이즈", "종아리", R.drawable.z_lever_standing_calf_raise, "lever standing calf raise"),
        Exercise("덤벨 스탠딩 카프 레이즈", "종아리", R.drawable.z_dumbbell_standing_calf_raise, "dumbbell standing calf raise"),
        Exercise("스미스 시티드 숄더 프레스", "어깨", R.drawable.z_smith_seated_shoulder_press, "smith seated shoulder press"),
        Exercise("덤벨 프론트 레이즈", "어깨", R.drawable.z_dumbbell_front_raise, "dumbbell front raise"),
        Exercise("덤벨 레터럴 레이즈", "어깨", R.drawable.z_dumbbell_lateral_raise, "dumbbell lateral raise"),
        Exercise("싯업", "복근", R.drawable.z_sit_ups, "sit ups"),
        Exercise("버티컬 레그 레이즈", "복근", R.drawable.z_vertical_leg_raise, "vertical leg raise"),
        Exercise("트위스팅 크런치", "복근", R.drawable.z_twisting_crunch, "twisting crunch")
        )
    val (searchText, setSearchText) = remember {
        mutableStateOf("")
    }
    var selectedMusclePart by remember { mutableStateOf("전체") }
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
            val filteredExercises = exerciseArray
                .filter { it.main_part == selectedMusclePart || selectedMusclePart == "전체" }
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
                            imageModel = exercise.image,
                            contentDescription = "GIF Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(60.dp).width(60.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exercise.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = exercise.main_part)
                        }
                        Icon(
                            imageVector = if (isSelected) Icons.Outlined.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Localized description"
                        )
                        IconButton(onClick = { navController.navigate("explainexercise/${exercise.image}") }) {
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
    val (name, setName) = remember { mutableStateOf("") }
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            TextField(value = name, onValueChange = setName )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
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