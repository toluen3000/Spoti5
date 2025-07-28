package com.example.spoti5.domain.model.track

data class TrackModel (
    val album: AlbumModel?,
    val artists: List<ArtistModel>?,
    val availableMarkets: List<String>?,
    val discNumber: Int?,
    val durationMs: Int,
    val explicit: Boolean?,
    val externalIds: ExternalIdsModel?,
    val externalUrls: ExternalUrlsModel?,
    val href: String?,
    val id: String,
    val isLocal: Boolean?,
    val name: String?,
    val popularity: Int?,
    val previewUrl: String?,
    val trackNumber: Int?,
    val type: String?,
    val uri: String?
)


data class ExternalIdsModel(
    val isrc: String?
)