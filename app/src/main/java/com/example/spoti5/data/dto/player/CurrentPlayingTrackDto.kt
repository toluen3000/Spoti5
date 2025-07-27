package com.example.spoti5.data.dto.player

import ActionsDto
import ContextDto
import DeviceDto
import TrackItemDto
import com.example.spoti5.domain.model.player.CurrentPlayingTrackModel
import com.google.gson.annotations.SerializedName

data class CurrentPlayingTrackDto(
    @SerializedName("device")
    val device: DeviceDto?,

    @SerializedName("repeat_state")
    val repeatState: String?,

    @SerializedName("shuffle_state")
    val shuffleState: Boolean?,

    @SerializedName("context")
    val context: ContextDto?,

    @SerializedName("timestamp")
    val timestamp: Long?,

    @SerializedName("progress_ms")
    val progressMs: Int?,

    @SerializedName("is_playing")
    val isPlaying: Boolean?,

    @SerializedName("item")
    val track: TrackItemDto?,

    @SerializedName("currently_playing_type")
    val currentlyPlayingType: String?,

    @SerializedName("actions")
    val actions: ActionsDto?
){
    fun toDomainModel(): CurrentPlayingTrackModel {
        return CurrentPlayingTrackModel(
            device = device?.toDomainModel(),
            repeatState = repeatState ?: "off",
            shuffleState = shuffleState ?: false,
            context = context?.toDomainModel(),
            timestamp = timestamp ?: 0L,
            progressMs = progressMs ?: 0,
            isPlaying = isPlaying ?: false,
            track = track?.toDomainModel(),
            currentlyPlayingType = currentlyPlayingType ?: "unknown",
            actions = actions?.toDomainModel()
        )
    }
}
