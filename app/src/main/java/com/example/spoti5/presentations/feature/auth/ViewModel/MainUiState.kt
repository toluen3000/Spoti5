package com.example.spoti5.presentations.feature.auth.ViewModel


sealed class MainUiState <out T> {
    object Loading : MainUiState<Nothing>()
    data class Success<T>(val data: T) : MainUiState<T>()
    data class Error(val message: String) : MainUiState<Nothing>()
}