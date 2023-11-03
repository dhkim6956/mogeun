package io.ssafy.mogeun.network

import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.SignInRequest
import io.ssafy.mogeun.model.SignInResponse
import io.ssafy.mogeun.model.SignUpRequest
import io.ssafy.mogeun.model.SignUpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MogeunApiService {
    @POST("User/SignIn")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    @GET("User/isJoined")
    suspend fun dupEmail(@Query("email") email: String): DupEmailResponse

    @POST("User/Enroll")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): SignUpResponse

    @POST("Routine/Create")
    suspend fun AddRoutine(@Body addRoutineRequest: AddRoutineRequest): AddRoutineResponse


    @GET("Result/Monthly")
    suspend fun recordMonthly(@Query("user_key") userKey: String, @Query("date") date: String): MonthlyResponse

    @GET("Exercise/ListAll")
    suspend fun listAllExercise(@Query("name") name: String, @Query("eng_name") engName: String, @Query("exec_desc") execDesc: String, @Query("main_part") mainPart: Int, @Query("image_path") imagePath: String)

}