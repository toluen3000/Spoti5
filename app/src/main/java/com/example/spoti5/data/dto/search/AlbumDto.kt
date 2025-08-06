package com.example.spoti5.data.dto.search

import com.example.spoti5.domain.model.search.AlbumModel
import com.google.gson.annotations.SerializedName

data class AlbumDto(
    @SerializedName("album_type") val albumType: String?,
    @SerializedName("total_tracks") val totalTracks: Int?,
    @SerializedName("available_markets") val availableMarkets: List<String>?,
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDto?,
    @SerializedName("href") val href: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("images") val images: List<ImageDto>?,
    @SerializedName("name") val name: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("release_date_precision") val releaseDatePrecision: String?,
    @SerializedName("restrictions") val restrictions: RestrictionsDto?,
    @SerializedName("type") val type: String?,
    @SerializedName("uri") val uri: String?,
    @SerializedName("artists") val artists: List<ArtistDto>?
)
{
    fun toDomainModel(): AlbumModel {
        return AlbumModel(
            id = id ?: "",
            name = name ?: "",
            images = images?.map { it.toDomainModel() } ?: emptyList(),
            releaseDate = releaseDate ?: "",
            artists = artists.orEmpty().map { it.toDomainModel() }
        )
    }
}




