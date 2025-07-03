package com.example.spoti5.domain.repository

import com.example.spoti5.domain.model.UserToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
//    suspend fun authenticate(): Result<AuthToken>
//    suspend fun getStoredToken(): AuthToken?
//    suspend fun saveToken(token: AuthToken)
//    suspend fun clearToken()
//    fun isTokenValid(token: AuthToken): Boolean

    fun exchangeCode(code: String): Flow<UserToken>
}