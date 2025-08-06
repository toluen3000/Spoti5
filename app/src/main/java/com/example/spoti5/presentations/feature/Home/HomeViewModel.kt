package com.example.spoti5.presentations.feature.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.domain.model.UserModel
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.repository.AlbumsRepository
import com.example.spoti5.domain.repository.SpotifyRepository
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.album.ArtistModel
import com.example.spoti5.domain.repository.PlayerRepository
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState.*
import java.time.LocalDate
import java.time.ZonedDateTime

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    private val albumRepository: AlbumsRepository,
    private val playerRepository: PlayerRepository,
) : ViewModel() {

    // StateFlow to hold the user state

    private val _userState= MutableStateFlow<MainUiState<UserModel?>>(
        MainUiState.Loading
    )
    val userState: StateFlow<MainUiState<UserModel?>> = _userState.asStateFlow()



    fun fetchUserInfo() {
        viewModelScope.launch {
            _userState.value = MainUiState.Loading
            try {
                repository.getCurrentUser()
                    .collect { user ->
                        _userState.value = MainUiState.Success(user)
                    }
            } catch (e: Exception) {
                _userState.value = MainUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

//    //StateFlow to hold the album state
//    private val _albumsState = MutableStateFlow<MainUiState<List<NewAlbumsReleaseModel>>>(
//        MainUiState.Loading
//    )
//    val albumsState: StateFlow<MainUiState<List<NewAlbumsReleaseModel>>> = _albumsState.asStateFlow()

    //Fetch new albums

//    fun fetchNewAlbums() {
//        viewModelScope.launch {
//            _albumsState.value = MainUiState.Loading
//            when (val result = albumRepository.getNewAlbumsRelease()) {
//                is Result.Success -> {
//                    _albumsState.value = Success(result.data)
//                }
//                is Result.Error -> {
//                    _albumsState.value = Error(result.message?: "An error occurred")
//                }
//
//                Result.Empty -> {
//                    _albumsState.value = Error("No albums found")
//                }
//            }
//        }
//    }


    private val _albumsState = MutableStateFlow<MainUiState<List<AlbumModel>>>(
        MainUiState.Loading
    )
    val albumsState: StateFlow<MainUiState<List<AlbumModel>>> = _albumsState.asStateFlow()

    fun fetchNewAlbums() {
        viewModelScope.launch {
            _albumsState.value = MainUiState.Loading
            when (val result = albumRepository.getNewAlbumsRelease()) {
                is Result.Success -> {
                    _albumsState.value = Success(result.data)
                }
                is Result.Error -> {
                    _albumsState.value = Error(result.message ?: "An error occurred")
                }
                Result.Empty -> {
                    _albumsState.value = Error("No albums found")
                }
            }
        }
    }


    // show artist in home screen

    private val _artistState = MutableStateFlow<MainUiState<List<ArtistModel>>>(
        MainUiState.Loading
    )
    val artistState: StateFlow<MainUiState<List<ArtistModel>>> = _artistState.asStateFlow()

    fun fetchArtistsFromNewAlbumsRelease() {
        viewModelScope.launch {
            _artistState.value = MainUiState.Loading
            when (val result = albumRepository.getNewAlbumsRelease()){
                Result.Empty -> {
                    _artistState.value = Error("No artist found")
                }
                is Result.Error -> {
                    _albumsState.value = Error(result.message ?: "An error occurred")
                }
                is Result.Success -> {
                    _artistState.value = Success(result.data.map { album ->
                        ArtistModel(
                            id = album.artists?.firstOrNull()?.id ?: "",
                            name = album.artists?.firstOrNull()?.name,
                            imageUrl = album.images?.firstOrNull()?.url
                        )
                    }.sortedBy { it.name })
                }
            }
        }
    }


    // fetch recently played tracks
    private val _recentPlayedUiState = MutableStateFlow<MainUiState<List<com.example.spoti5.domain.model.player.AlbumModel>>>(
        MainUiState.Loading
    )
    val recentPlayedUiState: StateFlow<MainUiState<List<com.example.spoti5.domain.model.player.AlbumModel>>> = _recentPlayedUiState.asStateFlow()

    fun fetchRecentlyPlayedTracks() {
        viewModelScope.launch {
            _recentPlayedUiState.value = MainUiState.Loading
            when (val result = playerRepository.getRecentTracks()) {
                is Result.Success -> {
                    val today = LocalDate.now()
                    val albumList = result.data.items
                        ?.filter { item ->
                            val playedAt = item.playedAt
                            playedAt != null && ZonedDateTime.parse(playedAt).toLocalDate().isEqual(today)
                        }
                        ?.mapNotNull { it.track?.album }
                        ?.distinctBy { it.id }
                        ?.take(8)

                    _recentPlayedUiState.value = Success(albumList ?: emptyList())
                    Log.d("HomeViewModel", "Fetched ${albumList?.size ?: 0} recently played albums")
                }
                is Result.Error -> {
                    _recentPlayedUiState.value = Error(result.message ?: "An error occurred")
                }
                Result.Empty -> {
                    _recentPlayedUiState.value = Error("No recently played tracks found")
                }
            }
        }
    }



}