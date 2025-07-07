package com.example.spoti5.domain.model.album

import com.example.spoti5.data.dto.ImageDto
import com.google.gson.annotations.SerializedName

data class AlbumModel(
    val id: String?,
    val name: String?,
    val releaseDate: String?,
    val albumType: String?,
    val images: List<ImageDto>?,
    val artists: List<ArtistModel>?,
    val tracks: TracksModel?,
    val total_tracks: Int,
    val uri: String,
    val externalUrls: String? = null
)

data class ExternalUrlsModel(
    val spotify: String?
)

data class TrackItemModel(
    val name: String?,
    val id: String?,
    val uri: String?,
    val artists: List<ArtistModel>?,
    val durationMs: Int?,
    val trackNumber: Int?

)

data class TracksModel(
    val items: List<TrackItemModel>?
)


data class ArtistModel(
    val id: String?,
    val name: String?
)

