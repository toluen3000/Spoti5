package com.example.spoti5.data.repositories

import com.example.spoti5.data.remote.SpotifyAuthRemoteDataSource
import com.example.spoti5.domain.model.UserToken
import com.example.spoti5.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: SpotifyAuthRemoteDataSource
): AuthRepository {

    override fun exchangeCode(code: String): Flow<UserToken> = flow {
        val token = remoteDataSource.exchangeCode(code)
        emit(token)
    }

}