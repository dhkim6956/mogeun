package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.CreateRoutineRequest
import io.ssafy.mogeun.model.CreateRoutineResponse
import io.ssafy.mogeun.network.MogeunApiService

interface CreateRoutineRepository {
    suspend fun createRoutine(email: String, routineName: String): CreateRoutineResponse
}

class NetworkCreateRoutineRepository(
    private val mogeunApiService: MogeunApiService
): CreateRoutineRepository {
    override suspend fun createRoutine(email: String, routineName: String): CreateRoutineResponse {
        return mogeunApiService.CreateRoutine(CreateRoutineRequest(email, routineName))
    }
}