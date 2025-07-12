package com.example.spoti5.presentations.feature.details.UiState

sealed class CheckSavedAlbumState {
    object Idle : CheckSavedAlbumState()
    object Loading : CheckSavedAlbumState()
    data class Success(val isSaved: Boolean) : CheckSavedAlbumState()
    data class Error(val message: String) : CheckSavedAlbumState()
}
