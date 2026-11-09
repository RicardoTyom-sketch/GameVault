package com.gamevault.mobile.data.repository

import com.gamevault.mobile.data.datasource.GameVaultApi
import com.gamevault.mobile.data.datasource.SessionStore
import com.gamevault.mobile.data.dto.LoginRequestDto
import com.gamevault.mobile.data.dto.RegisterRequestDto
import com.gamevault.mobile.data.mapper.toModel
import com.gamevault.mobile.data.model.User

class AuthRepositoryImpl(
    private val api: GameVaultApi,
    private val sessionStore: SessionStore
) : AuthRepository {
    override suspend fun registerAndLogin(name: String, email: String, password: String): User {
        api.register(RegisterRequestDto(name, email, password))
        return login(email, password)
    }
    override suspend fun login(email: String, password: String): User {
        val response = api.login(LoginRequestDto(email, password))
        sessionStore.saveToken(response.accessToken)
        return response.user.toModel()
    }
    override suspend fun currentUser(): User? {
        if (!hasSession()) return null
        return api.profile().toModel()
    }
    override fun hasSession() = !sessionStore.getToken().isNullOrBlank()
    override fun logout() = sessionStore.clear()
}
