package com.gamevault.mobile.data.model

data class Game(
    val id: Int,
    val title: String,
    val genre: String,
    val platform: String,
    val rating: Double,
    val developer: String,
    val completed: Boolean,
    val ownerId: Int
)
