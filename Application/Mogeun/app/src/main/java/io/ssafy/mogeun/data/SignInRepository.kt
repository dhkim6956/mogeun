package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.SignInRequest
import io.ssafy.mogeun.model.SignInResponse
import io.ssafy.mogeun.network.MogeunApiService

interface SignInRepository {
    suspend fun signIn(email: String, pw: String): SignInResponse
}

class NetworkSignInRepository(
    private val mogeunApiService: MogeunApiService
): SignInRepository {
    override suspend fun signIn(email: String, pw: String): SignInResponse {
        return mogeunApiService.signIn(SignInRequest(email, pw))
    }
}