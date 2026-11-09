package com.gamevault.mobile.data.mapper

import com.gamevault.mobile.data.dto.GameRequestDto
import com.gamevault.mobile.data.model.GameInput

fun GameInput.toRequestDto(): GameRequestDto = GameRequestDto(
    title = title,
    genre = genre,
    platform = platform,
    developer = developer,
    rating = rating,
    completed = completed
)
