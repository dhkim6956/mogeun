package io.ssafy.mogeun.network

import io.ssafy.mogeun.model.SignInRequest
import io.ssafy.mogeun.model.SignInResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MogeunApiService {
    @POST("User/SignIn")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse
}