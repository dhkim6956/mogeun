package io.ssafy.mogeun.ui.screens.signup

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R

@Composable
fun SignupScreen(navController: NavHostController) {

    val (text1, setValue1) = remember {
        mutableStateOf("")
    }
    val (text2, setValue2) = remember {
        mutableStateOf("")
    }
    val (text3, setValue3) = remember {
        mutableStateOf("")
    }
    val (text4, setValue4) = remember {
        mutableStateOf("")
    }

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
                    text = "회원정보를",
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
        Column(modifier = Modifier.padding(28.dp)) {
            Text(text = "아이디")
            Row {
                TextField(
                    value = text1,
                    onValueChange = setValue1,
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
                value = text2,
                onValueChange = setValue2,
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = "비밀 번호 확인")
            TextField(
                value = text3,
                onValueChange = setValue3,
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = "닉네임")
            TextField(
                value = text4,
                onValueChange = setValue4,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center // 가로로 가운데 정렬
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "남성")
                }
                Spacer(modifier = Modifier.width(16.dp)) // 간격 조절
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "여성")
                }
            }
        }
    }
}
