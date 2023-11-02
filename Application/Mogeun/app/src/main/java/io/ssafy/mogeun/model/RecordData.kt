package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutineReportList(
    @SerialName(value = "routine_report_key")
    val key: Int,
    @SerialName(value = "routine_name")
    val routineName: String,
    @SerialName(value = "start_time")
    val startTime: String,
    @SerialName(value = "end_time")
    val endTime: String,
)

@Serializable
data class RoutineInfoList(
    val date: String,
    @SerialName(value = "routine_count")
    val routineCount: Int,
    @SerialName(value = "routine_reports")
    val routineReports: List<RoutineReportList>
)

@Serializable
data class MonthlyResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<RoutineInfoList>
)