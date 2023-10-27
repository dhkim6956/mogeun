package io.ssafy.mogeun.ui.screens.routine

import android.content.Context
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R

@Composable
fun RoutineScreen(navController: NavHostController) {
//    val context = LocalContext.current
//    val test = LocalContext.current.resources.getIdentifier("chest", "string", context.packageName)
    val openAlertDialog = remember { mutableStateOf(false) }
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
                ) {
                    Text(
                        text = "모근이님 안녕하세요.",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 24.sp
                    )
                }
                Button(
                    onClick = { /*TODO*/ },
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
                    Text(text = "32.kg")
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
                    Text(text = "14.2%")
                }
            }
        }
        Column(modifier = Modifier
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(top = 20.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "루틴명 : 밀기", modifier = Modifier.padding(start = 40.dp, top = 10.dp), fontSize = 24.sp)
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.symbol_more),
                        contentDescription = "dotdotdot",
                        contentScale = ContentScale.Crop,
                    )
                }

            }
            Row {
                Column(modifier = Modifier.width(200.dp)) {
                    Text(text = "- 덤벨 푸쉬업")
                    Text(text = "- 바벨 벤치 프레스")
                    Text(text = "- 덤벨 플라이")
                }
                Column {
                    Text(text = "사용 근육")
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.chest),
                            contentDescription = "chest",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(50.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.triceps),
                            contentDescription = "triceps",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(50.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Image(
                            painter = painterResource(id = R.drawable.biceps),
                            contentDescription = "biceps",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(50.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { /*TODO*/ },
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.scrim),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(text = "루틴시작", color = MaterialTheme.colorScheme.scrim)
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(end = 30.dp, bottom = 30.dp), contentAlignment = Alignment.BottomEnd) {
        Button(
            onClick = { openAlertDialog.value = true },
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
    when {
        // ...
        openAlertDialog.value -> {
            AlertDialogExample(
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
                    navController.navigate("addroutine")
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