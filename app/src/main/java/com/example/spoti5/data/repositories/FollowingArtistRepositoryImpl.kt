package com.example.spoti5.data.repositories

import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.result.Result
import com.example.spoti5.data.result.safeApiCall
import com.example.spoti5.di.IoDispatcher
import com.example.spoti5.domain.model.artist.ArtistDetailModel
import com.example.spoti5.domain.repository.FollowingArtistRepository
import com.google.android.play.core.integrity.t
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FollowingArtistRepositoryImpl @Inject constructor(
    private val api: SpotifyApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FollowingArtistRepository {
    override suspend fun getFollowingArtists(
        type: String,
        limit: Int,
        after: String?
    ): Result<List<ArtistDetailModel>> {
        return withContext(ioDispatcher) {
            safeApiCall {
                val response = api.getUserFollowingArtists(type = type,limit = limit, after = after)
                Result.Success(response.artists.items.map { it.toDomainModel() })
            }
        }
    }

    override suspend fun isFollowingArtist(artistId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun followArtist(artistId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun unfollowArtist(artistId: String) {
        TODO("Not yet implemented")
    }
}