package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetOfRoutineResponse(
    val date: String,
    @SerialName(value = "routine_count")
    val routineCount: Int,
    @SerialName(value = "routine_reports")
    val routineReports: List<RoutineReport>
)