package io.ssafy.mogeun.ui.screens.routine.execution.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import io.ssafy.mogeun.ui.theme.MogeunTheme

@Composable
fun RoutineProgress(page: Int, execCnt: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .background(Color(0x22000000))
                .padding(horizontal = 20.dp)
                .zIndex(1f)
        ) {
            Text(text = "< $page / $execCnt >", fontSize = 20.sp)
            ElevatedAssistChip(
                onClick = {  },
                label = { Text("운동 추가") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Localized description",
                    )
                },
                colors = AssistChipDefaults.elevatedAssistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    leadingIconContentColor = MaterialTheme.colorScheme.secondary
                )
            )
            Row (
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .offset(0.dp, 20.dp)
                        .wrapContentSize(unbounded = true, align = Alignment.TopEnd)
                        .shadow(12.dp, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(8.dp)
                    ) {
                        Text(text = "루틴종료", fontSize = 24.sp, textAlign = TextAlign.Center, lineHeight = 28.sp)
                    }
                }
            }
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
                .zIndex(0f)
                .padding(horizontal = 20.dp)
        ) {
            Text("진행 시간 : 71분 21초", color = MaterialTheme.colorScheme.onPrimary, fontSize = 24.sp)
        }
    }
}

@Preview(showBackground = true, widthDp = 430, heightDp = 932)
@Composable
fun PreviewRoutineProgress() {
    MogeunTheme {
        Column (Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f))
            RoutineProgress(1, 4)
        }
    }
}