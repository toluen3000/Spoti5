package com.example.spoti5.data.repositories

import android.content.Context
import android.util.Log
import com.example.spoti5.BuildConfig
import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.result.Result
import com.example.spoti5.data.result.safeApiCall
import com.example.spoti5.di.IoDispatcher
import com.example.spoti5.domain.model.player.DeviceModel
import com.example.spoti5.domain.model.player.RecentlyPlayedTrackModel
import com.example.spoti5.domain.model.player.UserQueueModel
import com.example.spoti5.domain.model.player.utils.PlaybackState
import com.example.spoti5.domain.repository.PlayerRepository
import com.example.spoti5.utils.RepeatMode
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class PlayerRepositoryImpl @Inject constructor(
    private val api: SpotifyApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context

): PlayerRepository {


    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val _playerStateFlow = MutableStateFlow(PlaybackState(
        isPlaying = false,
        trackName = "",
        artistName = "",
        albumName = "",
        albumImageUrl = null,
        trackUri = "",
        positionMs = 0L,
        durationMs = 0L
    ))

    override val playerStateFlow: StateFlow<PlaybackState>
        get() = _playerStateFlow

    override suspend fun connectSpotify() {
        val clientId = BuildConfig.SPOTIFY_CLIENT_ID
        val redirectUri = BuildConfig.SPOTIFY_REDIRECT_URI

        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(remote: SpotifyAppRemote) {
                spotifyAppRemote = remote
                subscribeToPlayerState()
                Log.d("MainActivity", "Connected to Spotify")
            }

            override fun onFailure(error: Throwable) {
                Log.e("Spotify", "Connection failed: ${error.localizedMessage}")
            }
        })
    }

    override suspend fun disconnectSpotify() {
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
            spotifyAppRemote = null
        }
    }

    private fun subscribeToPlayerState() {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback { playerState ->
            val track = playerState.track
            if (track != null) {
               val newState = PlaybackState(
                    isPlaying = !playerState.isPaused,
                    trackName = track.name,
                    artistName = track.artist.name,
                    albumName = track.album.name,
                    trackUri = track.uri,
                    positionMs = playerState.playbackPosition,
                    durationMs = track.duration
                )

                _playerStateFlow.value = newState

                spotifyAppRemote?.imagesApi
                    ?.getImage(track.imageUri, Image.Dimension.LARGE)
                    ?.setResultCallback { bitmap ->
                        // Update the album image in the player state
                        _playerStateFlow.value = newState.copy(albumImageUrl = bitmap)
                    }


            }
        }
    }


    override suspend fun play(uri: String) {
        spotifyAppRemote?.playerApi?.play(uri)
    }

    override suspend fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }

    override suspend fun resume() {
        spotifyAppRemote?.playerApi?.resume()
    }

    override suspend fun resumePlayback(deviceId: String): Result<Boolean> {
        return withContext(ioDispatcher) {
            safeApiCall {
                val response = api.resumePlayback()
                if (response.isSuccessful) {
                    Result.Success(true)
                } else {
                    Result.Error("Resume playback failed with code ${response.code()}")
                }
            }
        }
    }

    override suspend fun skipNext() {
        spotifyAppRemote?.playerApi?.skipNext()
    }

    override suspend fun skipPrevious() {
        spotifyAppRemote?.playerApi?.skipPrevious()
    }


    override suspend fun transferPlayback(
        deviceId: String,
        play: Boolean
    ): Result<Boolean> {
        return withContext(ioDispatcher) {
            safeApiCall {
               val response = api.transferPlayback(SpotifyApi.TransferPlaybackBody(listOf(deviceId),play))
                if (response.isSuccessful) {
                    Result.Success(true)
                } else {
                    Result.Error("Transfer playback failed with code ${response.code()}")
                }
            }
        }
    }

    override suspend fun getAvailableDevices(): Result<List<DeviceModel>> {
        return withContext(ioDispatcher){
            safeApiCall {
                val response = api.getAvailableDevices()
                Result.Success(response.devices.map { it.toDomainModel() } )
            }
        }
    }


    override suspend fun setRepeatMode(state: RepeatMode): Result<Boolean> {
        return withContext(ioDispatcher) {
            safeApiCall {
                val response = api.repeatMode(state.name)
                if (response.isSuccessful) {
                    Result.Success(true)
                } else {
                    Result.Error("Set repeat mode failed with code ${response.code()}")
                }
            }
        }
    }

    override suspend fun setPlaybackVolume(volumePercent: Int): Result<Boolean> {
        return withContext(ioDispatcher){
            safeApiCall {
                val response = api.playbackVolume(volumePercent)
                if (response.isSuccessful) {
                    Result.Success(true)
                } else {
                    Result.Error("Set playback volume failed with code ${response.code()}")
                }
            }
        }
    }

    override suspend fun toggleShuffleMode(state: Boolean): Result<Boolean> {
        return withContext(ioDispatcher){
            safeApiCall {
                val response = api.togglePlaybackShuffle(state)
                if (response.isSuccessful) {
                    Result.Success(true)
                } else {
                    Result.Error("Toggle shuffle mode failed with code ${response.code()}")
                }
            }
        }
    }

    override suspend fun getRecentTracks(): Result<RecentlyPlayedTrackModel> {
        return withContext(ioDispatcher) {
            safeApiCall {
                val response = api.getRecentlyPlayedTracks()
                Result.Success(response.toModel())
            }
        }
    }

    override suspend fun addToQueue(uri: String): Result<Boolean> {
        return withContext(ioDispatcher) {
            safeApiCall {
                val response = api.addToQueue(uri)
                if (response.isSuccessful) {
                    Result.Success(true)
                } else {
                    Result.Error("Add to queue failed with code ${response.code()}")
                }
            }
        }
    }

    override suspend fun getUserQueue(): Result<UserQueueModel> {
        return withContext(ioDispatcher){
            safeApiCall {
                val response = api.getUserQueue()
               Result.Success(response.toDomainModel())
            }
        }
    }
}