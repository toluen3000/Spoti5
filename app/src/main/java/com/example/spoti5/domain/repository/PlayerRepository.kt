package com.example.spoti5.domain.repository

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

    val playerStateFlow: StateFlow<PlaybackState>

    suspend  fun startSeekBarLoop()

    suspend fun transferPlayback(deviceId: String, play: Boolean): Result<Boolean>

    suspend fun getAvailableDevices(): Result<List<DeviceModel>>

    suspend fun setRepeatMode(state: RepeatMode): Result<Boolean>

    suspend fun setPlaybackVolume(volumePercent: Int): Result<Boolean>

    suspend fun toggleShuffleMode(state: Boolean): Result<Boolean>

    suspend fun getRecentTracks(): Result<RecentlyPlayedTrackModel>

    suspend fun addToQueue(uri: String): Result<Boolean>

    suspend fun getUserQueue(): Result<UserQueueModel>


}