package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.network.MogeunApiService

interface RecordRepository {
    suspend fun recordMonthly(userKey: String, date: String): MonthlyResponse
}

class NetworkRecordRepository(
    private val mogeunApiService: MogeunApiService
): RecordRepository {
    override suspend fun recordMonthly(userKey: String, date: String): MonthlyResponse {
        return mogeunApiService.recordMonthly(userKey, date)
    }
}