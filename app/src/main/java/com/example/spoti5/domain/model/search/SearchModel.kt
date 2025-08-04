package com.example.spoti5.domain.model.search

data class SearchResultModel(
    val tracks: List<TrackModel>,
    val artists: List<ArtistModel>,
    val albums: List<AlbumModel>,
)



data class ImageModel(
    val url: String,
    val width: Int,
    val height: Int
)


