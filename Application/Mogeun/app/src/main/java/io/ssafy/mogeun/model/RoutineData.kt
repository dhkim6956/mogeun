package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddRoutineRequest(
    @SerialName(value = "user_key")
    val userKey: Int?,
    @SerialName(value = "routine_name")
    val routineName: String
)
@Serializable
data class AddRoutineResponseData(
    @SerialName(value = "user_key")
    val userKey: Int,
    @SerialName(value = "user_email")
    val userEmail: String?,
    @SerialName(value = "routine_name")
    val routineName: String,
    @SerialName(value = "routine_key")
    val routineKey: Int,
    @SerialName(value = "exec_key")
    val execKey: Int,
    @SerialName(value = "total_sets")
    val totalSets: Int,
    @SerialName(value = "plan_key")
    val planKey: Int,
    @SerialName(value = "set_key")
    val setKey: Int
)
@Serializable
data class AddRoutineResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: AddRoutineResponseData
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
    @SerialName(value = "routine_key")
    val routineKey: Int,
    val name: String,
    @SerialName(value = "image_path")
    val imagePath: List<String>
)
@Serializable
data class ListMyExerciseResponseDataExercises(
    val key: Int,
    val name: String,
    @SerialName(value = "eng_name")
    val engName: String,
    @SerialName(value = "sensing_part")
    val sensingPart: List<String>
)

@Serializable
data class ListMyExerciseResponseData(
    @SerialName(value = "routine_key")
    val routineKey: Int,
    @SerialName(value = "routine_name")
    val routineName: String,
    val exercise: List<ListMyExerciseResponseDataExercises>
)

@Serializable
data class ListMyExerciseResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: ListMyExerciseResponseData
)

