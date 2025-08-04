package com.example.spoti5.domain.model.search


data class TrackModel(
    val id: String,
    val name: String,
    val artists: List<ArtistModel>,
    val album: AlbumModel?,
    val durationMs: Int,
    val previewUrl: String?,
    val uri: String
)
