package com.example.spoti5.data.dto

import com.example.spoti5.domain.model.UserModel
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("display_name")
    val display_name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("images")
    val images: List<ImageDto>,
    @SerializedName("country")
    val country: String?,
){
    fun toDomainModel(): UserModel {
        return UserModel(
            id = id,
            name = display_name ?: "Unknown",
            email = email?: "No email provided",
            imageUrl = images.firstOrNull()?.url,
            country = country?: "No country provided"
        )
    }
}

data class ImageDto(
    @SerializedName("url")
    val url: String
)

