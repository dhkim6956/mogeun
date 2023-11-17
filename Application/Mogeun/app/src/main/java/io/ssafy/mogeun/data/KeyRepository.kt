package io.ssafy.mogeun.data

import kotlinx.coroutines.flow.Flow

interface KeyRepository {
    suspend fun insertKey(key: Key)
    fun getKey(): Flow<Key?>
    suspend fun deleteKeyData()
}

class OfflineKeyRepository(private val keyDao: KeyDao): KeyRepository {
    override suspend fun insertKey(key: Key) = keyDao.insert(key)
    override fun getKey(): Flow<Key?> = keyDao.getKey(1)
    override suspend fun deleteKeyData() = keyDao.deleteKeyData()
}