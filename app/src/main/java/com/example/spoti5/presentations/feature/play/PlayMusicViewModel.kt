package com.example.spoti5.presentations.feature.play

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.player.DeviceModel
import com.example.spoti5.domain.model.player.UserQueueModel
import com.example.spoti5.domain.model.player.utils.PlaybackState
import com.example.spoti5.domain.model.track.TrackModel
import com.example.spoti5.domain.repository.ExoplayerRepository
import com.example.spoti5.domain.repository.PlayerRepository
import com.example.spoti5.domain.repository.TrackRepository
import com.example.spoti5.presentations.feature.play.UiState.ItemUiState
import com.example.spoti5.presentations.feature.play.UiState.PlayerUiState
import com.example.spoti5.utils.RepeatMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

    fun previous() {
        viewModelScope.launch {
            playerRepository.skipPrevious()
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

    fun toggleShuffleButton(){
        viewModelScope.launch {
            playerRepository.toggleShuffleMode()
        }
    }

    fun seekTo(positionMs: Long) {
        viewModelScope.launch {
            playerRepository.seekTo(positionMs)
        }
    }

    // update seekbar with playback state
    val playerState: StateFlow<PlaybackState> = playerRepository.playerStateFlow

    fun startSeekBarLoop() {
        viewModelScope.launch {
            playerRepository.startSeekBarLoop()
        }
    }


    //---------------------------------------------------------------------------------------


    // Using Web API for playback state

    private val _playbackState = MutableStateFlow<PlayerUiState>(PlayerUiState.Idle)
    val playbackState: StateFlow<PlayerUiState> = _playbackState.asStateFlow()

    init {
        // Observe playerState real-time từ SDK callback
        viewModelScope.launch {
            playerRepository.playerStateFlow.collect { state ->
                _playbackState.value = PlayerUiState.Success(state)
            }
        }
    }

    // Fetch infor

    private val _inforPlaybackState = MutableStateFlow<ItemUiState<List<DeviceModel>>>(ItemUiState.Loading)
    val inforPlaybackState: StateFlow<ItemUiState<List<DeviceModel>>> = _inforPlaybackState.asStateFlow()

    // Call Web API to get devices,
    fun fetchAvailableDevices() {
        viewModelScope.launch {
            _inforPlaybackState.value = ItemUiState.Loading
            val result = playerRepository.getAvailableDevices()
            _inforPlaybackState.value = when(result) {
                Result.Empty -> ItemUiState.Empty
                is Result.Error -> { ItemUiState.Error(result.message ?: "Unknown error") }
                is Result.Success -> { ItemUiState.Success(result.data) }
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
                 val result = trackRepository.getTrackById(trackId)

                _trackState.value = when (result) {
                    Result.Empty -> ItemUiState.Empty
                    is Result.Error -> ItemUiState.Error(result.message ?: "Unknown error")
                    is Result.Success -> ItemUiState.Success(result.data)
                }
            }
        }

    // fetch user queue
    private val _userQueueState = MutableStateFlow<ItemUiState<UserQueueModel>>(
        ItemUiState.Loading)
    val userQueueState: StateFlow<ItemUiState<UserQueueModel>> = _userQueueState.asStateFlow()

    fun fetchUserQueue() {
        viewModelScope.launch {
            _userQueueState.value = ItemUiState.Loading
            when (val result = playerRepository.getUserQueue()) {
                Result.Empty -> {
                    _userQueueState.value = ItemUiState.Empty
                }
                is Result.Error -> {
                    _userQueueState.value = ItemUiState.Error(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    _userQueueState.value = ItemUiState.Success(result.data)

                }
            }
        }
    }


    // toggle repeat mode

    private val _repeatModeState = MutableStateFlow<ItemUiState<Boolean>>(ItemUiState.Loading)
    val repeatModeState: StateFlow<ItemUiState<Boolean>> = _repeatModeState.asStateFlow()

    fun setRepeatMode(state: String, deviceId: String) {
        viewModelScope.launch {
            _repeatModeState.value = ItemUiState.Loading
            when (val result = playerRepository.setRepeatMode(state, deviceId)) {
                Result.Empty -> {
                    _repeatModeState.value = ItemUiState.Empty
                }
                is Result.Error -> {
                    _repeatModeState.value = ItemUiState.Error(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    _repeatModeState.value = ItemUiState.Success(result.data)
                }
            }
        }
    }


    // transfer playback

    private val _transPlayback = MutableStateFlow<ItemUiState<Boolean>>(ItemUiState.Loading)
    val tramsPlayback: StateFlow<ItemUiState<Boolean>> = _transPlayback.asStateFlow()

    fun transferPlayback(deviceId: SpotifyApi.TransferPlaybackBody) {
        viewModelScope.launch {
            _transPlayback.value = ItemUiState.Loading

            when ( val result = playerRepository.transferPlayback(deviceId)) {
                Result.Empty -> {
                    Log.d("Trans Playback","Empty")
                    _transPlayback.value = ItemUiState.Empty
                }
                is Result.Error -> {
                    _transPlayback.value = ItemUiState.Error(result.message ?: "Unknown")
                }
                is Result.Success -> {
                    _transPlayback.value = ItemUiState.Success(result.data)
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