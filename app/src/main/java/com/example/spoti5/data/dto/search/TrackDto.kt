package com.example.spoti5.data.dto.search

import com.example.spoti5.domain.model.search.TrackModel
import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("album") val album: AlbumDto?,
    @SerializedName("artists") val artists: List<ArtistDto>?,
    @SerializedName("available_markets") val availableMarkets: List<String>?,
    @SerializedName("disc_number") val discNumber: Int?,
    @SerializedName("duration_ms") val durationMs: Int?,
    @SerializedName("explicit") val explicit: Boolean?,
    @SerializedName("external_ids") val externalIds: ExternalIdsDto?,
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDto?,
    @SerializedName("href") val href: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("is_playable") val isPlayable: Boolean?,
    @SerializedName("linked_from") val linkedFrom: Any?,
    @SerializedName("restrictions") val restrictions: RestrictionsDto?,
    @SerializedName("name") val name: String?,
    @SerializedName("popularity") val popularity: Int?,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("track_number") val trackNumber: Int?,
    @SerializedName("type") val type: String?,
    @SerializedName("uri") val uri: String?,
    @SerializedName("is_local") val isLocal: Boolean?
){
    fun toDomainModel(): TrackModel {
        return TrackModel(
            id = id ?: "",
            name = name ?: "",
            artists = artists?.map { it.toDomainModel() } ?: emptyList(),
            album = album?.toDomainModel(),
            durationMs = durationMs ?: 0,
            previewUrl = previewUrl,
            uri = uri ?: ""
        )
    }
}




