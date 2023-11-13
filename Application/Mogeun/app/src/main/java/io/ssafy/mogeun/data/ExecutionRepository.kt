package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.SetOfRoutineResponse
import io.ssafy.mogeun.network.MogeunApiService

interface ExecutionRepository {

    suspend fun getSetOfRoutine(planKey: Int): SetOfRoutineResponse
}

class NetworkExecutionRepository(
    private val mogeunApiService: MogeunApiService
): ExecutionRepository {
    override suspend fun getSetOfRoutine(planKey: Int): SetOfRoutineResponse {
        return mogeunApiService.getSetOfRoutine(planKey)
    }
}