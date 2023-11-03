package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.model.ListAllExerciseResponse
import io.ssafy.mogeun.network.MogeunApiService

interface AddRoutineRepository {
    suspend fun addRoutine(userKey: Int, routineName: String): AddRoutineResponse
}
interface ListAllExerciseRepository {
    suspend fun listAllExercise(name: String, engName: String, execDesc: String, mainPart: Int, imagePath: String): ListAllExerciseResponse
}

class NetworkAddRoutineRepository(
    private val mogeunApiService: MogeunApiService
): AddRoutineRepository {
    override suspend fun addRoutine(userKey: Int, routineName: String): AddRoutineResponse {
        return mogeunApiService.addRoutine(AddRoutineRequest(userKey, routineName))
    }
}

class NetworkListAllExerciseRepository(
    private val mogeunApiService: MogeunApiService
): ListAllExerciseRepository {
    override suspend fun listAllExercise(name: String, engName: String, execDesc: String, mainPart: Int, imagePath: String): ListAllExerciseResponse {
        return mogeunApiService.listAllExercise(name, engName, execDesc, mainPart, imagePath)
    }
}