package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRoutineRequest(
    @SerialName(value = "user_key")
    val userKey: Int,
    @SerialName(value = "routine_name")
    val routineName: String
)

@Serializable
data class AddRoutineResponsedata(
    @SerialName(value = "routine_key")
    val routineKey: Int, // int
    @SerialName(value = "routine_name")
    val routineName: String, // int
    @SerialName(value = "user_key")
    val userKey:Int,
    @SerialName(value = "exec_key")
    val execKey: Int,
    @SerialName(value = "total_sets")
    val totalSets: Int,
    @SerialName(value = "plan_key")
    val planKey: Int,
    @SerialName(value = "set_key")
    val setKey: Int,
    @SerialName(value = "user_email")
    val userEmail: String? // int
)
@Serializable
data class AddRoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: AddRoutineResponsedata
)

@Serializable
data class ListAllExerciseResponsedata(
    val key: Int,
    val name: String,
    @SerialName(value = "eng_name")
    val engName: String,
    @SerialName(value = "sensing_part")
    val sensingPart: String?,
    @SerialName(value = "main_part")
    val mainPart: String,
    @SerialName(value = "image_path")
    val imagePath: String
)

@Serializable
data class ListAllExerciseResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<ListAllExerciseResponsedata>
)