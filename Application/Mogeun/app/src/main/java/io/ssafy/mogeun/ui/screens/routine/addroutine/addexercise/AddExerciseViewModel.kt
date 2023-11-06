package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.ListAllExerciseRepository
import io.ssafy.mogeun.model.ListAllExerciseResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddExerciseViewModel(private val listAllExerciseRepository: ListAllExerciseRepository): ViewModel() {
    private val _listAllExerciseSuccess = MutableStateFlow(false)
    val listAllExerciseSuccess: StateFlow<Boolean> = _listAllExerciseSuccess.asStateFlow()

    fun initListAllExerciseSuccess(){
        _listAllExerciseSuccess.value = false
    }

    fun listAllExercise(name: String, engName: String, execDesc: String, mainPart: Int, imagePath: String){
        lateinit var ret: ListAllExerciseResponse
        viewModelScope.launch {
            ret = listAllExerciseRepository.listAllExercise(name, engName, execDesc, mainPart, imagePath)
            Log.d("listAllExercise", "$ret")

            if(ret.message == "SUCCESS") {
                _listAllExerciseSuccess.value = true
            }
        }
    }
}