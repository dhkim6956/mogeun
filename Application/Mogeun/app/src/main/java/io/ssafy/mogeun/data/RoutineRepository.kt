package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.AddRoutineRequest
import io.ssafy.mogeun.model.AddRoutineResponse
import io.ssafy.mogeun.network.MogeunApiService

interface RoutineRepository {
    suspend fun addRoutine(email: String, routineName: String): AddRoutineResponse
}

class NetworkAddRoutineRepository(
    private val mogeunApiService: MogeunApiService
): RoutineRepository {
    override suspend fun addRoutine(email: String, routineName: String): AddRoutineResponse {
        return mogeunApiService.AddRoutine(AddRoutineRequest(email, routineName))
    }
}

//class NetworkListAllExerciseRepository(
//    private val mogeunApiService: MogeunApiService
//): RoutineRepository {
//    override suspend fun listAllExercise(name: String, engName: String, execDesc: String, mainPart: Int, imagePath: String): ListAllExerciseResponse {
//        return mogeunApiService.ListAllExercise(name, engName, execDesc, mainPart, imagePath)
//    }
//}