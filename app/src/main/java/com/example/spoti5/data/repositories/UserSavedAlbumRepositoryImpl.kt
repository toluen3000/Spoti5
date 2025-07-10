package com.example.spoti5.data.repositories

import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.result.safeApiCall
import com.example.spoti5.di.IoDispatcher
import com.example.spoti5.domain.model.library.SavedAlbumItemModel
import com.example.spoti5.domain.repository.UserSavedAlbumRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.example.spoti5.data.result.Result
import javax.inject.Inject

class UserSavedAlbumRepositoryImpl @Inject constructor(
    private val api: SpotifyApi ,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): UserSavedAlbumRepository {
    override suspend fun getUserSavedAlbums(): Result<List<SavedAlbumItemModel>> {
        return withContext(dispatcher) {
            safeApiCall {
                val response = api.getUserSavedAlbums()
                Result.Success(response.items?.map { it.toDomainModel() } ?: emptyList())
            }
        }
    }


}