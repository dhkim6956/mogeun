package io.ssafy.mogeun.ui.screens.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R

@Composable
fun SignupScreen(navController: NavHostController) {
    val inputForm = remember { mutableIntStateOf(1) }
    val firstText = remember { mutableStateOf("회원정보를") }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = firstText.value,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontSize = 24.sp
                )
                Text(
                    text = "입력해주세요",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    fontSize = 24.sp
                )
            }
        }
        when (inputForm.value) {
            1 -> Essential(inputForm, firstText, navController) // 여성 버튼 클릭 시 firstText 값을 전달
            2 -> Inbody(inputForm, firstText, navController)
        }
    }
}

@Composable
fun Essential(inputForm: MutableIntState, firstText: MutableState<String>, navController: NavHostController) {
    val (id, setId) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val (checkingPassword, setCheckingPassword) = remember { mutableStateOf("") }
    val (nickname, setNickname) = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(28.dp)) {
        Text(text = "아이디")
        Row {
            TextField(
                value = id,
                onValueChange = setId,
                modifier = Modifier.width(220.dp),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.width(100.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "중복확인")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "비밀 번호")
        TextField(
            value = password,
            onValueChange = setPassword,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "비밀 번호 확인")
        TextField(
            value = checkingPassword,
            onValueChange = setCheckingPassword,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "닉네임")
        TextField(
            value = nickname,
            onValueChange = setNickname,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { /*TODO*/ }, shape = RoundedCornerShape(10.dp)) {
                Text(text = "남성")
            }
            Spacer(modifier = Modifier.width(48.dp))
            Button(
                onClick = {

                    },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "여성")
            }
        }
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Image(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "back",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.height(50.dp)
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            inputForm.value = 2
                            firstText.value = "인바디를"
                            },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Text(
                            text = "회원가입",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(
                                                    start = 20.dp,
                                                    end = 20.dp
                                                )
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = ""
        )
    }
}

@Composable
fun Inbody(inputForm: MutableIntState, firstText: MutableState<String>, navController: NavHostController) {
    val (height, setHeight) = remember { mutableStateOf("") }
    val (weight, setWeight) = remember { mutableStateOf("") }
    val (muscleMass, setMuscleMass) = remember { mutableStateOf("") }
    val (bodyFat, setBodyFat) = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(28.dp)) {
        Text(text = "키")
        TextField(
            value = height,
            onValueChange = setHeight,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "몸무게")
        TextField(
            value = weight,
            onValueChange = setWeight,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "골격근량")
        TextField(
            value = muscleMass,
            onValueChange = setMuscleMass,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "체지방")
        TextField(
            value = bodyFat,
            onValueChange = setBodyFat,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
    }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                actions = {
                    IconButton(
                        onClick = {navController.navigate("login")},
                        modifier = Modifier
                            .width(104.dp)
                            .height(36.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(30.dp)),
                    ) {
                        Text(text = "건너뛰기", fontSize = 16.sp)
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {navController.navigate("login")  },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Text(
                            text = "회원가입",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(
                                                    start = 20.dp,
                                                    end = 20.dp
                                                )
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Text(
            modifier = Modifier.padding(innerPadding),
            text = ""
        )
    }
}
