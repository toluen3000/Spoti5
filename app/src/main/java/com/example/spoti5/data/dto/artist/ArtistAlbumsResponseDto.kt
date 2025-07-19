package com.example.spoti5.data.dto.artist

import com.example.spoti5.data.dto.album.AlbumDto
import com.example.spoti5.domain.model.artist.ArtistAlbumModel
import com.google.gson.annotations.SerializedName

data class ArtistAlbumsResponseDto(
    @SerializedName("items")
    val items: List<AlbumDto>? = null,

    @SerializedName("total")
    val total: Int?,

    @SerializedName("limit")
    val limit: Int?,

    @SerializedName("offset")
    val offset: Int? ,

    @SerializedName("href")
    val href: String?,

    @SerializedName("next")
    val next: String?,

    @SerializedName("previous")
    val previous: String?
) {
    fun toDomainModel(): ArtistAlbumModel {

        return ArtistAlbumModel(
            items = items?.map { it.toDomainModel() } ?: emptyList(),
            total = total ?: 0,
            limit = limit ?: 0,
            offset = offset ?: 0,
            href = href ?: "Unknown",
            next = next ?: "Unknown",
            previous = previous ?: "Unknown"
        )
    }
}