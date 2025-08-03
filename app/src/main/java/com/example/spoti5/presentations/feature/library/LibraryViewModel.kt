package com.example.spoti5.presentations.feature.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.domain.model.library.SavedAlbumItemModel
import com.example.spoti5.domain.repository.UserSavedAlbumRepository
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.artist.ArtistDetailModel
import com.example.spoti5.domain.model.track.TrackModel
import com.example.spoti5.domain.model.track.UserSavedTrackModel
import com.example.spoti5.domain.repository.FollowingArtistRepository
import com.example.spoti5.domain.repository.TrackRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val userSavedAlbumRepository: UserSavedAlbumRepository,
    private val userSavedTrackRepository: TrackRepository,
    private val userSavedArtistRepository: FollowingArtistRepository
): ViewModel() {

    // album state

    private val _albumsSavedState = MutableStateFlow<MainUiState<List<SavedAlbumItemModel>>>(
        MainUiState.Loading)
    val albumsSavedState : StateFlow<MainUiState<List<SavedAlbumItemModel>>> = _albumsSavedState.asStateFlow()

    fun fetchUserSavedAlbums() {
        viewModelScope.launch {
            _albumsSavedState.value = MainUiState.Loading

            val result = userSavedAlbumRepository.getUserSavedAlbums()
            when (result) {


                Result.Empty -> {
                    _albumsSavedState.value = MainUiState.Error("No albums found")
                }
                is Result.Error -> {
                    _albumsSavedState.value = MainUiState.Error(result.message ?: "An error occurred")
                }
                is Result.Success -> {
                    _albumsSavedState.value = MainUiState.Success(result.data)
                }
            }
        }
    }

    // artist state

    private val _artistsSavedState = MutableStateFlow<MainUiState<List<ArtistDetailModel>>>(
        MainUiState.Loading)
    val artistsSavedState: StateFlow<MainUiState<List<ArtistDetailModel>>> = _artistsSavedState.asStateFlow()

    fun fetchUserSavedArtists() {
        viewModelScope.launch {
            _artistsSavedState.value = MainUiState.Loading

            val result = userSavedArtistRepository.getFollowingArtists(
                type = "artist",
                limit = 20,
                after = null
            )
            when (result) {
                Result.Empty -> {
                    _artistsSavedState.value = MainUiState.Error("No artists found")
                }
                is Result.Error -> {
                    _artistsSavedState.value = MainUiState.Error(result.message ?: "An error occurred")
                }
                is Result.Success -> {
                    _artistsSavedState.value = MainUiState.Success(result.data)
                    Log.d("LibraryViewModel", "Fetched user saved artists: ${result.data}")
                }
            }
        }
    }





    // track state
    private val _userTracksSavedState = MutableStateFlow<MainUiState<List<TrackModel>>>(
        MainUiState.Loading)
    val userTracksSavedState: StateFlow<MainUiState<List<TrackModel>>> = _userTracksSavedState.asStateFlow()


    fun fetchUserSavedTracks() {
        viewModelScope.launch {
            _userTracksSavedState.value = MainUiState.Loading

            val result = userSavedTrackRepository.getUserSavedTracksByPage(
                limit = 20,
                offset = 0
            )
            when (result) {
                Result.Empty -> {
                    _userTracksSavedState.value = MainUiState.Error("No tracks found")
                }
                is Result.Error -> {
                    _userTracksSavedState.value = MainUiState.Error(result.message ?: "An error occurred")
                }
                is Result.Success -> {
                    _userTracksSavedState.value = MainUiState.Success(result.data)
                    Log.d("LibraryViewModel", "Fetched user saved tracks: ${result.data}")
                }
            }
        }
    }



}