package io.ssafy.mogeun.data

import kotlinx.coroutines.flow.Flow

interface KeyRepository {
    suspend fun insertKey(key: Key)
}

class OfflineKeyRepository(private val keyDao: KeyDao): KeyRepository {
    override suspend fun insertKey(key: Key) = keyDao.insert(key)
}