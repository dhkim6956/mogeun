package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetOfRoutineDetail(
    @SerialName(value = "set_number")
    val setNumber: Int,
    val weight: Int,
    @SerialName(value = "target_rep")
    val targetRep: Int
)

@Serializable
data class SetOfRoutineResponseData(
    @SerialName(value = "exec_name")
    val execName: String,
    @SerialName(value = "set_amount")
    val setAmount: Int,
    @SerialName(value = "set_details")
    val setDetails: List<SetOfRoutineDetail>
)

@Serializable
data class SetOfRoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: SetOfRoutineResponseData?
)

@Serializable
data class StartRoutineRequest(
    @SerialName(value = "user_key")
    val userKey: Int,
    @SerialName(value = "routine_key")
    val routineKey: Int,
    @SerialName(value = "is_attached")
    val isAttached: String
)

@Serializable
data class EndRoutineRequest(
    @SerialName(value = "user_key")
    val userKey: Int,
    @SerialName(value = "routine_report_key")
    val reportKey: Int,
)

@Serializable
data class RoutineExecutionResponseData(
    @SerialName(value = "user_key")
    val userKey: Int,
    val email: String?,
    @SerialName(value = "routine_key")
    val routineKey: Int,
    @SerialName(value = "report_key")
    val reportKey: Int?,
    @SerialName(value = "plan_key")
    val planKey: Int,
    @SerialName(value = "is_attached")
    val isAttached: String,
    @SerialName(value = "start_time")
    val startTime: String?,
    @SerialName(value = "end_time")
    val endTime: String?,
    @SerialName(value = "routine_report_key")
    val routineReportKey: Int?,
    @SerialName(value = "set_number")
    val setNumber: Int,
    @SerialName(value = "muscle_avg")
    val muscleAvg: Double,
    val weight: Int,
    @SerialName(value = "target_rep")
    val targetRep: Int,
    @SerialName(value = "success_rep")
    val successRep: Int,
    @SerialName(value = "muscle_fatigue")
    val muscleFatigue: Double
)

@Serializable
data class RoutineExecutionResponse(
    val code: Int,
    val status: String,
    val message: String?,
    val data: RoutineExecutionResponseData?
)