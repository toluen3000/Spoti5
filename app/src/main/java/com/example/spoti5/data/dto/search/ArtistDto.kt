package com.example.spoti5.data.dto.search

import com.example.spoti5.domain.model.search.ArtistModel
import com.google.gson.annotations.SerializedName

data class ArtistDto(
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDto?,
    @SerializedName("href") val href: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("uri") val uri: String?,
    @SerializedName("followers") val followers: FollowersDto? = null,
    @SerializedName("genres") val genres: List<String>? = null,
    @SerializedName("images") val images: List<ImageDto>? = null,
    @SerializedName("popularity") val popularity: Int? = null
){
    fun toDomainModel(): ArtistModel {
        return ArtistModel(
            id = id ?: "",
            name = name ?: "",
            image = images?.firstOrNull()?.toDomainModel(),
            genres = genres ?: emptyList()
        )
    }
}
