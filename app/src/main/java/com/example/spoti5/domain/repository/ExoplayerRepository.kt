package com.example.spoti5.domain.repository

import com.example.spoti5.presentations.feature.play.UiState.PlayerUiState
import kotlinx.coroutines.flow.StateFlow

interface ExoplayerRepository {

    val playerState: StateFlow<PlayerUiState>

    suspend fun playTrack(url: String)
    suspend fun pause()
    suspend fun stop()

//    suspend fun seekTo(position: Long)
//
//    suspend fun getCurrentPosition(): Long
//
//    suspend fun getDuration(): Long
//
//    suspend fun isPlaying(): Boolean
//
//    suspend fun setVolume(volume: Float)
//
//    suspend fun releasePlayer()
//
//    suspend fun nextTrack()
//
//    suspend fun previousTrack()


}