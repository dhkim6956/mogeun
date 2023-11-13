package io.ssafy.mogeun.ui.screens.summary

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.SummaryRepository
import io.ssafy.mogeun.model.BodyInfo
import io.ssafy.mogeun.model.BodyInfoResponse
import io.ssafy.mogeun.model.MostPerformedExercise
import io.ssafy.mogeun.model.MostPerformedExerciseResponse
import io.ssafy.mogeun.model.MostSetExercise
import io.ssafy.mogeun.model.MostSetExerciseResponse
import io.ssafy.mogeun.model.MostWeightedExercise
import io.ssafy.mogeun.model.MostWeightedExerciseResponse
import io.ssafy.mogeun.model.PerformedMuscleInfo
import io.ssafy.mogeun.model.PerformedMuscleInfoResponse
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

    private val _summaryPerformedMuscleSuccess = MutableStateFlow(false)
    val summaryPerformedMuscleSuccess: StateFlow<Boolean> = _summaryPerformedMuscleSuccess.asStateFlow()
    var summaryPerformedMuscle = mutableStateListOf<List<PerformedMuscleInfo>>()

    private val _summaryExerciseMostSuccess = MutableStateFlow(false)
    val summaryExerciseMostSuccess: StateFlow<Boolean> = _summaryExerciseMostSuccess.asStateFlow()
    var summaryExerciseMost = mutableStateListOf<MostPerformedExercise>()

    private val _summaryExerciseWeightSuccess = MutableStateFlow(false)
    val summaryExerciseWeightSuccess: StateFlow<Boolean> = _summaryExerciseWeightSuccess.asStateFlow()
    var summaryExerciseWeight = mutableStateListOf<MostWeightedExercise>()

    private val _summaryExerciseSetSuccess = MutableStateFlow(false)
    val summaryExerciseSetSuccess: StateFlow<Boolean> = _summaryExerciseSetSuccess.asStateFlow()
    var summaryExerciseSet = mutableStateListOf<MostSetExercise>()

    var itemIndex = mutableIntStateOf(0)

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

    fun summaryPerformedMuscle() {
        if (userKey !== null) {
            lateinit var ret: PerformedMuscleInfoResponse
            viewModelScope.launch {
                ret = summaryRepository.summaryPerformedMuscle(userKey.toString(), "1")
                Log.d("summaryPerformedMuscleAll", "$ret")

                if (ret.message == "SUCCESS") {
                    summaryPerformedMuscle.add(ret.data)
                }

                ret = summaryRepository.summaryPerformedMuscle(userKey.toString(), "2")
                Log.d("summaryPerformedMuscleYear", "$ret")

                if (ret.message == "SUCCESS") {
                    summaryPerformedMuscle.add(ret.data)
                }

                ret = summaryRepository.summaryPerformedMuscle(userKey.toString(), "3")
                Log.d("summaryPerformedMuscleMonth", "$ret")

                if (ret.message == "SUCCESS") {
                    _summaryPerformedMuscleSuccess.value = true
                    summaryPerformedMuscle.add(ret.data)
                }
            }
        }
    }

    fun summaryExerciseMost() {
        if (userKey !== null) {
            lateinit var ret: MostPerformedExerciseResponse
            viewModelScope.launch {
                ret = summaryRepository.summaryExerciseMost(userKey.toString(), "1")
                Log.d("summaryPerformedMuscleAll", "$ret")

                if (ret.message == "SUCCESS") {
                    summaryExerciseMost.add(ret.data)
                }

                ret = summaryRepository.summaryExerciseMost(userKey.toString(), "2")
                Log.d("summaryPerformedMuscleYear", "$ret")

                if (ret.message == "SUCCESS") {
                    summaryExerciseMost.add(ret.data)
                }

                ret = summaryRepository.summaryExerciseMost(userKey.toString(), "3")
                Log.d("summaryPerformedMuscleMonth", "$ret")

                if (ret.message == "SUCCESS") {
                    _summaryExerciseMostSuccess.value = true
                    summaryExerciseMost.add(ret.data)
                }
            }
        }
    }

    fun summaryExerciseWeight() {
        if (userKey !== null) {
            lateinit var ret: MostWeightedExerciseResponse
            viewModelScope.launch {
                ret = summaryRepository.summaryExerciseWeight(userKey.toString(), "1")
                Log.d("summaryPerformedMuscleAll", "$ret")

                if (ret.message == "SUCCESS") {
                    summaryExerciseWeight.add(ret.data)
                }

                ret = summaryRepository.summaryExerciseWeight(userKey.toString(), "2")
                Log.d("summaryPerformedMuscleYear", "$ret")

                if (ret.message == "SUCCESS") {
                    summaryExerciseWeight.add(ret.data)
                }

                ret = summaryRepository.summaryExerciseWeight(userKey.toString(), "3")
                Log.d("summaryPerformedMuscleMonth", "$ret")

                if (ret.message == "SUCCESS") {
                    _summaryBodyInfoSuccess.value = true
                    summaryExerciseWeight.add(ret.data)
                }
            }
        }
    }

    fun summaryExerciseSet() {
        if (userKey !== null) {
            lateinit var ret: MostSetExerciseResponse
            viewModelScope.launch {
                ret = summaryRepository.summaryExerciseSet(userKey.toString(), "1")
                Log.d("summaryPerformedMuscleAll", "$ret")

                if (ret.message == "SUCCESS") {
                    summaryExerciseSet.add(ret.data)
                }

                ret = summaryRepository.summaryExerciseSet(userKey.toString(), "2")
                Log.d("summaryPerformedMuscleYear", "$ret")

                if (ret.message == "SUCCESS") {
                    summaryExerciseSet.add(ret.data)
                }

                ret = summaryRepository.summaryExerciseSet(userKey.toString(), "3")
                Log.d("summaryPerformedMuscleMonth", "$ret")

                if (ret.message == "SUCCESS") {
                    _summaryExerciseSetSuccess.value = true
                    summaryExerciseSet.add(ret.data)
                }
            }
        }
    }
}