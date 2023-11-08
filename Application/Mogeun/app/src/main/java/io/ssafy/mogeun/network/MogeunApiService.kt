package io.ssafy.mogeun.network

import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.DupEmailBody
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.RoutineResponse
import io.ssafy.mogeun.model.SignInRequest
import io.ssafy.mogeun.model.SignInResponse
import io.ssafy.mogeun.model.SignUpRequest
import io.ssafy.mogeun.model.SignUpResponse
import io.ssafy.mogeun.model.GetRoutineListResponse
import io.ssafy.mogeun.model.SetRequest
import io.ssafy.mogeun.model.SetResponse
import io.ssafy.mogeun.model.UpdateUserRequest
import io.ssafy.mogeun.model.UpdateUserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface MogeunApiService {
    @POST("User/SignIn")
    suspend fun signIn(@Body signInRequest: SignInRequest): SignInResponse

    @GET("User/isJoined")
    suspend fun dupEmail(@Query("email") email: String): DupEmailResponse

    @POST("User/Enroll")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): SignUpResponse

    @GET("User/Detail")
    suspend fun getInbody(@Query("user_key") userKey: String): GetInbodyResponse

    @PUT("User/Log/Change/All")
    suspend fun updateUser(@Body updateUserRequest: UpdateUserRequest): UpdateUserResponse

    @GET("Routine/ListAll")
    suspend fun getRoutineList(@Query("user_key") userKey: String): GetRoutineListResponse

    @POST("Routine/Create")
    suspend fun addRoutine(@Query("user_key") userKey: String, @Query("routine_name") routineName: String): AddRoutineResponse


    @GET("Result/Monthly")
    suspend fun recordMonthly(@Query("user_key") userKey: String, @Query("date") date: String): MonthlyResponse

    @GET("Result/Routine")
    suspend fun recordRoutine(@Query("user_key") userKey:String, @Query("routine_result_key") reportKey: String): RoutineResponse

    @GET("Exercise/ListAll")
    suspend fun listAllExercise(): ListAllExerciseResponse

    @POST("Report/Routine/Set")
    suspend fun getSet(@Body setRequest: SetRequest): SetResponse

}