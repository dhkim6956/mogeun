package io.ssafy.mogeun.data

import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.model.SignInRequest
import io.ssafy.mogeun.model.SignInResponse
import io.ssafy.mogeun.model.SignUpRequest
import io.ssafy.mogeun.model.SignUpResponse
import io.ssafy.mogeun.network.MogeunApiService

interface UserRepository {
    suspend fun signIn(email: String, pw: String): SignInResponse
    suspend fun dupEmail(email: String): DupEmailResponse
    suspend fun signUp(userEmail: String, userPw: String, userName: String, gender: String, height: Double?, weight: Double?, smm: Double?, ffm: Double?): SignUpResponse
    suspend fun getInbody(user_key: String): GetInbodyResponse
}

class NetworkUserRepository(
    private val mogeunApiService: MogeunApiService
): UserRepository {
    override suspend fun signIn(email: String, pw: String): SignInResponse {
        return mogeunApiService.signIn(SignInRequest(email, pw))
    }
    override suspend fun dupEmail(email: String): DupEmailResponse {
        return  mogeunApiService.dupEmail(email = email)
    }
    override suspend fun signUp(userEmail: String, userPw: String, userName: String, gender: String, height: Double?, weight: Double?, smm: Double?, ffm: Double?): SignUpResponse {
        return mogeunApiService.signUp(SignUpRequest(userEmail, userPw, userName, gender, height, weight, smm, ffm))
    }
    override suspend fun getInbody(key: String): GetInbodyResponse {
        return mogeunApiService.getInbody(userKey = key)
    }
}