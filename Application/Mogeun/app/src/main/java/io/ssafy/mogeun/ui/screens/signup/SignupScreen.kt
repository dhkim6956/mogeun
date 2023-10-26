package io.ssafy.mogeun.ui.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

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
            1 -> Essential(inputForm, firstText) // 여성 버튼 클릭 시 firstText 값을 전달
            2 -> Inbody(inputForm)
        }
    }
}

@Composable
fun Essential(inputForm: MutableIntState, firstText: MutableState<String>) {
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
                modifier = Modifier.width(220.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.width(100.dp)
            ) {
                Text(text = "중복확인")
            }
        }
        Text(text = "비밀 번호")
        TextField(
            value = password,
            onValueChange = setPassword,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "비밀 번호 확인")
        TextField(
            value = checkingPassword,
            onValueChange = setCheckingPassword,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "닉네임")
        TextField(
            value = nickname,
            onValueChange = setNickname,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "남성")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                inputForm.value = 2
                firstText.value = "인바디를"
            }) {
                Text(text = "여성")
            }
        }
    }
}

@Composable
fun Inbody(inputForm: MutableIntState) {
    val (height, setHeight) = remember { mutableStateOf("") }
    val (weight, setWeight) = remember { mutableStateOf("") }
    val (muscleMass, setMuscleMass) = remember { mutableStateOf("") }
    val (bodyFat, setBodyFat) = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(28.dp)) {
        Text(text = "키")
        TextField(
            value = height,
            onValueChange = setHeight,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "몸무게")
        TextField(
            value = weight,
            onValueChange = setWeight,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "골격근량")
        TextField(
            value = muscleMass,
            onValueChange = setMuscleMass,
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "체지방")
        TextField(
            value = bodyFat,
            onValueChange = setBodyFat,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = "남성")
        }
    }
}
