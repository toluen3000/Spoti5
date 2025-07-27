package com.example.spoti5.data.dto.player

import ContextDto
import TrackItemDto
import com.example.spoti5.domain.model.player.CursorsModel
import com.example.spoti5.domain.model.player.PlayedTrackItemModel
import com.example.spoti5.domain.model.player.RecentlyPlayedTrackModel
import com.google.gson.annotations.SerializedName

data class RecentlyPlayedTrackResponseDto(
    @SerializedName("href")
    val href: String?,

    @SerializedName("limit")
    val limit: Int?,

    @SerializedName("next")
    val next: String?,

    @SerializedName("cursors")
    val cursors: CursorsDto?,

    @SerializedName("total")
    val total: Int?,

    @SerializedName("items")
    val items: List<PlayedTrackItemDto>?
){
    fun toModel(): RecentlyPlayedTrackModel {
        return RecentlyPlayedTrackModel(
            href = href ?: "Unknown",
            limit = limit ?: 0,
            next = next ?: "Unknown",
            cursors = cursors?.toModel(),
            total = total ?: 0,
            items = items?.map { it.toModel() } ?: emptyList()
        )
    }
}

data class CursorsDto(
    @SerializedName("after")
    val after: String?,

    @SerializedName("before")
    val before: String?
){
    fun toModel(): CursorsModel {
        return CursorsModel(
            after = after ?: "Unknown",
            before = before ?: "Unknown"
        )
    }
}



data class PlayedTrackItemDto(
    @SerializedName("track")
    val track: TrackItemDto?,

    @SerializedName("played_at")
    val playedAt: String?,

    @SerializedName("context")
    val context: ContextDto?
){
    fun toModel(): PlayedTrackItemModel {
        return PlayedTrackItemModel(
            track = track?.toDomainModel(),
            playedAt = playedAt ?: "Unknown",
            context = context?.toDomainModel()
        )
    }
}



