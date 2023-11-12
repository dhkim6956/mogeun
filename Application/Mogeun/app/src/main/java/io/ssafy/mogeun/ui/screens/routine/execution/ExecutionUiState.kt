package io.ssafy.mogeun.ui.screens.routine.execution

import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.data.bluetooth.BluetoothDevice
import io.ssafy.mogeun.model.ListMyExerciseResponse

data class EmgUiState (
    val emg1: Emg? = null,
    val emg2: Emg? = null,
)

data class RoutineState(
    val planList: ListMyExerciseResponse? = null,
    val showBottomSheet: Boolean = false
)