package com.example.spoti5.domain.repository

import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.model.album.TrackItemModel

interface AlbumsRepository {

    suspend fun getAlbumById(albumId: String): Result<AlbumModel>

    suspend fun getAlbumTracks(albumId: String): Result<List<TrackItemModel>>

//    suspend fun getNewAlbumsRelease(): Result<List<NewAlbumsReleaseModel>>

//    suspend fun getNewAlbumsRelease(): Result<NewAlbumsReleaseModel>

    suspend fun getNewAlbumsRelease(): Result<List<AlbumModel>>

//    suspend fun getNewAlbumsRelease(): Result<List<AlbumsModel>>

    suspend fun saveAlbumToUserLibrary(albumId: String): Result<Boolean>

    suspend fun deleteAlbumFromUserLibrary(albumId: String): Result<Boolean>

    suspend fun checkIfAlbumIsSaved(albumId: String): Result<Boolean>
}