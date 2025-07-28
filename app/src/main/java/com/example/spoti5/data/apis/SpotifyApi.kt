package com.example.spoti5.data.apis

import DeviceDto
import PlaybackStateDto
import com.example.spoti5.data.dto.UserDto
import com.example.spoti5.data.dto.album.AlbumDto
import com.example.spoti5.data.dto.album.NewAlbumsReleaseDto
import com.example.spoti5.data.dto.album.TracksDto
import com.example.spoti5.data.dto.artist.ArtistAlbumsResponseDto
import com.example.spoti5.data.dto.artist.ArtistDetailDto
import com.example.spoti5.data.dto.artist.ArtistTopTracksResponseDto
import com.example.spoti5.data.dto.artist.RelatedArtistsResponseDto
import com.example.spoti5.data.dto.artist.SeveralArtistsResponseDto
import com.example.spoti5.data.dto.library.UserSavedAlbumDto
import com.example.spoti5.data.dto.player.CurrentPlayingTrackDto
import com.example.spoti5.data.dto.player.RecentlyPlayedTrackResponseDto
import com.example.spoti5.data.dto.player.UserQueueDto
import com.example.spoti5.data.dto.track.TrackDto
import com.example.spoti5.data.dto.track.UserSavedTrackDto
import com.example.spoti5.data.dto.track.UserSavedTrackResponseDto
import com.example.spoti5.data.result.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SpotifyApi {

    @GET("me")
    suspend fun getCurrentUser (
    ): UserDto



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

    //------------------------------------------------------------------------------------------------
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

    //------------------------------------------------------------------------------------------------
    // Player API
    data class TransferPlaybackBody(
        val device_ids: List<String>,
        val play: Boolean = true
    )

    @PUT("me/player")
    suspend fun transferPlayback(
        @Body body: TransferPlaybackBody
    ): Response<Unit>

    @GET("/me/player/devices")
    suspend fun getAvailableDevices(): List<DeviceDto>


    @PUT("me/player/repeat")
    suspend fun repeatMode(
        @Query("state") state: String,
        @Query("device_id") deviceId: String? = null
    ): Response<Unit>

    @PUT("me/player/volume")
    suspend fun playbackVolume(
        @Query("volume_percent") volumePercent: Int,
        @Query("device_id") deviceId: String? = null
    ): Response<Unit>

    @PUT("me/player/shuffle")
    suspend fun togglePlaybackShuffle(
        @Query("state") state: Boolean,
        @Query("device_id") deviceId: String? = null
    ): Response<Unit>


    @GET("me/player/recently-played")
    suspend fun getRecentlyPlayedTracks(
        @Query("limit") limit: Int = 20,
        @Query("before") before: String? = null,
        @Query("after") after: String? = null
    ): RecentlyPlayedTrackResponseDto


    @GET("me/player/queue")
    suspend fun getUserQueue(
        @Query("device_id") deviceId: String? = null
    ): UserQueueDto


    @POST("me/player/queue")
    suspend fun addToQueue(
        @Query("uri") uri: String,
        @Query("device_id") deviceId: String? = null
    ): Response<Unit>


    // ------------------------------------------------------------------------------------------------
    // track api

    @GET("tracks/{id}")
    suspend fun getTrackById(
        @Path("id") trackId: String
    ): TrackDto

    @GET("tracks")
    suspend fun getSeveralTracksById(
        @Query("ids") trackIds: String
    ): List<TrackDto>

    @GET("me/tracks")
    suspend fun getUserSavedTracks(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): UserSavedTrackResponseDto

    @PUT("me/tracks")
    suspend fun saveTrackForCurrentUser(
        @Query("ids") trackIds: String
    ): Response<Unit>

    @DELETE("me/tracks")
    suspend fun removeTrackFromUserLibrary(
        @Query("ids") trackIds: String
    ): Response<Unit>

    @GET("me/tracks/contains")
    suspend fun checkUserSavedTracks(
        @Query("ids") trackIds: String
    ): Response<Boolean>

    @GET("recommendations")
    suspend fun getRecommendedTracks(
        @Query("limit") limit: Int = 20,
        @Query("seed_artists") seedArtists: String? = null,
        @Query("seed_genres") seedGenres: String? = null,
        @Query("seed_tracks") seedTracks: String? = null
    ): Response<TrackDto>








}