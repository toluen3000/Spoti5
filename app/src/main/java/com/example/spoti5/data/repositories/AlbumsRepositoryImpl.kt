package com.example.spoti5.data.repositories

import android.util.Log
import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.dto.album.NewAlbumsReleaseDto
import com.example.spoti5.data.result.Result
import com.example.spoti5.data.result.safeApiCall
import com.example.spoti5.data.result.toResult
import com.example.spoti5.di.IoDispatcher
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.model.album.NewAlbumsReleaseModel
import com.example.spoti5.domain.model.album.TracksModel
import com.example.spoti5.domain.repository.AlbumsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.onSuccess


class AlbumsRepositoryImpl @Inject constructor(
    private val api: SpotifyApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
): AlbumsRepository {
    override suspend fun getAlbumById(albumId: String): Result<AlbumModel> {
        return withContext(dispatcher) {
            safeApiCall {
                api.getAlbumById(albumId)
                    .toResult{it.toDomainModel()}
            }
        }

    }

    override suspend fun getAlbumTracks(albumId: String): Result<TracksModel> {
        return withContext(dispatcher) {
            safeApiCall {
                api.getAlbumTracks(albumId)
                    .toResult { it.toDomainModel() }
            }
        }
    }

    override suspend fun getNewAlbumsRelease(): Result<List<AlbumModel>> {
        return withContext(dispatcher) {
            safeApiCall {
                val response = api.getNewAlbumsRelease()
                Result.Success(response.albums.items.map { it.toDomainModel() })
            }
        }
    }

        // Successfully fetch new albums release -> json but BaseResponse don't have status TYPE like Success or Error

//    override suspend fun getNewAlbumsRelease(): Result<NewAlbumsReleaseModel> {
//        return withContext(dispatcher) {
//            safeApiCall {
//                api.getNewAlbumsRelease()
//                    .toResult { it.toDomainModel() }
//            }
//        }
//    }

//    override suspend fun getNewAlbumsRelease(): Result<List<NewAlbumsReleaseModel>> {
//        return withContext(dispatcher) {
//            safeApiCall {
//                api.getNewAlbumsRelease()
//                  .toResult { it.map(NewAlbumsReleaseDto::toDomainModel) }
//
//
//            }
//        }
//    }


}