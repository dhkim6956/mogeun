package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.network.MogeunApiService

interface RoutineRepository{
    suspend fun addRoutine(userKey: Int, routineName: String): AddRoutineResponse
    suspend fun listAllExercise(): ListAllExerciseResponse
}


class NetworkRoutineRepository(
    private val mogeunApiService: MogeunApiService
): RoutineRepository {
    override suspend fun addRoutine(userKey: Int, routineName: String): AddRoutineResponse {
        return mogeunApiService.addRoutine(AddRoutineRequest(userKey, routineName))
    }
    override suspend fun listAllExercise(): ListAllExerciseResponse {
        return mogeunApiService.listAllExercise()
    }
}

