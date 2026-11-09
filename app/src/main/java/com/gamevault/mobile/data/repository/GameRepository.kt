package com.gamevault.mobile.data.repository

import com.gamevault.mobile.data.model.Game
import com.gamevault.mobile.data.model.GameInput

interface GameRepository {
    suspend fun getGames(): List<Game>
    suspend fun getGame(id: Int): Game
    suspend fun createGame(input: GameInput): Game
    suspend fun updateGame(id: Int, input: GameInput): Game
    suspend fun deleteGame(id: Int)
}
