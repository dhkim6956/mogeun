package io.ssafy.mogeun.ui.screens.setting.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.ssafy.mogeun.ui.screens.signup.SignupViewModel

@Composable
fun UserScreen(viewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)) {
    var heightText by remember { mutableStateOf(if(viewModel.height == null) "" else viewModel.height.toString()) }
    var weightText by remember { mutableStateOf(if(viewModel.weight == null) "" else viewModel.weight.toString()) }
    var muscleMassText by remember { mutableStateOf(if(viewModel.muscleMass == null) "" else viewModel.muscleMass.toString()) }
    var bodyFatText by remember { mutableStateOf(if(viewModel.bodyFat == null) "" else viewModel.bodyFat.toString()) }
    var nickname by remember { mutableStateOf(if(viewModel.nickname == null) "" else viewModel.nickname.toString()) }

    LaunchedEffect(Unit) {
        viewModel.getUserKey()
    }

    Column {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "닉네임")
            TextField(
                value = nickname,
                onValueChange = {
                    nickname = it
                    if (it == "") {
                        viewModel.updateNickname(null)
                    } else {
                        viewModel.updateNickname(it)
                    }

                })
            Text(text = "신장")
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
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(text = "체중")
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
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
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
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(text = "체지방량")
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
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Button(onClick = {viewModel.updateUser()}) {
                Text(text = "변경사항 수정")
            }
        }
    }
}