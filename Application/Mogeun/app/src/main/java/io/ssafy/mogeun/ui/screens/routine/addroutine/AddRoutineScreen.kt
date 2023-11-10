package io.ssafy.mogeun.ui.screens.routine.addroutine

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ssafy.mogeun.model.ListMyExerciseResponseData
import io.ssafy.mogeun.model.MyExerciseResponseData
import io.ssafy.mogeun.ui.AppViewModelProvider
import io.ssafy.mogeun.ui.screens.routine.searchRoutine.RoutineList


@Composable
fun AddRoutineScreen(
    navController: NavHostController,
    viewModel: AddRoutineViewModel = viewModel(factory = AddRoutineViewModel.Factory),
    routineKey: Int?
) {
    LaunchedEffect(Unit){
        viewModel.getUserKey()
        viewModel.listMyExercise(routineKey)
    }
//    val exercises = viewModel.exerciseList
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Log.d("routineKey", "${routineKey}")
        LazyColumn {
            // slide_list_view
            viewModel.exerciseList?.let {
                itemsIndexed(it) { index, item ->
                    ExerciseList(item)
                }
            }
//            items(exercises) { exercise ->
//                ExerciseList()
//            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
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

@Composable
fun ExerciseList(item: ListMyExerciseResponseData) {
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
            Text(text = "${item.name}")
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}