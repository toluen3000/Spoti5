package com.example.spoti5.presentations.feature.Home

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
import com.example.spoti5.domain.model.album.NewAlbumsReleaseModel
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SpotifyRepository,
    private val albumRepository: AlbumsRepository
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




}