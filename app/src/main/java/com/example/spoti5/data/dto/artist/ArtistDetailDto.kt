package com.example.spoti5.data.dto.artist

import com.example.spoti5.domain.model.artist.ArtistDetailModel
import com.example.spoti5.domain.model.artist.ExternalUrlsModel
import com.google.gson.annotations.SerializedName

class ArtistDetailDto(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("href")
    val href: String?, // The URL to access the artist's details ,

    @SerializedName("uri")
    val uri: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("popularity")
    val popularity: Int?,

    @SerializedName("genres")
    val genres: List<String>?,

    @SerializedName("images")
    val images: List<ImageDto>?,

    @SerializedName("followers")
    val followers: FollowersDto?,

    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?
) {
    fun toDomainModel(): ArtistDetailModel{
        return ArtistDetailModel(
            id = id ?: "Unknown",
            name = name ?: "Unknown",
            href = href ?: "Unknown",
            uri = uri ?: "Unknown",
            type = type ?: "Unknown",
            popularity = popularity ?: 0,
            genres = genres ?: emptyList(),
            images = images?.map { it.toDomainModel() } ?: emptyList(),
            followers = followers?.toDomainModel(),
            externalUrls = externalUrls?.toDomainModel()
        )
    }
}


data class FollowersDto(
    @SerializedName("href")
    val href: String?, //This will always be set to null, as the Web API does not support it at the moment.

    @SerializedName("total")
    val total: Int?
){
    fun toDomainModel(): com.example.spoti5.domain.model.artist.FollowersModel {
        return com.example.spoti5.domain.model.artist.FollowersModel(
            href = href ?: "Unknown",
            total = total ?: 0
        )
    }
}

data class ExternalUrlsDto(
    @SerializedName("spotify")
    val spotify: String?
){
    fun toDomainModel():ExternalUrlsModel {
        return ExternalUrlsModel(
            spotify = spotify ?: "https://open.spotify.com/artist/"
        )
    }
}

data class ImageDto(
    @SerializedName("url")
    val url: String?,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("width")
    val width: Int?
){
    fun toDomainModel(): com.example.spoti5.domain.model.artist.ImageModel {
        return com.example.spoti5.domain.model.artist.ImageModel(
            url = url ?: "Unknown",
            height = height ?: 0,
            width = width ?: 0
        )
    }
}