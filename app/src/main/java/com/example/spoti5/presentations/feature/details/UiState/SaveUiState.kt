package com.example.spoti5.presentations.feature.details.UiState

sealed class SaveUiState {
    object Idle : SaveUiState()
    object Saving : SaveUiState()
    object Saved : SaveUiState()
    data class SaveError(val message: String) : SaveUiState()
}