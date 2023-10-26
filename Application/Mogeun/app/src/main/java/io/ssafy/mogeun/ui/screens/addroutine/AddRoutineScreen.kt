package io.ssafy.mogeun.ui.screens.addroutine

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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



@Composable
fun AddRoutineScreen(navController: NavHostController) {
    val exerciseList = listOf("Barbell Bench Press", "Dumbbell Bench Press", "Dumbbell Bench Press")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        ) {
            // slide_list_view
            for (i in 0 until exerciseList.size) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(88.dp)
                        .border(2.dp, Color.Black)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(text = exerciseList[i])
                        Text(text = "hihi")
                    }
                }
                if (i < exerciseList.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))  // 박스 사이에 8dp 높이의 공간 추가
                }
            }
        }
}
