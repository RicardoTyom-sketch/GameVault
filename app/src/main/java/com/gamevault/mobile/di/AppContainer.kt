package com.gamevault.mobile.di

import android.content.Context
import com.gamevault.mobile.data.datasource.GameVaultServiceFactory
import com.gamevault.mobile.data.datasource.SessionStore
import com.gamevault.mobile.data.datasource.SharedPreferencesSessionStore
import com.gamevault.mobile.data.repository.AuthRepository
import com.gamevault.mobile.data.repository.AuthRepositoryImpl
import com.gamevault.mobile.data.repository.GameRepository
import com.gamevault.mobile.data.repository.GameRepositoryImpl

class AppContainer(context: Context) {
    val sessionStore: SessionStore = SharedPreferencesSessionStore(context)
    private val api = GameVaultServiceFactory(
        baseUrl = ApiConfig.BASE_URL,
        tokenProvider = { sessionStore.getToken() }
    ).create()
    val authRepository: AuthRepository = AuthRepositoryImpl(api, sessionStore)
    val gameRepository: GameRepository = GameRepositoryImpl(api)
}
