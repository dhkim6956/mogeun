package io.ssafy.mogeun.model

import kotlinx.serialization.Serializable

@Serializable
data class BodyInfoResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: List<Float>
)