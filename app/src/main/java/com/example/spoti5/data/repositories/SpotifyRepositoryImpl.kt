package com.example.spoti5.data.repositories

import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.dto.UserDto
import com.example.spoti5.domain.model.UserModel
import com.example.spoti5.domain.repository.SpotifyRepository
import com.example.spoti5.utils.SharePrefUtils
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SpotifyRepositoryImpl @Inject constructor(
    private val api: SpotifyApi,
): SpotifyRepository {

    override suspend fun getCurrentUser(): Flow<UserModel> = flow {
        val userDto = api.getCurrentUser()
        emit(userDto.toDomainModel())
    }.flowOn(Dispatchers.IO)

}