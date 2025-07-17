package com.example.spoti5.domain.model.artist

import com.example.spoti5.data.dto.artist.ExternalUrlsDto
import com.example.spoti5.data.dto.artist.FollowersDto
import com.example.spoti5.data.dto.artist.ImageDto
import com.google.gson.annotations.SerializedName

data class ArtistDetailModel(
    val id: String,
    val name: String?,
    val href: String?, // The URL to access the artist's details ,
    val uri: String?,
    val type: String?,
    val popularity: Int?,
    val genres: List<String>?,
    val images: List<ImageModel>?,
    val followers: FollowersModel?,
    val externalUrls: ExternalUrlsModel?
)

data class FollowersModel(

    val href: String?, //This will always be set to null, as the Web API does not support it at the moment.


    val total: Int?
)

data class ExternalUrlsModel(

    val spotify: String?
)

data class ImageModel(

    val url: String?,

    val height: Int?,

    val width: Int?
)

