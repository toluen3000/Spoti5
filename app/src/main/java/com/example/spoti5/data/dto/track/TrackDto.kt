package com.example.spoti5.data.dto.track


import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("album")
    val album: AlbumDto?,
    @SerializedName("artists")
    val artists: List<ArtistDto>?,
    @SerializedName("available_markets")
    val availableMarkets: List<String>?,
    @SerializedName("disc_number")
    val discNumber: Int?,
    @SerializedName("duration_ms")
    val durationMs: Int?,
    @SerializedName("explicit")
    val explicit: Boolean?,
    @SerializedName("external_ids")
    val externalIds: ExternalIdsDto?,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,
    @SerializedName("href")
    val href: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("is_local")
    val isLocal: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("popularity")
    val popularity: Int?,
    @SerializedName("preview_url")
    val previewUrl: String?,
    @SerializedName("track_number")
    val trackNumber: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("uri")
    val uri: String?
){
    fun toDomainModel(): com.example.spoti5.domain.model.track.TrackModel {
        return com.example.spoti5.domain.model.track.TrackModel(
            album = album?.toDomainModel(),
            artists = artists?.map { it.toDomainModel() } ?: emptyList(),
            availableMarkets = availableMarkets ?: emptyList(),
            discNumber = discNumber ?: 0,
            durationMs = durationMs ?: 0,
            explicit = explicit ?: false,
            externalIds = externalIds?.toDomainModel(),
            externalUrls = externalUrls?.toDomainModel(),
            href = href ?: "Unknown",
            id = id ?: "Unknown",
            isLocal = isLocal ?: false,
            name = name ?: "Unknown",
            popularity = popularity ?: 0,
            previewUrl = previewUrl ?: "Unknown",
            trackNumber = trackNumber ?: 0,
            type = type ?: "Unknown",
            uri = uri ?: "Unknown"
        )
    }
}