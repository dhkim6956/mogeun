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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory),navController: NavHostController) {

    val signInSuccess by viewModel.signInSuccess.collectAsState()
    if(signInSuccess) {
        navController.navigate("Routine")
    }
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)

    Column {
        Box(
            modifier = Modifier
                .height(330.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 120.dp)
                    .padding(bottom = 100.dp)
                    .height(150.dp)
            )
            Text(text = "모근", fontSize = 60.sp, color = MaterialTheme.colorScheme.primary)
        }
        Column(modifier = Modifier.padding(28.dp)) {
            Text(text = "아이디")
            TextField(
                value = viewModel.id,
                onValueChange = { viewModel.updateText1(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "비밀번호")
            TextField(
                value = viewModel.pwd,
                onValueChange = { viewModel.updateText2(it) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    val ret = viewModel.signIn()
                    Log.d("signIn", "$ret") }
                ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "로그인", color = MaterialTheme.colorScheme.scrim)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "회원가입", color = MaterialTheme.colorScheme.scrim)
            }
        }
    }
}
