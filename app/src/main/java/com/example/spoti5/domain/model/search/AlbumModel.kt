package com.example.spoti5.domain.model.search

data class AlbumModel(
    val id: String,
    val name: String,
    val artists: List<ArtistModel>,
    val images: List<ImageModel>,
    val releaseDate: String
)

