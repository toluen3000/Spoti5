package com.example.spoti5.data.repositories

import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.result.Result
import com.example.spoti5.data.result.safeApiCall
import com.example.spoti5.di.IoDispatcher
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.model.album.ArtistModel
import com.example.spoti5.domain.model.album.TrackItemModel
import com.example.spoti5.domain.model.artist.ArtistDetailModel
import com.example.spoti5.domain.model.artist.RelatedArtistModel
import com.example.spoti5.domain.repository.ArtistDetailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtistDetailRepositoryImpl @Inject constructor (
    private val api: SpotifyApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ArtistDetailRepository {


    override suspend fun getArtistDetail(artistId: String): Result<ArtistDetailModel> {
        return withContext(dispatcher){
            safeApiCall {
                val response = api.getArtistById(artistId)

                if (response != null) {
                    Result.Success(response.toDomainModel())
                } else {
                    Result.Error("Failed to fetch artist details")
                }
            }
        }
    }

    override suspend fun getArtistTopTracks(artistId: String): Result<List<TrackItemModel>> {
        return withContext(dispatcher){
            safeApiCall {
                val response = api.getArtistTopTracks(artistId)
                Result.Success(response.tracks?.map { it.toDomainModel() } ?: emptyList())
            }
        }
    }

    override suspend fun getArtistRelatedArtists(artistId: String): Result<List<RelatedArtistModel>> {
        return withContext(dispatcher){
            safeApiCall {
                val response = api.getArtistRelatedArtists(artistId)
                Result.Success(response.artists.map { it.toDomainModel()})

            }
        }
    }

    override suspend fun getArtistAlbums(artistId: String): Result<List<AlbumModel>> {
        return withContext(dispatcher) {
            safeApiCall {
                val response = api.getArtistAlbums(artistId)
                Result.Success(response.items?.map { it.toDomainModel()} ?: emptyList())
            }
        }
    }

    override suspend fun getSeveralArtistsById(artistIds: String): Result<List<ArtistDetailModel>> {
        return withContext(dispatcher) {
            safeApiCall {
                val response = api.getSeveralArtistsById(artistIds)
                Result.Success(response.artists.map { it.toDomainModel() })
            }
        }
    }
}