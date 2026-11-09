package com.gamevault.mobile.data.datasource

import com.gamevault.mobile.data.dto.*
import retrofit2.Response
import retrofit2.http.*

interface GameVaultApi {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequestDto
    ): UserDto

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    @GET("api/auth/me")
    suspend fun profile(): UserDto

    @GET("api/games")
    suspend fun getGames(): List<GameDto>

    @GET("api/games/{id}")
    suspend fun getGame(
        @Path("id") id: Int
    ): GameDto

    @POST("api/games")
    suspend fun createGame(
        @Body request: GameRequestDto
    ): GameDto

    @PUT("api/games/{id}")
    suspend fun updateGame(
        @Path("id") id: Int,
        @Body request: GameRequestDto
    ): GameDto

    @DELETE("api/games/{id}")
    suspend fun deleteGame(
        @Path("id") id: Int
    ): Response<Unit>
}