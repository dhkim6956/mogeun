package io.ssafy.mogeun.ui.screens.setting.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = viewModel(factory = SettingViewModel.Factory),
    navController: NavHostController,
    snackbarHostState: SnackbarHostState
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    LaunchedEffect(viewModel.deleteUserSuccess) {
        if (viewModel.deleteUserSuccess == true) {
            navController.navigate("Splash")
            snackbarHostState.showSnackbar("탈퇴가 완료되었습니다.")
        }
    }
    LaunchedEffect(viewModel.errorDeleteUser) {
        if (viewModel.errorDeleteUser == true) {
            snackbarHostState.showSnackbar("잘못된 정보입니다.")
        }
    }
    Column(
        modifier = Modifier
            .padding(32.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column {
            Text(
                text = "회원정보",
                fontSize = 20.sp,
                )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onErrorContainer,
                shape = RoundedCornerShape(15.dp)
            )
        ) {
            Row(modifier = Modifier
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f,
                    )
                }
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navController.navigate("User") }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                    ) {
                    Image(
                        painter = painterResource(id = R.drawable.user_information_update),
                        contentDescription = "user_information_update",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(32.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "내 정보 수정")
                    Text(
                        text = "개인 정보를 수정 합니다.",
                        color = Color.Gray
                        )
                }
            }
            Row(modifier = Modifier
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f,
                    )
                }
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    viewModel.deleteUserKey()
                    navController.navigate("Splash")
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_logout_24),
                        contentDescription = "baseline_logout_24",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(32.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "로그아웃")
                    Text(
                        text = "서비스에서 로그아웃 합니다.",
                        color = Color.Gray
                    )
                }
            }
            Row(modifier = Modifier
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f,
                    )
                }
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    openAlertDialog.value = true
                }
            ) {
                when {
                    openAlertDialog.value -> {
                        AlertDialogExample(
                            onDismissRequest = { openAlertDialog.value = false },
                            onConfirmation = {
                                viewModel.deleteUser()
                                openAlertDialog.value = false
                            },
                            dialogTitle = "회원 정보를 입력해 주세요.",
                            icon = Icons.Default.Info
                        )
                    }
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user_delete),
                        contentDescription = "user_delete",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(32.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "회원 탈퇴")
                    Text(
                        text = "서비스를 그만 이용하고 싶어요.",
                        color = Color.Gray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column {
            Text(
                text = "서비스 연동",
                fontSize = 20.sp,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onErrorContainer,
                shape = RoundedCornerShape(15.dp)
            )
        ) {
            Row(modifier = Modifier
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f,
                    )
                }
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    navController.navigate("Connection")
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bluetooth),
                        contentDescription = "bluetooth",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(32.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "기기 연동")
                    Text(
                        text = "디바이스를 연결합니다.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
            Row(modifier = Modifier
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f,
                    )
                }
                .fillMaxWidth()
                .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.run),
                        contentDescription = "run",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(32.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "웨어러블 기기 연동")
                    Text(
                        text = "삼성 헬스 서비스와 연동합니다.",
                        color = Color.Gray
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column {
            Text(
                text = "앱 정보",
                fontSize = 20.sp,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onErrorContainer,
                shape = RoundedCornerShape(15.dp)
            )
        ) {
            Row(modifier = Modifier
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f,
                    )
                }
                .fillMaxWidth()
                .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.review),
                        contentDescription = "review",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(32.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "Google Play 평가")
                    Text(
                        text = "리뷰를 남겨주세요.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
            Row(modifier = Modifier
                .drawWithContent {
                    drawContent()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2f,
                    )
                }
                .fillMaxWidth()
                .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(48.dp)
                        .width(48.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = "info",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(32.dp),
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "앱 정보")
                    Text(
                        text = "버전, Contact ...",
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    icon: ImageVector,
) {
    val viewModel: SettingViewModel = viewModel(factory = SettingViewModel.Factory)
    val keyboardController = LocalSoftwareKeyboardController.current
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                Text(text = "아이디")
                TextField(
                    value = viewModel.username,
                    onValueChange = { viewModel.updateId(it) },
                    label = {  },
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "비밀번호")
                TextField(
                    value = viewModel.pw,
                    onValueChange = { viewModel.updatePw(it) },
                    label = {  },
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
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