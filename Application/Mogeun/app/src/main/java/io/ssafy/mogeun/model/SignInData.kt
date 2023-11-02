package io.ssafy.mogeun.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName(value = "user_email")
    val email: String,
    @SerialName(value = "user_pw")
    val pw: String
)

@Serializable
data class SignInResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: String?
)

@Serializable
data class DupEmailBody(
    @SerialName(value = "is_joined")
    val isJoined: Boolean,
    @SerialName(value = "join_state")
    val joinState: String
)

@Serializable
data class DupEmailResponse(
    val code: Int,
    val status: String,
    val message: String,
    val data: DupEmailBody
)