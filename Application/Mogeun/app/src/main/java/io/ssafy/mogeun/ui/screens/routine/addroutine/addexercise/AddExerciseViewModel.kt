package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.model.ListAllExerciseResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddExerciseViewModel(private val listAllExerciseRepository: RoutineRepository) : ViewModel() {
    private val _listAllExerciseSuccess = MutableStateFlow(false)
    val listAllExerciseSuccess: StateFlow<Boolean> = _listAllExerciseSuccess.asStateFlow()

    data class ExerciseItem(
        val name: String,
        val engName: String,
        val execDesc: String,
        val mainPart: Int,
        val imagePath: String
    )

    var exerciseList = mutableStateListOf<ExerciseItem>()

    fun initListAllExerciseSuccess() {
        _listAllExerciseSuccess.value = false
        exerciseList.clear()
    }

    fun listAllExercise() {
        viewModelScope.launch {
            val response = listAllExerciseRepository.listAllExercise("", "", "", 0, "")
            Log.d("listAllExercise", "$response")

            if (response.status == "SUCCESS") {
                _listAllExerciseSuccess.value = true

                val exercises = response.data.map { exerciseData ->
                    ExerciseItem(
                        name = exerciseData.name,
                        engName = exerciseData.engName,
                        execDesc = exerciseData.execDesc,
                        mainPart = exerciseData.mainPart,
                        imagePath = exerciseData.imagePath
                    )
                }
                exerciseList.clear()
                exerciseList.addAll(exercises)
            }
        }
    }
}