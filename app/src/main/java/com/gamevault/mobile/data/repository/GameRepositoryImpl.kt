package com.gamevault.mobile.data.repository

import com.gamevault.mobile.data.datasource.GameVaultApi
import com.gamevault.mobile.data.mapper.toModel
import com.gamevault.mobile.data.mapper.toRequestDto
import com.gamevault.mobile.data.model.Game
import com.gamevault.mobile.data.model.GameInput

class GameRepositoryImpl(private val api: GameVaultApi) : GameRepository {
    override suspend fun getGames(): List<Game> = api.getGames().map { it.toModel() }
    override suspend fun getGame(id: Int): Game = api.getGame(id).toModel()
    override suspend fun createGame(input: GameInput): Game =
        api.createGame(input.toRequestDto()).toModel()
    override suspend fun updateGame(id: Int, input: GameInput): Game =
        api.updateGame(id, input.toRequestDto()).toModel()
    override suspend fun deleteGame(id: Int) {
        val response = api.deleteGame(id)
        if (!response.isSuccessful) error("No se pudo eliminar: HTTP ${response.code()}")
    }
}
