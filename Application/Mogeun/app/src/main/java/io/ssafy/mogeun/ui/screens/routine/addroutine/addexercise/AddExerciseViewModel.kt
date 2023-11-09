package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.NetworkRoutineRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.model.ListAllExerciseResponsedata
import io.ssafy.mogeun.ui.screens.routine.addroutine.AddRoutineViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AddExerciseViewModel(
    private val keyRepository: KeyRepository,
    private val routineRepository: RoutineRepository
) : ViewModel() {
    private val _listAllExerciseSuccess = MutableStateFlow(false)
    val listAllExerciseSuccess: StateFlow<Boolean> = _listAllExerciseSuccess.asStateFlow()
    var exerciseList = mutableStateListOf<ListAllExerciseResponsedata>()

    private val _addRoutineSuccess = MutableStateFlow(false)
    val addRoutineSuccess: StateFlow<Boolean> = _addRoutineSuccess.asStateFlow()
    var myRoutine = mutableStateListOf<AddRoutineRequest>()
    var userKey by mutableStateOf<Int?>(null)
    fun initListAllExerciseSuccess() {
        _listAllExerciseSuccess.value = false
        exerciseList.clear()
    }
    fun updateUserKey(value: Int?) {
        userKey = value
    }
    fun initAddRoutineSuccess() {
        _addRoutineSuccess.value = false
        myRoutine.clear()
    }
    fun getUserKey() {
        viewModelScope.launch {
            val key = keyRepository.getKey().first()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            updateUserKey(userKey)
        }
    }
    fun listAllExercise() {
        lateinit var ret: ListAllExerciseResponse
        viewModelScope.launch {
            ret = routineRepository.listAllExercise()
            Log.d("listAllExercise", "$ret")

            if (ret.message == "SUCCESS") {
                _listAllExerciseSuccess.value = true
                // 이름을 기준으로 중복을 제거합니다.
                val uniqueExercises = ret.data.distinctBy { it.name }
                exerciseList.clear()
                exerciseList.addAll(uniqueExercises)
            }
        }
    }

    fun addRoutine(userKey: Int, routineName: String) {
        lateinit var ret: AddRoutineResponse
        viewModelScope.launch{
            ret = routineRepository.addRoutine(userKey, routineName)
            Log.d("addRoutine", "$ret")
            if(ret.message == "SUCCESS") {
                _addRoutineSuccess.value = true
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val routineRepository = application.container.routineRepository
                val keyRepository = application.container.keyRepository
                AddExerciseViewModel(routineRepository = routineRepository, keyRepository = keyRepository)
            }
        }
    }
}