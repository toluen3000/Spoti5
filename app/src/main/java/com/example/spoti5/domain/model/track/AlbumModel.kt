package com.example.spoti5.domain.model.track

data class AlbumModel(
    val albumType: String?,
    val artists: List<ArtistModel>?,
    val availableMarkets: List<String>?,
    val externalUrls: ExternalUrlsModel?,
    val href: String?,
    val id: String?,
    val images: List<ImageModel>?,
    val name: String?,
    val releaseDate: String?,
    val releaseDatePrecision: String?,
    val totalTracks: Int?,
    val type: String?,
    val uri: String?
)


data class ExternalUrlsModel(
    val spotify: String?
)
