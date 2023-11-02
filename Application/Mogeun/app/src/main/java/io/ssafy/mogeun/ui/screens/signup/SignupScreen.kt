package io.ssafy.mogeun.ui.screens.signup

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.screens.login.LoginViewModel

@Composable
fun SignupScreen(viewModel: SignupViewModel = viewModel(factory = SignupViewModel.Factory), navController: NavHostController) {
    val inputForm = viewModel.inputForm
    val firstText = viewModel.firstText

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.primary
                ),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = firstText,
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
        when (inputForm) {
            1 -> Essential(viewModel, navController)
            2 -> Inbody(navController)
        }
    }
}

@Composable
fun Essential(viewModel: SignupViewModel = viewModel(factory = SignupViewModel.Factory), navController: NavHostController) {
    val id = viewModel.id
    val password = viewModel.password
    val checkingPassword = viewModel.checkingPassword
    val nickname = viewModel.nickname

    Column(modifier = Modifier.padding(28.dp)) {
        Text(text = "아이디")
        Row {
            TextField(
                value = id,
                onValueChange = {
                    viewModel.updateId(it)
                    viewModel.updateCheckEmail(0)
                },
                modifier = Modifier.width(220.dp),
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    val ret = viewModel.dupEmail()
                    Log.d("signIn", "$ret")
                },
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
            onValueChange = viewModel::updatePassword,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "비밀 번호 확인")
        TextField(
            value = checkingPassword,
            onValueChange = viewModel::updateCheckingPassword,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "닉네임")
        TextField(
            value = nickname,
            onValueChange = viewModel::updateNickname,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Preview_MultipleRadioButtons()
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
                            if(viewModel.checkEmail == 1 && viewModel.password == viewModel.checkingPassword && viewModel.nickname !== "" && viewModel.selectedGender !== "") {
                                viewModel.updateInputForm(2)
                                viewModel.updateFirstText("인바디를")
                            }
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
fun Inbody(
    navController: NavHostController,
    viewModel: SignupViewModel = viewModel(factory = SignupViewModel.Factory)
) {
    var heightText by remember { mutableStateOf(if(viewModel.height == null) "" else viewModel.height.toString()) }
    var weightText by remember { mutableStateOf(if(viewModel.weight == null) "" else viewModel.weight.toString()) }
    var muscleMassText by remember { mutableStateOf(if(viewModel.muscleMass == null) "" else viewModel.muscleMass.toString()) }
    var bodyFatText by remember { mutableStateOf(if(viewModel.bodyFat == null) "" else viewModel.bodyFat.toString()) }

    Column(modifier = Modifier.padding(28.dp)) {
        Text(text = "키")
        TextField(
            value = heightText,
            onValueChange = {
                heightText = it
                if (it == "") {
                    viewModel.updateHeight(null)
                } else {
                    viewModel.updateHeight(it.toDouble())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "몸무게")
        TextField(
            value = weightText,
            onValueChange = {
                weightText = it
                if (it == "") {
                    viewModel.updateWeight(null)
                } else {
                    viewModel.updateWeight(it.toDouble())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "골격근량")
        TextField(
            value = muscleMassText,
            onValueChange = {
                muscleMassText = it
                if (it == "") {
                    viewModel.updateMuscleMass(null)
                } else {
                    viewModel.updateMuscleMass(it.toDouble())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "체지방")
        TextField(
            value = bodyFatText,
            onValueChange = {
                bodyFatText = it
                if (it == "") {
                    viewModel.updateBodyFat(null)
                } else {
                    viewModel.updateBodyFat(it.toDouble())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
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
                        onClick = {
                            val ret = viewModel.signUp()
                            Log.d("signUp", "$ret")
                            navController.navigate("login")
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
fun Preview_MultipleRadioButtons(viewModel: SignupViewModel = viewModel(factory = SignupViewModel.Factory)) {
    val selectedGender = viewModel.selectedGender
    val isSelectedItem: (String) -> Boolean = { selectedGender == it }
    val onChangeState: (String) -> Unit = { viewModel.updateSelectedGender(it) }
    val items = listOf("m", "f")
    Column(Modifier.padding(8.dp)) {
        Text(text = "성별을 선택해주세요 : ${selectedGender.ifEmpty { "NONE" }}")
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectable(
                        selected = isSelectedItem(item),
                        onClick = { onChangeState(item) },
                        role = Role.RadioButton
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = isSelectedItem(item),
                    onClick = null
                )
                Text(
                    text = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}