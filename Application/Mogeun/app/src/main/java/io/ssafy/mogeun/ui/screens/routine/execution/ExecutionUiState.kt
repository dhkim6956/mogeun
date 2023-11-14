package io.ssafy.mogeun.ui.screens.routine.execution

import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.model.ListMyExerciseResponse
import io.ssafy.mogeun.model.SetOfRoutineDetail

data class EmgUiState (
    val emg1: Emg? = null,
    val emg2: Emg? = null,
    val emg1Avg: Double = 0.0,
    val emg2Avg: Double = 0.0,
    val emg1List: List<Int> = listOf(),
    val emg2List: List<Int> = listOf(),
    val emg1Max: Int = 0,
    val emg2Max: Int = 0
)

data class SetOfPlan(
    val planKey: Int,
    val valueChanged: Boolean,
    val setOfRoutineDetail: List<SetOfRoutineDetail>
)

data class RoutineState(
    val planList: ListMyExerciseResponse? = null,
    val planDetails: List<SetOfPlan> = listOf(),
    val showBottomSheet: Boolean = false,
    val requestedPlan: List<Int> = listOf()
)

data class ElapsedTime(
    val startTime: Long,
    val minute: Int,
    val second: Int,
)