package io.ssafy.mogeun.network

import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.SignInRequest
import io.ssafy.mogeun.model.SignInResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MogeunApiService {
    @POST("User/SignIn")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    @GET("User/isJoined")
    suspend fun dupEmail(@Query("email") email: String): DupEmailResponse


    @GET("Result/Monthly")
    suspend fun recordMonthly(@Query("user_key") userKey: String, @Query("date") date: String): MonthlyResponse
}