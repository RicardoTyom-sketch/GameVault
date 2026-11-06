package com.gamevault.mobile.data.mapper

import com.gamevault.mobile.data.dto.GameDto
import com.gamevault.mobile.data.dto.GameRequestDto
import com.gamevault.mobile.data.dto.UserDto
import com.gamevault.mobile.data.model.Game
import com.gamevault.mobile.data.model.User

fun GameDto.toModel(): Game = Game(
    id = id,
    title = title,
    genre = genre,
    platform = platform,
    rating = rating,
    developer = developer,
    completed = completed,
    ownerId = ownerId
)

fun Game.toRequestDto(): GameRequestDto =
    GameRequestDto(
        title = title,
        genre = genre,
        platform = platform,
        rating = rating,
        developer = developer,
        completed = completed
    )

fun UserDto.toModel(): User = User(
    id = id,
    name = name,
    email = email
)