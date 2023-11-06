package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.RoutineRepository
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

    fun initListAllExerciseSuccess() {
        _listAllExerciseSuccess.value = false
        exerciseList.clear()
    }

    fun listAllExercise() {
        lateinit var ret: ListAllExerciseResponse
        viewModelScope.launch {
            val ret = listAllExerciseRepository.listAllExercise()
            Log.d("listAllExercise", "$ret")

            if (ret.message == "SUCCESS") {
                _listAllExerciseSuccess.value = true
                for(exercise in ret.data){
                    exerciseList.add(exercise)
                }
            }
        }
    }
}