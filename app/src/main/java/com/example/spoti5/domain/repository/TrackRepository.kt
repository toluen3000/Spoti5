package com.example.spoti5.domain.repository

import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.track.TrackModel
import com.example.spoti5.domain.model.track.UserSavedTrackModel

interface TrackRepository {


    suspend fun getTrackById(id: String): Result<TrackModel>

    suspend fun getSeveralTracksById(ids: String): Result<List<TrackModel>>

    suspend fun getUserSavedTracksByPage(
        limit: Int,
        offset: Int
    ): Result<List<TrackModel>>

    suspend fun saveTrackForCurrentUser(trackIds: String): Result<Unit>

    suspend fun removeTrackFromUserLibrary(trackIds: String): Result<Unit>

    suspend fun isTrackSavedForCurrentUser(trackId: String): Result<Boolean>

    suspend fun getRecommendedTracks(
        limit: Int = 20,
        market: String? = null,
        seedArtists: String? = null,
        seedGenres: String? = null,
        seedTracks: String? = null
    ): Result<List<TrackModel>>



}