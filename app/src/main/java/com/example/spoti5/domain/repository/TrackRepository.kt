package com.example.spoti5.domain.repository

import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.track.TrackModel

interface TrackRepository {


    suspend fun getTrackById(id: String): Result<TrackModel>

    suspend fun getSeveralTracksById(ids: String): Result<List<TrackModel>>

    suspend fun getUserSavedTracksByPage(
        limit: Int,
        offset: Int
    ): Result<List<TrackModel>>

    suspend fun saveTrackForCurrentUser(trackIds: String): Result<Boolean>

    suspend fun removeTrackFromUserLibrary(trackIds: String): Result<Boolean>

    suspend fun isTrackSavedForCurrentUser(trackId: String): Result<Boolean>

    suspend fun getRecommendedTracks(
        limit: Int = 20,
        seedTracks: String? = null
    ): Result<List<TrackModel>>



}