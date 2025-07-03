package com.example.spoti5.domain.use_cases

import com.example.spoti5.BuildConfig
import com.example.spoti5.domain.model.UserToken
import com.example.spoti5.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class AuthenticateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(code: String): Flow<UserToken> {
        return authRepository.exchangeCode(code)
    }

    object SpotifyConstants {
        const val CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID
        const val REDIRECT_URI = BuildConfig.SPOTIFY_REDIRECT_URI
        val SCOPES = arrayOf("user-read-private", "user-read-email", "streaming")
    }
}
