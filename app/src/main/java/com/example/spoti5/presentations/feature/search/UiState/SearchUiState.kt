package com.example.spoti5.presentations.feature.search.UiState

sealed class SearchUiState <out T> {
    object Loading : SearchUiState<Nothing>()
    data class Success<T>(val data: T) : SearchUiState<T>()
    data class Error(val message: String) : SearchUiState<Nothing>()
    object Empty : SearchUiState<Nothing>()
}