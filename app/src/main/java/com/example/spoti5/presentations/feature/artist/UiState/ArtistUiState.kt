package com.example.spoti5.presentations.feature.artist.UiState

sealed class ArtistUiState<out T> {
    data class Success<T>(val data: T) : ArtistUiState<T>()
    data class Error(val message: String) : ArtistUiState<Nothing>()
    object Loading : ArtistUiState<Nothing>()
    object Empty : ArtistUiState<Nothing>()
}