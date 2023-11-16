package io.ssafy.mogeun.ui.screens.setting.setting

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@OptIn(ExperimentalFoundationApi::class)
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
            snackbarHostState.showSnackbar("아이디와 비밀번호를 정확하게 입력해주세요.")
        }
    }

    val userMenus: List<MenuItemInfo> = listOf(
        MenuItemInfo(
            "내 정보 수정",
            "개인 정보를 수정합니다.",
            Icons.Default.ManageAccounts,
            {navController.navigate("User")},
            Color(0xFFFFF0C9),
            Position.Top
        ),
        MenuItemInfo(
            "로그아웃",
            "서비스에서 로그아웃합니다.",
            Icons.Default.Logout,
            {
                viewModel.deleteUserKey()
                navController.navigate("Splash")
            },
            Color(0xFFFFE0C9),
            Position.Mid
        ),
        MenuItemInfo(
            "회원탈퇴",
            "서비스를 그만 이용하고 싶어요.",
            Icons.Default.GroupOff,
            {openAlertDialog.value = true},
            Color(0xFFFFC9C9),
            Position.Bot
        )
    )

    val serviceMenus: List<MenuItemInfo> = listOf(
        MenuItemInfo(
            "기기연동",
            "디바이스를 연결합니다.",
            Icons.Default.BluetoothSearching,
            {navController.navigate("Connection")},
            Color(0xFFC9E2FF),
            Position.Single
        )
    )

    val appMenus: List<MenuItemInfo> = listOf(
        MenuItemInfo(
            "Google Play 평가",
            "리뷰를 남겨주세요.",
            Icons.Default.RateReview,
            {},
            Color(0xFFEAC9FF),
            Position.Top
        ),
        MenuItemInfo(
            "앱 정보",
            "버전, Contact...",
            Icons.Default.Info,
            {},
            Color(0xFFFFC9E3),
            Position.Bot
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        stickyHeader {
            LazyHeader("회원정보")
        }
        items(userMenus) {
            LazyList(menu = it)
        }
        stickyHeader {
            LazyHeader("서비스 연동")
        }
        items(serviceMenus) {
            LazyList(menu = it)
        }
        stickyHeader {
            LazyHeader("앱 정보")
        }
        items(appMenus) {
            LazyList(menu = it)
        }
    }



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
}

enum class Position() {
    Top, Mid, Bot, Single
}

data class MenuItemInfo(
    val title: String,
    val description: String,
    val vector: ImageVector,
    val onClick: () -> Unit,
    val color: Color,
    val position: Position,
)

@Composable
fun LazyHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color(0xFFF7F7F7))
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(title, fontStyle = FontStyle.Italic, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun LazyList(menu: MenuItemInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .wrapContentHeight()
            .shadow(
                if (menu.position == Position.Bot || menu.position == Position.Single) 4.dp else 0.dp,
                shape = if (menu.position == Position.Bot) {
                    RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                } else if (menu.position == Position.Single) {
                    RoundedCornerShape(20.dp)
                } else {
                    RoundedCornerShape(0.dp)
                }
            )
            .clip(
                shape = when (menu.position) {
                    Position.Single -> RoundedCornerShape(20.dp)
                    Position.Top -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    Position.Bot -> RoundedCornerShape(
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )

                    Position.Mid -> RoundedCornerShape(0.dp)
                }
            )
            .clickable {
                menu.onClick()
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(color = Color(0xFFFFF4ED))
        ) {
            if(menu.position != Position.Top && menu.position != Position.Single)
                Divider(
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                )
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .background(color = menu.color)
                            .fillMaxSize()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize(0.7f)
                        ) {
                            Image(
                                imageVector = menu.vector,
                                contentDescription = menu.title
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(text = menu.title, fontWeight = FontWeight.Bold)
                    Text(text = menu.description)
                }

            }
            if(menu.position != Position.Bot && menu.position != Position.Single)
                Divider(
                    thickness = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                )
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
                Text("탈퇴하기")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("취소")
            }
        }
    )
}