package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.model.ListAllExerciseResponsedata
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddExerciseViewModel(private val listAllExerciseRepository: RoutineRepository) : ViewModel() {
    private val _listAllExerciseSuccess = MutableStateFlow(false)
    val listAllExerciseSuccess: StateFlow<Boolean> = _listAllExerciseSuccess.asStateFlow()
    var exerciseList = mutableStateListOf<ListAllExerciseResponsedata>()

    private val _addRoutineSuccess = MutableStateFlow(false)
    val addRoutineSuccess: StateFlow<Boolean> = _addRoutineSuccess.asStateFlow()
    var myRoutine = mutableStateListOf<AddRoutineRequest>()

    fun initListAllExerciseSuccess() {
        _listAllExerciseSuccess.value = false
        exerciseList.clear()
    }

    fun initAddRoutineSuccess() {
        _addRoutineSuccess.value = false
        myRoutine.clear()
    }

    fun listAllExercise() {
        lateinit var ret: ListAllExerciseResponse
        viewModelScope.launch {
            ret = listAllExerciseRepository.listAllExercise()
            Log.d("listAllExercise", "$ret")

            if (ret.message == "SUCCESS") {
                _listAllExerciseSuccess.value = true
                for(exercise in ret.data){
                    exerciseList.add(exercise)
                }
            }
        }
    }

    fun addRoutine(userKey: String, routineName: String) {
        lateinit var ret: AddRoutineResponse
        viewModelScope.launch{
            ret = listAllExerciseRepository.addRoutine(userKey, routineName)
            Log.d("addRoutine", "$ret")
            if(ret.message == "SUCCESS") {
                _addRoutineSuccess.value = true
            }
        }
    }
}