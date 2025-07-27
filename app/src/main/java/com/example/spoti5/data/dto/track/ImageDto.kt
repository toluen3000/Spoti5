package com.example.spoti5.data.dto.track


import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("height")
    val height: Int?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("width")
    val width: Int?
){
    fun toDomainModel(): com.example.spoti5.domain.model.track.ImageModel {
        return com.example.spoti5.domain.model.track.ImageModel(
            height = height ?: 0,
            url = url ?: "Unknown",
            width = width ?: 0
        )
    }
}