package com.example.spoti5.domain.model.player


data class CurrentPlayingTrackModel(
    val device: DeviceModel?,

    val repeatState: String?,

    val shuffleState: Boolean?,

    val context: ContextModel?,

    val timestamp: Long?,

    val progressMs: Int?,

    val isPlaying: Boolean?,

    val track: TrackItemModel?,

    val currentlyPlayingType: String?,

    val actions: ActionsModel?
)
