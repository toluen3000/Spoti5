package com.example.spoti5.presentations.feature.play.UiState

import com.example.spoti5.domain.model.player.utils.PlaybackState

sealed class PlayerUiState {
    object Idle : PlayerUiState()
    object Loading : PlayerUiState()
    object Playing : PlayerUiState()
    object Paused : PlayerUiState()
    object Stopped : PlayerUiState()
    data class Success(val data: PlaybackState) : PlayerUiState()
    data class Error(val message: String) : PlayerUiState()
}
