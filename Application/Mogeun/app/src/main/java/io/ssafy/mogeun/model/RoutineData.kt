package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRoutineRequest(
    @SerialName(value = "user_email")
    val email: String,
    @SerialName(value = "routine_name")
    val routineName: String
)

@Serializable
data class AddRoutineResponsedata(
    val routine_key: Int, // int
    val routine_name: String, // int
    val user_email:String,
    val exec_key: Int,
    val total_sets: Int,
    val plan_key: Int,
    val set_key: Int
)
@Serializable
data class AddRoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: AddRoutineResponsedata
)

@Serializable
data class ListAllExerciseResponse(
    val name: String,
    val engName: String,
    val execDesc: String,
    val mainPart: Int,
    val imagePath: String
)