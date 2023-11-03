package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutineReport(
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
data class MonthlyRoutine(
    val date: String,
    @SerialName(value = "routine_count")
    val routineCount: Int,
    @SerialName(value = "routine_reports")
    val routineReports: List<RoutineReport>
)

@Serializable
data class MonthlyResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<MonthlyRoutine>
)

@Serializable
data class Exercise(
    @SerialName(value = "exec_name")
    val execName: String,
    @SerialName(value = "image_path")
    val imagePath: String,
    val sets:Int,
    val parts: List<String>
)

@Serializable
data class RoutineInfoData(
    val name: String,
    val date: String,
    val calorie: Int,
    @SerialName(value = "perform_time")
    val performTime: Int,
    val exercises: List<Exercise>
)

@Serializable
data class RoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: RoutineInfoData
)