package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoutineRequest(
    @SerialName(value = "user_email")
    val email: String,
    @SerialName(value = "routine_name")
    val routineName: String
)

@Serializable
data class CreateRoutineResponsedata(
    val routine_key: Int, // int
    val routine_name: String, // int
    val user_email:String,
    val exec_key: Int,
    val total_sets: Int,
    val plan_key: Int,
    val set_key: Int
)
@Serializable
data class CreateRoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: CreateRoutineResponsedata
)

