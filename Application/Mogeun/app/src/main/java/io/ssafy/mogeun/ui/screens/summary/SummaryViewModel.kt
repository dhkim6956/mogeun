package io.ssafy.mogeun.ui.screens.summary

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.SummaryRepository
import io.ssafy.mogeun.model.BodyInfo
import io.ssafy.mogeun.model.BodyInfoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SummaryViewModel(
    private val summaryRepository: SummaryRepository,
    private val keyRepository: KeyRepository
): ViewModel() {
    var userKey by mutableStateOf<Int?>(null)

    private val _summaryBodyInfoSuccess = MutableStateFlow(false)
    val summaryBodyInfoSuccess: StateFlow<Boolean> = _summaryBodyInfoSuccess.asStateFlow()
    var summaryBodyInfo by mutableStateOf<BodyInfo?>(null)

    private fun updateUserKey(update: Int?) {
        userKey= update
    }

    private fun getUserKey() {
        viewModelScope.launch {
            val key = keyRepository.getKey().first()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            updateUserKey(userKey)
        }
    }

    fun summaryBody() {
        getUserKey()

        if (userKey !== null) {
            lateinit var ret: BodyInfoResponse
            viewModelScope.launch {
                ret = summaryRepository.summaryBodyInfo(userKey.toString())
                Log.d("summaryBodyInfo", "$ret")

                if (ret.message == "SUCCESS") {
                    _summaryBodyInfoSuccess.value = true
                    summaryBodyInfo = ret.data
                }
            }
        }
    }
}