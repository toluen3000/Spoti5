package com.example.spoti5.presentations.feature.details.UiState

sealed class DeleteUiState {
    object Idle : DeleteUiState()
    object Deleting : DeleteUiState()
    object Deleted : DeleteUiState()
    data class DeleteError(val message: String) : DeleteUiState()
}