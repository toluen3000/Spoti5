package com.example.spoti5.domain.repository

import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.artist.ArtistDetailModel

interface FollowingArtistRepository {

    suspend fun getFollowingArtists(
        type: String = "artist",
        limit: Int = 20,
        after: String? = null
    ): Result<List<ArtistDetailModel>>

    suspend fun isFollowingArtist(artistId: String): Boolean

    suspend fun followArtist(artistId: String)

    suspend fun unfollowArtist(artistId: String)
}