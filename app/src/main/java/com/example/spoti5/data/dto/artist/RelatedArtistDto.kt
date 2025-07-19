package com.example.spoti5.data.dto.artist

import com.example.spoti5.domain.model.artist.RelatedArtistModel
import com.google.gson.annotations.SerializedName

data class RelatedArtistDto(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,

    @SerializedName("followers")
    val followers: FollowersDto?,

    @SerializedName("genres")
    val genres: List<String>?,

    @SerializedName("href")
    val href: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("images")
    val images: List<ImageDto>?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("popularity")
    val popularity: Int?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("uri")
    val uri: String?
){
    fun toDomainModel(): RelatedArtistModel {
        return RelatedArtistModel(
            genres = genres ?: emptyList(),
            id = id ?: "Unknown",
            name = name ?: "Unknown",
            popularity = popularity ?: 0,
            imageUrl = images?.firstOrNull()?.url ?: ""

        )
    }
}

data class RelatedArtistsResponseDto(
    @SerializedName("artists")
    val artists: List<RelatedArtistDto>
)
