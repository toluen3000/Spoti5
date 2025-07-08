package com.example.spoti5.presentations.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.model.album.TrackItemModel
import com.example.spoti5.domain.repository.AlbumsRepository
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val albumsRepository: AlbumsRepository
) : ViewModel() {

    private val _albumState = MutableStateFlow<MainUiState<AlbumModel>>(
        Loading
    )
    val albumState: StateFlow<MainUiState<AlbumModel>> = _albumState.asStateFlow()

    fun fetchAlbumById(albumId: String) {
        viewModelScope.launch {
            _albumState.value = Loading
            try {
                val result = albumsRepository.getAlbumById(albumId)
                when (result) {
                    is Result.Success -> {
                        _albumState.value = Success(result.data)
                    }

                    is Result.Error -> {
                        _albumState.value = Error(result.message ?: "Unknown error")
                    }

                    Result.Empty -> {
                        _albumState.value = Error("No data found")
                    }
                }
            } catch (e: Exception) {
                _albumState.value = Error(e.message ?: "Unknown error")
            }
        }
    }



//    private val _trackItemsState = MutableStateFlow<MainUiState<List<TrackItemModel>>>(
//        MainUiState.Loading
//    )

//    val trackItemsState: StateFlow<MainUiState<List<TrackItemModel>>> = _trackItemsState.asStateFlow()
//
//    fun fetchAlbumTracksById(albumId: String) {
//        viewModelScope.launch {
//            _trackItemsState.value = MainUiState.Loading
//            try {
//                val result = albumsRepository.getAlbumTracks(albumId)
//                when (result) {
//                    is Result.Success -> {
//                        _trackItemsState.value = Success(result.data)
//                    }
//                    is Result.Error -> {
//                        _trackItemsState.value = Error(result.message ?: "Unknown error")
//                    }
//
//                    Result.Empty ->  {
//                        _trackItemsState.value = Error("No data found")
//                    }
//                }
//            } catch (e: Exception) {
//                _trackItemsState.value = MainUiState.Error(e.message ?: "Unknown error")
//            }
//        }
//    }

    // State: Tracks
    private val _trackItemsState =
        MutableStateFlow<MainUiState<List<TrackItemModel>>>(Loading)
    val trackItemsState: StateFlow<MainUiState<List<TrackItemModel>>> =
        _trackItemsState.asStateFlow()

    fun fetchAlbumTracksById(albumId: String) {
        viewModelScope.launch {
            _trackItemsState.value = Loading
            when (val result = albumsRepository.getAlbumTracks(albumId)) {
                is Result.Success -> _trackItemsState.value = Success(result.data)
                is Result.Error -> _trackItemsState.value = Error(result.message ?: "Unknown error")
                Result.Empty -> _trackItemsState.value = Error("No data found")
            }
        }
    }
}

