package com.example.spoti5.presentations.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.model.album.TrackItemModel
import com.example.spoti5.domain.repository.AlbumsRepository
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState.*
import com.example.spoti5.presentations.feature.details.UiState.CheckSavedAlbumState
import com.example.spoti5.presentations.feature.details.UiState.DeleteUiState
import com.example.spoti5.presentations.feature.details.UiState.SaveUiState
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


    // State: Album
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

    // State : check if album is saved
    private val _isAlbumSavedState = MutableStateFlow<CheckSavedAlbumState>(CheckSavedAlbumState.Idle)
    val isAlbumSavedState: StateFlow<CheckSavedAlbumState> = _isAlbumSavedState.asStateFlow()

    fun checkIfAlbumIsSaved(albumId: String) {
        viewModelScope.launch {
            _isAlbumSavedState.value = CheckSavedAlbumState.Loading
            when (val result = albumsRepository.checkIfAlbumIsSaved(albumId)) {
                Result.Empty -> {
                    _isAlbumSavedState.value = CheckSavedAlbumState.Error("No data found")
                }
                is Result.Error -> {
                    _isAlbumSavedState.value = CheckSavedAlbumState.Error(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    _isAlbumSavedState.value = CheckSavedAlbumState.Success(result.data)
                }
            }
        }
    }

    fun toggleAlbum(albumId: String) {
        viewModelScope.launch {
            val currentState = _isAlbumSavedState.value
            if (currentState is CheckSavedAlbumState.Success && currentState.isSaved) {
                deleteAlbumFromUserLib(albumId)
            } else {
                saveAlbumToUserLib(albumId)
            }
        }
    }


    // State : add album to user library
    private val _saveAlbumState = MutableStateFlow<SaveUiState>(SaveUiState.Idle)
    val saveAlbumState: StateFlow<SaveUiState> = _saveAlbumState.asStateFlow()


    fun saveAlbumToUserLib(albumId: String){

        viewModelScope.launch {
            _saveAlbumState.value = SaveUiState.Saving

            when(val result = albumsRepository.saveAlbumToUserLibrary(albumId)){
                Result.Empty -> {
                    _saveAlbumState.value = SaveUiState.SaveError("No data found")
                }
                is Result.Error -> {
                    _saveAlbumState.value = SaveUiState.SaveError(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    if (result.data) {
                        _isAlbumSavedState.value = CheckSavedAlbumState.Success(true)
                        _saveAlbumState.value = SaveUiState.Saved
                    } else {
                        _saveAlbumState.value = SaveUiState.SaveError("Add album failed")
                    }
                }
            }
        }

    }

    // State : delete album from user library
    private val _deleteAlbumState = MutableStateFlow<DeleteUiState>(DeleteUiState.Idle)
    val deleteAlbumState: StateFlow<DeleteUiState> = _deleteAlbumState.asStateFlow()

    fun deleteAlbumFromUserLib(albumId: String) {
        viewModelScope.launch {
            _deleteAlbumState.value = DeleteUiState.Deleting

            when (val result = albumsRepository.deleteAlbumFromUserLibrary(albumId)) {
                Result.Empty -> {
                    _deleteAlbumState.value = DeleteUiState.DeleteError("No data found")
                }
                is Result.Error -> {
                    _deleteAlbumState.value = DeleteUiState.DeleteError(result.message ?: "Unknown error")
                }
                is Result.Success -> {
                    if (result.data) {
                        _isAlbumSavedState.value = CheckSavedAlbumState.Success(false)
                        _deleteAlbumState.value = DeleteUiState.Deleted
                    } else {
                        _deleteAlbumState.value = DeleteUiState.DeleteError("Delete album failed")
                    }
                }
            }
        }
    }


}

