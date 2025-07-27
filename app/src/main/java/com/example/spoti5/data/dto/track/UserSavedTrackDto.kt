package com.example.spoti5.data.dto.track

import com.example.spoti5.domain.model.track.UserSavedTrackModel
import com.example.spoti5.domain.model.track.UserSavedTrackResponseModel
import com.google.gson.annotations.SerializedName


data class UserSavedTrackDto(
    @SerializedName("added_at")
    val addedAt: String,

    @SerializedName("track")
    val track: TrackDto
){
    fun toDomainModel(): UserSavedTrackModel {
        return UserSavedTrackModel(
            added_at = addedAt,
            track = track.toDomainModel()
        )
    }
}


data class UserSavedTrackResponseDto(
    @SerializedName("href")
    val href: String,

    @SerializedName("limit")
    val limit: Int,

    @SerializedName("next")
    val next: String?,

    @SerializedName("offset")
    val offset: Int,

    @SerializedName("previous")
    val previous: String?,

    @SerializedName("total")
    val total: Int,

    @SerializedName("items")
    val items: List<UserSavedTrackDto>
){
    fun toDomainModel(): UserSavedTrackResponseModel {
        return UserSavedTrackResponseModel(
            href = href,
            limit = limit,
            next = next,
            offset = offset,
            previous = previous,
            total = total,
            items = items.map { it.toDomainModel() }
        )
    }
}
