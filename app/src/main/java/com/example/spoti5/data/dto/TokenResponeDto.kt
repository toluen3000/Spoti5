package com.example.spoti5.data.dto

import com.example.spoti5.domain.model.UserToken
import com.google.gson.annotations.SerializedName

data class TokenResponseDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int
) {
    fun toDomain(): UserToken = UserToken(accessToken, tokenType, expiresIn)
}
