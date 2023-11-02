package io.ssafy.mogeun.network

import io.ssafy.mogeun.model.CreateRoutineRequest
import io.ssafy.mogeun.model.CreateRoutineResponse
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.SignInRequest
import io.ssafy.mogeun.model.SignInResponse
import io.ssafy.mogeun.model.SignUpRequest
import io.ssafy.mogeun.model.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MogeunApiService {
    @POST("User/SignIn")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    @GET("User/isJoined")
    suspend fun dupEmail(@Query("email") email: String): DupEmailResponse

    @POST("User/Enroll")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): SignUpResponse

    @POST("Routine/Create")
    suspend fun CreateRoutine(@Body createRoutineRequest: CreateRoutineRequest): CreateRoutineResponse
}