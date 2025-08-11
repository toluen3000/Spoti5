package com.example.spoti5.data.repositories

import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.dto.track.RecommendationResponse
import com.example.spoti5.data.result.Result
import com.example.spoti5.data.result.safeApiCall
import com.example.spoti5.di.IoDispatcher
import com.example.spoti5.domain.model.track.TrackModel
import com.example.spoti5.domain.model.track.UserSavedTrackModel
import com.example.spoti5.domain.repository.TrackRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor (
    val api: SpotifyApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): TrackRepository {


    override suspend fun getTrackById(id: String): Result<TrackModel> {
        return withContext(ioDispatcher){
            safeApiCall {
                val response = api.getTrackById(id)
                Result.Success(response.toDomainModel())
            }
        }
    }

    override suspend fun getSeveralTracksById(ids: String): Result<List<TrackModel>> {
        return withContext(ioDispatcher){
            safeApiCall {
                val response = api.getSeveralTracksById(ids)
                Result.Success(response.map { it.toDomainModel() })
            }
        }
    }


    override suspend fun getUserSavedTracksByPage(
        limit: Int,
        offset: Int
    ): Result<List<TrackModel>> {
        return withContext(ioDispatcher){
            safeApiCall {
                val response = api.getUserSavedTracks(limit, offset)
                Result.Success(response.items.map { it.track.toDomainModel() })
            }
        }
    }

    override suspend fun saveTrackForCurrentUser(trackIds: String): Result<Boolean> {
        return withContext(ioDispatcher){
            val response = api.saveTrackForCurrentUser(trackIds)

            if (response.isSuccessful){
                Result.Success(true)
            }else {
                Result.Error("Save failed with code ${response.code()}")
            }

        }
    }

    override suspend fun removeTrackFromUserLibrary(trackIds: String): Result<Boolean> {
        return withContext(ioDispatcher){
            val response = api.removeTrackFromUserLibrary(trackIds)

            if (response.isSuccessful){
                Result.Success(true)
            }else{
                Result.Error("Delete failed with code ${response.code()}")
            }
        }
    }

    override suspend fun isTrackSavedForCurrentUser(trackId: String): Result<Boolean> {
        return withContext(ioDispatcher){
            val response = api.getUserSavedTracks()
            if (response.items.any { it.track.id == trackId }){
                Result.Success(true)
            }else{
                Result.Success(false)
            }
        }
    }

    override suspend fun getRecommendedTracks(
        limit: Int,
        seedTracks: String?
    ): Result<List<TrackModel>> {
        return withContext(ioDispatcher){
            safeApiCall {
                val response = api.getRecommendedTracks(limit, seedTracks)
                Result.Success(response.tracks!!.map { it.toDomainModel()})
            }
        }
    }


}