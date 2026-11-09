package com.gamevault.mobile.data.repository

import com.gamevault.mobile.data.model.User

interface AuthRepository {
    suspend fun registerAndLogin(name: String, email: String, password: String): User
    suspend fun login(email: String, password: String): User
    suspend fun currentUser(): User?
    fun hasSession(): Boolean
    fun logout()
}
