package com.gamevault.mobile.data.datasource

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class GameVaultServiceFactory(
    private val baseUrl: String,
    private val tokenProvider: () -> String?
) {
    fun create(): GameVaultApi {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                val token = tokenProvider()

                if (!token.isNullOrBlank()) {
                    builder.header(
                        "Authorization",
                        "Bearer $token"
                    )
                }

                chain.proceed(builder.build())
            }
            .build()

        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(GameVaultApi::class.java)
    }
}