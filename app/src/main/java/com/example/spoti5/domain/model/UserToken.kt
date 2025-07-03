package com.example.spoti5.domain.model

data class UserToken(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int
)
