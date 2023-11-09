package io.ssafy.mogeun.ui.screens.routine.searchRoutine

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import io.ssafy.mogeun.model.GetRoutineListResponseBody
import io.ssafy.mogeun.ui.Screen
import io.ssafy.mogeun.ui.screens.signup.SignupViewModel

@Composable
fun RoutineScreen(
    viewModel: RoutineViewModel = viewModel(factory = RoutineViewModel.Factory),
    navController: NavHostController
) {
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
                    RoutineList(navController, item)
                }
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(start = 30.dp, bottom = 30.dp), contentAlignment = Alignment.BottomStart) {
        Button(
            onClick = { navController.navigate("addexercise") },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            shape = RoundedCornerShape(10.dp)
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
@Composable
fun RoutineList(
    navController: NavHostController,
    routine: GetRoutineListResponseBody
) {
    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.onPrimary)
        .padding(top = 20.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = routine.name,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                fontSize = 24.sp,
                maxLines = 1
            )
            Button(
                onClick = { navController.navigate("addroutine/${routine.routineKey}") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.symbol_more),
                    contentDescription = "dotdotdot",
                    contentScale = ContentScale.Crop,
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
                onClick = { navController.navigate(Screen.Execution.route) },
                shape = RoundedCornerShape(12.dp)
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
            modifier = Modifier.height(32.dp).width(32.dp)
        )
    }
    Spacer(modifier = Modifier.width(10.dp))
}