package com.example.spoti5.data.dto.artist

import com.example.spoti5.data.dto.album.TrackItemDto
import com.example.spoti5.domain.model.album.TrackItemModel
import com.google.gson.annotations.SerializedName

data class ArtistTopTracksResponseDto(
    @SerializedName("tracks")
    val tracks: List<TrackItemDto>?
){

    fun toDomainModel(): List<TrackItemModel> {
        return tracks?.map { it.toDomainModel() } ?: emptyList()
    }

}
