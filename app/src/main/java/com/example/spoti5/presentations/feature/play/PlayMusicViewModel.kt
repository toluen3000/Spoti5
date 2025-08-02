package com.example.spoti5.presentations.feature.play

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.player.DeviceModel
import com.example.spoti5.domain.model.track.TrackModel
import com.example.spoti5.domain.repository.ExoplayerRepository
import com.example.spoti5.domain.repository.PlayerRepository
import com.example.spoti5.domain.repository.TrackRepository
import com.example.spoti5.presentations.feature.play.UiState.ItemUiState
import com.example.spoti5.presentations.feature.play.UiState.PlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayMusicViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val trackRepository: TrackRepository,
    private val exoPlayerRepository: ExoplayerRepository
) : ViewModel() {


    //---------------------------------------------------------------------------------------
    // Playback state for the current track (Spotify SDK ) // not using ExoPlayer for Spotify tracks

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

    fun resume(useSdk: Boolean = true) {
        viewModelScope.launch {
            if (useSdk) {
                playerRepository.resume()
            } else {

                val result = playerRepository.resumePlayback("id")
                Log.d("ViewModel", "Resume via API: $result")
            }
        }
    }

    //---------------------------------------------------------------------------------------
    // Using Web API for playback state

    private val _playbackState = MutableStateFlow<PlayerUiState>(PlayerUiState.Idle)
    val playbackState: StateFlow<PlayerUiState> = _playbackState.asStateFlow()

    // Fetch infor

    private val _inforPlaybackState = MutableStateFlow<ItemUiState<List<DeviceModel>>>(ItemUiState.Loading)
    val inforPlaybackState: StateFlow<ItemUiState<List<DeviceModel>>> = _inforPlaybackState.asStateFlow()

    // Call Web API to get devices,
    fun fetchAvailableDevices() {
        viewModelScope.launch {
            _inforPlaybackState.value = ItemUiState.Loading
            when (val result = playerRepository.getAvailableDevices()) {
                Result.Empty -> {
                    _inforPlaybackState.value = ItemUiState.Empty
                }
                is Result.Error -> {
                    _inforPlaybackState.value = ItemUiState.Error(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    _inforPlaybackState.value = ItemUiState.Success(result.data)
                }
            }
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