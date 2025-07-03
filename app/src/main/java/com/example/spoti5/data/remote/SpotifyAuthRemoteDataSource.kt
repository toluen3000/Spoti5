package com.example.spoti5.data.remote

import com.example.spoti5.BuildConfig
import com.example.spoti5.data.apis.SpotifyAuthApi
import com.example.spoti5.domain.model.UserToken
import jakarta.inject.Inject
import okhttp3.Credentials


class SpotifyAuthRemoteDataSource @Inject constructor(
    private val api: SpotifyAuthApi
) {
    suspend fun exchangeCode(code: String): UserToken {
        val response = api.getAccessTokenFromCode(
            grantType = "authorization_code",
            code = code,
            redirectUri = BuildConfig.SPOTIFY_REDIRECT_URI,
            clientId = BuildConfig.SPOTIFY_CLIENT_ID
        )
        return response.toDomain()
    }
}
