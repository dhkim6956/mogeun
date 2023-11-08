package io.ssafy.mogeun.ui.screens.setting.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.screens.routine.searchRoutine.RoutineViewModel

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = viewModel(factory = SettingViewModel.Factory),
    navController: NavController
) {
    Column(modifier = Modifier.padding(32.dp)) {
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
                .clickable{ navController.navigate("User") }
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
                .clickable{
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
            ) {
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
