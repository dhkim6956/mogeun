package io.ssafy.mogeun.ui.screens.setting.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.ui.screens.signup.SignupViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserScreen(
    viewModel: UserViewModel = viewModel(factory = UserViewModel.Factory),
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel.getUserKey()
    }
    LaunchedEffect(viewModel.userKey) {
        if (viewModel.userKey !== null) {
            viewModel.getInbody()
        }
    }
    Column {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "닉네임")
            TextField(
                value = viewModel.nickname,
                onValueChange = {
                    viewModel.updateNickname(it)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                }),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "신장")
            TextField(
                value = viewModel.height.toString(),
                onValueChange = {
                    viewModel.updateHeight(it.toDouble())
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "체중")
            TextField(
                value = viewModel.weight.toString(),
                onValueChange = {
                    viewModel.updateWeight(it.toDouble())
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "골격근량")
            TextField(
                value = viewModel.muscleMass.toString(),
                onValueChange = {
                    viewModel.updateMuscleMass(it.toDouble())
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "체지방량")
            TextField(
                value = viewModel.bodyFat.toString(),
                onValueChange = {
                    viewModel.updateBodyFat(it.toDouble())
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                viewModel.updateUser()
                navController.navigate("Routine")
            }) {
                Text(text = "변경사항 수정")
            }
        }
    }
}