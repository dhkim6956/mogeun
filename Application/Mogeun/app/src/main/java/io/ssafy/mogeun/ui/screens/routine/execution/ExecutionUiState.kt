package io.ssafy.mogeun.ui.screens.routine.execution

import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.data.bluetooth.BluetoothDevice

data class EmgUiState (
    val emg1: Emg? = null,
    val emg2: Emg? = null,
)