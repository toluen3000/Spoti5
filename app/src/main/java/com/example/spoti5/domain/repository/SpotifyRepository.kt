package com.example.spoti5.domain.repository

import com.example.spoti5.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

interface SpotifyRepository {
    suspend fun getCurrentUser(): Flow<UserModel>
}