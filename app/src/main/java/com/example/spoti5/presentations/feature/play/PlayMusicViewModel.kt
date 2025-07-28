package com.example.spoti5.presentations.feature.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.player.PlaybackStateModel
import com.example.spoti5.domain.model.track.TrackModel
import com.example.spoti5.domain.repository.ExoplayerRepository
import com.example.spoti5.domain.repository.PlayerRepository
import com.example.spoti5.domain.repository.TrackRepository
import com.example.spoti5.presentations.feature.play.UiState.ItemUiState
import com.example.spoti5.presentations.feature.play.UiState.PlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayMusicViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val trackRepository: TrackRepository,
    private val exoPlayerRepository: ExoplayerRepository
) : ViewModel() {


    //---------------------------------------------------------------------------------------
    // Playback state for the current track (Spotify web api ) // not using ExoPlayer for Spotify tracks
    private val _playbackState = MutableStateFlow<PlayerUiState>(PlayerUiState.Idle)
    val playbackState: StateFlow<PlayerUiState> = _playbackState.asStateFlow()


    fun connectSpotify() {
        viewModelScope.launch {
            playerRepository.connectSpotify()
        }
    }

    fun disconnectSpotify() {
        viewModelScope.launch {
            playerRepository.disconnectSpotify()
        }
    }

    fun play(uri: String) {
        viewModelScope.launch {
            playerRepository.play(uri)
        }
    }

    fun pause() {
        viewModelScope.launch {
            playerRepository.pause()
        }
    }

    fun next() {
        viewModelScope.launch {
            playerRepository.skipNext()
        }
    }

    init {
        // Observe playerState real-time từ SDK callback
        viewModelScope.launch {
            playerRepository.playerStateFlow.collect { state ->
                _playbackState.value = PlayerUiState.Success(state)
            }
        }
    }


    //---------------------------------------------------------------------------------------
    // Track state for fetching track details
    private val _trackState = MutableStateFlow<ItemUiState<TrackModel>>(
        ItemUiState.Loading)

     val trackState : StateFlow<ItemUiState<TrackModel>> = _trackState.asStateFlow()


        fun fetchTrackById(trackId: String) {
            viewModelScope.launch {
                 _trackState.value = ItemUiState.Loading
                 when (val result = trackRepository.getTrackById(trackId)) {
                     Result.Empty -> {
                            _trackState.value = ItemUiState.Empty
                     }
                     is Result.Error -> {
                            _trackState.value = ItemUiState.Error(result.message ?: "Unknown error")
                     }
                     is Result.Success -> {
                            _trackState.value = ItemUiState.Success(result.data)
                     }
                 }
            }
        }



    // spotify is not providing preview url for tracks, so i will use ExoPlayer for local playback // not in this VM
//    // Player state from ExoPlayerRepository
//    val playState: StateFlow<PlayerUiState> = exoPlayerRepository.playerState
//
//    // Player control methods - for Local Playback
//    fun playTrack() {
//        val currentTrack = (_trackState.value as? ItemUiState.Success)?.data
//        val previewUrl = currentTrack?.previewUrl
//
//        if (!previewUrl.isNullOrEmpty()) {
//            viewModelScope.launch {
//                exoPlayerRepository.playTrack(previewUrl)
//            }
//        }
//    }
//
//    fun pauseTrack() {
//        viewModelScope.launch {
//            exoPlayerRepository.pause()
//        }
//    }
//
//    fun stopTrack() {
//        viewModelScope.launch {
//            exoPlayerRepository.stop()
//        }
//    }

}