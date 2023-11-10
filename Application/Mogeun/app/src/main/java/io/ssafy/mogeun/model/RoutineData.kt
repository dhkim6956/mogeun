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
    @SerialName(value = "routine_key")
    val routineKey: Int,
    @SerialName(value = "routine_name")
    val routineName: String


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
    @SerialName(value = "exec_key")
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
    val exercises: List<ListMyExerciseResponseDataExercises>
)

@Serializable
data class ListMyExerciseResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: ListMyExerciseResponseData
)

@Serializable
data class AddAllExerciseRequest(
    @SerialName(value = "routine_key")
    val routineKey: Int?,
    @SerialName(value = "exec_key")
    val execKey: List<Int>
)

@Serializable
data class AddAllExerciseResponseData(
    @SerialName(value = "Added")
    val added: List<Int>,
    @SerialName(value = "Failed")
    val failed: List<Int>
)

@Serializable
data class AddAllExerciseResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: AddAllExerciseResponseData
)

@Serializable
data class MyExerciseResponseData(
    @SerialName(value = "exec_key")
    val execKey: Int,
    val name: String,
    @SerialName(value = "eng_name")
    val engName: String,
    @SerialName(value = "sensing_part")
    val sensingPart: List<String>,
    @SerialName(value = "main_part")
    val mainPart: String?,
    @SerialName(value = "image_path")
    val imagePath: String?
)

@Serializable
data class MyExerciseResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: MyExerciseResponseData
)