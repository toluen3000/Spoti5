package com.example.spoti5.data.dto.player

import TrackItemDto
import com.google.gson.annotations.SerializedName


data class UserQueueDto(
    @SerializedName("currently_playing")
    val currentlyPlaying: TrackItemDto?,

    @SerializedName("queue")
    val queue: List<TrackItemDto>?
){
    fun toDomainModel(): com.example.spoti5.domain.model.player.UserQueueModel {
        return com.example.spoti5.domain.model.player.UserQueueModel(
            currentlyPlaying = currentlyPlaying?.toDomainModel(),
            queue = queue?.map { it.toDomainModel() } ?: emptyList()
        )
    }
}