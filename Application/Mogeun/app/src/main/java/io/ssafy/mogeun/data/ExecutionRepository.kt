package io.ssafy.mogeun.data

import io.ssafy.mogeun.network.MogeunApiService

interface ExecutionRepository {
//    suspend fun recordMonthly(userKey: String, date: String): MonthlyResponse
}
class NetworkExecutionRepository(
    private val mogeunApiService: MogeunApiService
): ExecutionRepository {

}