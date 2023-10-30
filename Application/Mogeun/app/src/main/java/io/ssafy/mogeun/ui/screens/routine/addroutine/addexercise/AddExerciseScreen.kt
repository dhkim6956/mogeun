package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(navController: NavHostController) {
    val musclePartList = listOf("전체", "가슴", "등", "복근", "삼두", "승모근", "어깨", "이두", "종아리", "허벅지")
    data class Exercise(val name: String, val musclePart: String, val image: Int)
    val exerciseArray = arrayOf(
        Exercise("Barbell Bench Press", "가슴", R.drawable.z_barbell_bench_press),
        Exercise("Dumbbell Bench Press", "가슴", R.drawable.z_dumbbell_bench_press),
        Exercise("Chest Dips", "가슴", R.drawable.z_chest_dips),
        Exercise("Dumbbell Shrug", "승모근", R.drawable.z_dumbbell_shrug),
        Exercise("Barbell Shrug", "승모근", R.drawable.z_barbell_shrug),
        Exercise("Lever Shrug", "승모근", R.drawable.z_lever_shrug),
        Exercise("Cable Pulldown", "등", R.drawable.z_cable_pulldown),
        Exercise("Pull up", "등", R.drawable.z_pull_up),
        Exercise("Cable Seated Row", "등", R.drawable.z_cable_seated_row),
        Exercise("Lever Preacher Curl", "이두", R.drawable.z_lever_preacher_curl),
        Exercise("Barbell Drag Curl", "이두", R.drawable.z_barbell_drag_curl),
        Exercise("Dumbbell Biceps Curl", "이두", R.drawable.z_dumbbell_biceps_curl),
        Exercise("Dumbbell Seated Triceps Extension", "삼두", R.drawable.z_dumbbell_seated_triceps_extension),
        Exercise("Cable Triceps Pushdown", "삼두", R.drawable.z_cable_triceps_pushdown),
        Exercise("Barbell Close Grip Bench Press", "삼두", R.drawable.z_barbell_close_grip_bench_press),
        Exercise("Lever Leg Extension", "허벅지", R.drawable.z_lever_leg_extension),
        Exercise("Leg Press", "허벅지", R.drawable.z_leg_press),
        Exercise("Squat", "허벅지", R.drawable.z_squat),
        Exercise("Smith Calf Raise", "종아리", R.drawable.z_smith_calf_raise),
        Exercise("Lever Standing Calf Raise", "종아리", R.drawable.z_lever_standing_calf_raise),
        Exercise("Dumbbell Standing Calf Raise", "종아리", R.drawable.z_dumbbell_standing_calf_raise),
        Exercise("Smith Seated Shoulder Press", "어깨", R.drawable.z_smith_seated_shoulder_press),
        Exercise("Dumbbell Front Raise", "어깨", R.drawable.z_dumbbell_front_raise),
        Exercise("Dumbbell Lateral Raise", "어깨", R.drawable.z_dumbbell_lateral_raise),
        Exercise("Sit-ups", "복근", R.drawable.z_sit_ups),
        Exercise("Vertical Leg Raise", "복근", R.drawable.z_vertical_leg_raise),
        Exercise("Twisting Crunch", "복근", R.drawable.z_twisting_crunch)
        )
    val (text1, setValue1) = remember {
        mutableStateOf("")
    }
    var selectedmusclePart by remember { mutableStateOf("전체") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)

    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = text1,
            onValueChange = setValue1,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
        )

        // slide_list_view
        Spacer(modifier = Modifier.height(16.dp))

        // muscle-part
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(musclePartList) { musclePart ->
                FilterChip(
                    selected = selectedmusclePart == musclePart,
                    onClick = { selectedmusclePart = musclePart },
                    label = { Text(musclePart) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        //exercise-list
        LazyColumn {
            val filteredExercises = if(selectedmusclePart == "전체"){
                exerciseArray.toList()
            }else{
                exerciseArray.filter { it.musclePart == selectedmusclePart }
            }
            items(filteredExercises) { exercise ->
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
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = exercise.name)
                            Text(text = exercise.musclePart)
                        }
                        GlideImage(
                            imageModel = exercise.image,
                            contentDescription = "GIF Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(60.dp).width(60.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))  // 박스 사이에 8dp 높이의 공간 추가
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd
        ) {
            Button(
                onClick = { navController.navigate("addexercise") }
            ) {
                Text("운동 추가")
            }
        }
    }
}