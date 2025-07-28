package com.example.spoti5.presentations.feature.play.UiState

sealed class ItemUiState<out T> {
    data class Success<T>(val data: T) : ItemUiState<T>()
    data class Error(val message: String) : ItemUiState<Nothing>()
    object Loading : ItemUiState<Nothing>()
    object Empty : ItemUiState<Nothing>()
}