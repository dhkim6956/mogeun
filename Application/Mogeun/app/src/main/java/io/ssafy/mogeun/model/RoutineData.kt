package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRoutineRequest(
    @SerialName(value = "user_key")
    val userKey: String,
    @SerialName(value = "routine_name")
    val routineName: String
)

@Serializable
data class AddRoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: AddRoutineRequest
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

@Serializable
data class GetRoutineListResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<GetRoutineListResponseBody>
)

@Serializable
data class GetRoutineListResponseBody(
    val key: Int,
    val name: String
)