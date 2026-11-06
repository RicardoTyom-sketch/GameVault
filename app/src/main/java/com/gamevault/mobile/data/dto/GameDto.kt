package com.gamevault.mobile.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Int,
    val title: String,
    val genre: String,
    val platform: String,
    val rating: Double,
    val developer: String = "",
    val completed: Boolean = false,
    @SerialName("owner_id")
    val ownerId: Int
)

@Serializable
data class GameRequestDto(
    val title: String,
    val genre: String,
    val platform: String,
    val rating: Double,
    val developer: String,
    val completed: Boolean
)