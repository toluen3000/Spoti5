package com.example.spoti5.data.dto.track


import com.google.gson.annotations.SerializedName

data class ArtistDto(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,
    @SerializedName("href")
    val href: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("uri")
    val uri: String?
){
    fun toDomainModel(): com.example.spoti5.domain.model.track.ArtistModel {
        return com.example.spoti5.domain.model.track.ArtistModel(
            externalUrls = externalUrls?.toDomainModel(),
            href = href ?: "Unknown",
            id = id ?: "Unknown",
            name = name ?: "Unknown",
            type = type ?: "Unknown",
            uri = uri ?: "Unknown"
        )
    }
}