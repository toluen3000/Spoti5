package com.example.spoti5.data.apis

import com.example.spoti5.data.dto.UserDto
import com.example.spoti5.data.dto.album.AlbumDto
import com.example.spoti5.data.dto.album.NewAlbumsReleaseDto
import com.example.spoti5.data.dto.album.TracksDto
import com.example.spoti5.data.result.BaseResponse
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.UserModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SpotifyApi {

    @GET("me")
    suspend fun getCurrentUser (
    ): UserDto



    @GET("me/player/currently-playing")
    suspend fun getCurrentlyPlaying(): BaseResponse<UserDto>


    @GET("albums/{id}")
    suspend fun getAlbumById(
        @Path("id") albumId: String
    ): BaseResponse<AlbumDto>

    @GET("albums/{id}/tracks")
    suspend fun getAlbumTracks(
        albumId: String
    ): BaseResponse<TracksDto>

//    @GET("browse/new-releases")
//    suspend fun getNewAlbumsRelease(
//    ): BaseResponse<NewAlbumsReleaseDto>

    @GET("browse/new-releases")
    suspend fun getNewAlbumsRelease(
    ): NewAlbumsReleaseDto
}