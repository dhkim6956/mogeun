package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.model.GetRoutineListResponse
import io.ssafy.mogeun.network.MogeunApiService

interface RoutineRepository{
    suspend fun addRoutine(userKey: Int, routineMame: String): AddRoutineResponse
    suspend fun getRoutineList(user_key: String): GetRoutineListResponse
    suspend fun listAllExercise(): ListAllExerciseResponse
}


class NetworkRoutineRepository(
    private val mogeunApiService: MogeunApiService
): RoutineRepository {
    override suspend fun addRoutine(userKey:Int, routineName: String): AddRoutineResponse {
        return mogeunApiService.addRoutine(AddRoutineRequest(userKey, routineName))
    }
    override suspend fun listAllExercise(): ListAllExerciseResponse {
        return mogeunApiService.listAllExercise()
    }

    override suspend fun getRoutineList(key: String): GetRoutineListResponse {
        return mogeunApiService.getRoutineList(key)
    }
}

