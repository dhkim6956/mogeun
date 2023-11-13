package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.EndRoutineRequest
import io.ssafy.mogeun.model.RoutineExecutionResponse
import io.ssafy.mogeun.model.SetOfRoutineResponse
import io.ssafy.mogeun.model.StartRoutineRequest
import io.ssafy.mogeun.network.MogeunApiService

interface ExecutionRepository {

    suspend fun getSetOfRoutine(planKey: Int): SetOfRoutineResponse
    suspend fun startRoutine(userKey: Int, routineKey: Int, isAttached: String): RoutineExecutionResponse
    suspend fun endRoutine(userKey: Int, reportKey: Int): RoutineExecutionResponse
}

class NetworkExecutionRepository(
    private val mogeunApiService: MogeunApiService
): ExecutionRepository {
    override suspend fun getSetOfRoutine(planKey: Int): SetOfRoutineResponse {
        return mogeunApiService.getSetOfRoutine(planKey)
    }

    override suspend fun startRoutine(
        userKey: Int,
        routineKey: Int,
        isAttached: String
    ): RoutineExecutionResponse {
        return mogeunApiService.startRoutine(StartRoutineRequest(userKey, routineKey, isAttached))
    }

    override suspend fun endRoutine(userKey: Int, reportKey: Int): RoutineExecutionResponse {
        return mogeunApiService.endRoutine(EndRoutineRequest(userKey, reportKey))
    }
}