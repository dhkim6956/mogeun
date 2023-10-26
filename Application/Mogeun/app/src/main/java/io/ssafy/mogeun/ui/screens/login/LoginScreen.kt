package io.ssafy.mogeun.ui.screens.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {

    val (text1, setValue1) = remember {
        mutableStateOf("")
    }
    val (text2, setValue2) = remember {
        mutableStateOf("")
    }

    Column {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.padding(end = 120.dp).padding(bottom = 100.dp).height(100.dp)
            )
            Text(text = "모근", fontSize = 48.sp)
        }
        Column(modifier = Modifier.padding(28.dp)) {
            Text(text = "아이디")

            TextField(
                value = text1,
                onValueChange = setValue1,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "비밀번호")

            TextField(
                value = text2,
                onValueChange = setValue2,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("routine") },
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(text = "로그인")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "회원이 아니신가요?",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {navController.navigate("signup")},
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(text = "회원가입")
            }
        }
    }
}
