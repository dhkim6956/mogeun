package io.ssafy.mogeun.ui.screens.routine.execution

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.EmgRepository
import io.ssafy.mogeun.data.SetRepository
import io.ssafy.mogeun.data.bluetooth.BluetoothController
import io.ssafy.mogeun.model.SetRequest
import io.ssafy.mogeun.model.SetResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExecutionViewModel(
    private val setRepository: SetRepository,
    private val emgRepository: EmgRepository,
    private val bluetoothController: BluetoothController
): ViewModel() {
    private val _getSetSuccess = MutableStateFlow(false)
    val getSetSuccess: StateFlow<Boolean> = _getSetSuccess.asStateFlow()
    fun getSet() {
        lateinit var ret: SetResponse
        viewModelScope.launch {
            ret = setRepository.getSet(SetRequest(1, 1, 1, 43.7F, 35, 10, 8, 12.6F, "2023-11-03T20:03:42", "2023-11-03T20:10:42"))
            Log.d("getSet", "$ret")
            if(ret.message == "SUCCESS") {
                _getSetSuccess.value = true
            }
        }
    }
}