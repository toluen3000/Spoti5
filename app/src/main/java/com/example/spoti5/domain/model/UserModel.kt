package com.example.spoti5.domain.model

data class UserModel (
    val id: String,
    val name: String,
    val email: String?,
    val imageUrl: String?,
    val country: String?,
    )

data class ImageModel(
    val url: String,
)
