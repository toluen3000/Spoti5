package com.example.spoti5.data.repositories

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.spoti5.domain.repository.ExoplayerRepository
import com.example.spoti5.presentations.feature.play.UiState.PlayerUiState
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoplayerRepositoryImpl @Inject constructor(
    val exoPlayer: ExoPlayer
) : ExoplayerRepository {


    private val _playerState = MutableStateFlow<PlayerUiState>(PlayerUiState.Idle)
    override val playerState: StateFlow<PlayerUiState> = _playerState.asStateFlow()

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _playerState.value = if (isPlaying) {
                    PlayerUiState.Playing
                } else {
                    PlayerUiState.Paused
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                _playerState.value = when (state) {
                    Player.STATE_BUFFERING -> PlayerUiState.Loading
                    Player.STATE_READY -> if (exoPlayer.isPlaying) {
                        PlayerUiState.Playing
                    } else {
                        PlayerUiState.Paused
                    }
                    Player.STATE_ENDED -> PlayerUiState.Stopped
                    Player.STATE_IDLE -> PlayerUiState.Idle
                    else -> PlayerUiState.Idle
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                _playerState.value = PlayerUiState.Error(error.message ?: "Unknown error")
            }
        })
    }

    override suspend fun playTrack(url: String) {
        _playerState.value = PlayerUiState.Loading

        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override suspend fun pause() {
        exoPlayer.pause()
    }

    override suspend fun stop() {
        exoPlayer.stop()
    }


}