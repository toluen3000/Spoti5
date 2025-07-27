package com.example.spoti5.data.dto.track


import com.google.gson.annotations.SerializedName

data class AlbumDto(
    @SerializedName("album_type")
    val albumType: String?,
    @SerializedName("artists")
    val artists: List<ArtistDto>?,
    @SerializedName("available_markets")
    val availableMarkets: List<String>?,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,
    @SerializedName("href")
    val href: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("images")
    val images: List<ImageDto>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("release_date_precision")
    val releaseDatePrecision: String?,
    @SerializedName("total_tracks")
    val totalTracks: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("uri")
    val uri: String?
){
    fun toDomainModel(): com.example.spoti5.domain.model.track.AlbumModel {
        return com.example.spoti5.domain.model.track.AlbumModel(
            albumType = albumType ?: "Unknown",
            artists = artists?.map { it.toDomainModel() } ?: emptyList(),
            availableMarkets = availableMarkets ?: emptyList(),
            externalUrls = externalUrls?.toDomainModel(),
            href = href ?: "Unknown",
            id = id ?: "Unknown",
            images = images?.map { it.toDomainModel() } ?: emptyList(),
            name = name ?: "Unknown",
            releaseDate = releaseDate ?: "Unknown",
            releaseDatePrecision = releaseDatePrecision ?: "Unknown",
            totalTracks = totalTracks ?: 0,
            type = type ?: "Unknown",
            uri = uri ?: "Unknown"
        )
    }
}