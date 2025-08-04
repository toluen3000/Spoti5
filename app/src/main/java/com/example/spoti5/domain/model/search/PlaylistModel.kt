package com.example.spoti5.domain.model.search

data class PlaylistModel(
    val id: String,
    val name: String,
    val description: String?,
    val images: List<ImageModel>,
    val ownerName: String
)

data class OwnerModel(
    val id: String,
    val name: String,
)

