package com.example.spoti5.domain.model.search

data class ArtistModel(
    val id: String,
    val name: String,
    val image: ImageModel? = null,
    val genres: List<String>? = null
)