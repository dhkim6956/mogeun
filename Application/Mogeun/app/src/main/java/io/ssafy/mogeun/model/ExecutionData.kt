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