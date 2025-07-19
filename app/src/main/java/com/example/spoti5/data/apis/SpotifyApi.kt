package com.example.spoti5.data.apis

import com.example.spoti5.data.dto.UserDto
import com.example.spoti5.data.dto.album.AlbumDto
import com.example.spoti5.data.dto.album.ArtistDto
import com.example.spoti5.data.dto.album.NewAlbumsReleaseDto
import com.example.spoti5.data.dto.album.TracksDto
import com.example.spoti5.data.dto.artist.ArtistAlbumsResponseDto
import com.example.spoti5.data.dto.artist.ArtistDetailDto
import com.example.spoti5.data.dto.artist.ArtistTopTracksResponseDto
import com.example.spoti5.data.dto.artist.RelatedArtistDto
import com.example.spoti5.data.dto.artist.RelatedArtistsResponseDto
import com.example.spoti5.data.dto.artist.SeveralArtistsResponseDto
import com.example.spoti5.data.dto.library.UserSavedAlbumDto
import com.example.spoti5.data.result.BaseResponse
import com.example.spoti5.domain.model.album.ArtistModel
import com.example.spoti5.domain.model.artist.ArtistDetailModel
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApi {

    @GET("me")
    suspend fun getCurrentUser (
    ): UserDto



    @GET("me/player/currently-playing")
    suspend fun getCurrentlyPlaying(): BaseResponse<UserDto>


    @GET("albums/{id}")
    suspend fun getAlbumById(
        @Path("id") albumId: String
    ): AlbumDto

    @GET("albums/{id}/tracks")
    suspend fun getAlbumTracks(
        @Path("id") albumId: String
    ): TracksDto

//    @GET("browse/new-releases")
//    suspend fun getNewAlbumsRelease(
//    ): BaseResponse<NewAlbumsReleaseDto>

    @GET("browse/new-releases")
    suspend fun getNewAlbumsRelease(
    ): NewAlbumsReleaseDto

    // user saved albums
    @GET("me/albums")
    suspend fun getUserSavedAlbums(
    ): UserSavedAlbumDto

    @PUT("me/albums")
    suspend fun saveAlbumToUserLibrary(
        @Query("ids") albumIds: String
    ): Response<Unit>

    @DELETE("me/albums")
    suspend fun deleteAlbumFromUserLibrary(
        @Query("ids") albumIds: String
    ): Response<Unit>

    // artist api

    @GET("artists/{id}")
    suspend fun getArtistById(
        @Path("id") artistId: String
    ): ArtistDetailDto

    @GET("artists")
    suspend fun getSeveralArtistsById(
        @Query("ids") artistIds: String
    ): SeveralArtistsResponseDto

    @GET("artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Path("id") artistId: String,
        @Query("include_groups") includeGroups: String = "album,single,compilation",
        @Query("limit") limit: Int = 50
    ): ArtistAlbumsResponseDto


//    @GET("artists/{id}/top-tracks")
//    suspend fun getArtistTopTracks(
//        @Path("id") artistId: String,
//    ): Response<TracksDto>

    @GET("artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(
        @Path("id") artistId: String,
    ): ArtistTopTracksResponseDto

    @GET("artists/{id}/related-artists")
    suspend fun getArtistRelatedArtists(
        @Path("id") artistId: String
    ): RelatedArtistsResponseDto



}