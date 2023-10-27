package io.ssafy.mogeun.ui.screens.record

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.ssafy.mogeun.R
import io.ssafy.mogeun.ui.Screen

data class routineInfo(
    val title: String,
    val detail: String
)

@Composable
fun RecordDetailScreen(navController: NavHostController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp
            )
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoutineInfoCard()
        RoutineGraphIconCard()
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(listOf(1, 2, 3)) {index, item ->
                RoutineExerciseCard()
            }
        }
    }
}

@Preview
@Composable
fun RecordDetailScreenPreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 30.dp
            )
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoutineInfoCard()
        RoutineGraphIconCard()
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            itemsIndexed(listOf(1, 2, 3)) {index, item ->
                RoutineExerciseCard()
            }
        }
    }
}

@Composable
fun RoutineInfoCard() {
    var routineInfoList: List<routineInfo> = listOf(routineInfo("소모 칼로리", "72kcal"), routineInfo("수행한 세트", "22set"), routineInfo("운동한 시간", "71분"))
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("test", fontSize = 24.sp)
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    )
            ) {
                routineInfoList.forEach{
                    RoutineInfo(it)
                }
                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }
}

@Composable
fun RoutineInfo(
    routineInfo: routineInfo
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 5.dp
            )
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2f,
                )
            }
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(routineInfo.title)
        Text(routineInfo.detail)
    }
}

@Composable
fun RoutineGraphIconCard() {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(
                vertical = 20.dp
            )
    ) {
        Row () {

        }
    }
}

@Composable
fun RoutineExerciseCard() {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                vertical = 10.dp,
                horizontal = 10.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column () {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                )
                Text("test")
            }
            Column () {
                Surface () {

                }
                Text(text = "자세히 보기", color = MaterialTheme.colorScheme.secondary)
//            TextButton(onClick = { navController.navigate("recorddetail") }) {
//                Text(text = "자세히 보기", color = MaterialTheme.colorScheme.secondary)
//            }
            }
        }
    }
}

@Composable
fun SetWeightIcon() {

}
