package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

class Exercise(){}
@Composable
fun AddExerciseScreen(navController: NavHostController) {
    val (text1, setValue1) = remember {
        mutableStateOf("")
    }
    val exerciseList = listOf("전체", "가슴", "등", "복근", "삼두", "승모근", "어깨", "이두", "종아리", "허벅지")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        // slide_list_view
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(60.dp)
                )
                .padding(16.dp)

        ) {
            Text(text = "searchbox")
        }
        Spacer(modifier = Modifier.height(16.dp))
        // muscle-part
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(exerciseList.size) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(vertical = 5.dp, horizontal = 5.dp)
                ) {
                    Text(text = exerciseList[index])
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        //exercise-list
        LazyColumn {
            items(exerciseList) { exercise ->
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
                    Column {
                        Text(text = exercise)
                        Text(text = "exercise details")
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