package com.example.spoti5.domain.repository

import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.player.DeviceModel
import com.example.spoti5.domain.model.player.RecentlyPlayedTrackModel
import com.example.spoti5.domain.model.player.UserQueueModel
import com.example.spoti5.domain.model.player.utils.PlaybackState
import com.example.spoti5.utils.RepeatMode
import kotlinx.coroutines.flow.StateFlow


interface PlayerRepository {

    suspend fun connectSpotify()

    suspend fun disconnectSpotify()

    suspend fun play(uri: String)

    suspend fun pause()

    suspend fun resume()

    suspend fun resumePlayback( deviceId: String ):Result<Boolean>

    suspend fun skipNext()

    suspend fun skipPrevious()

    suspend fun seekTo(positionMs: Long)

    val playerStateFlow: StateFlow<PlaybackState>

    suspend  fun startSeekBarLoop()

    suspend fun transferPlayback(deviceId: SpotifyApi.TransferPlaybackBody): Result<Boolean>

    suspend fun getAvailableDevices(): Result<List<DeviceModel>>

    suspend fun setRepeatMode(state: String, deviceId: String): Result<Boolean>

    suspend fun setPlaybackVolume(volumePercent: Int): Result<Boolean>

    suspend fun toggleShuffleMode()

    suspend fun getRecentTracks(): Result<RecentlyPlayedTrackModel>

    suspend fun addToQueue(uri: String): Result<Boolean>

    suspend fun getUserQueue(): Result<UserQueueModel>


}