package io.ssafy.mogeun.ui.screens.routine.execution.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.ssafy.mogeun.ui.theme.MogeunTheme

@Composable
fun RoutineProgress() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoutineProgress() {
    MogeunTheme {
        RoutineProgress()
    }
}