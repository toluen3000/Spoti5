package com.example.spoti5.data.dto.artist

import com.google.gson.annotations.SerializedName

data class UserFollowedArtistsResponse(
    @SerializedName("artists")
    val artists: ArtistsWrapperDto
)

data class ArtistsWrapperDto(
    val href: String?,
    val limit: Int?,
    val next: String?,
    val cursors: CursorsDto?,
    val total: Int?,
    val items: List<ArtistDetailDto>
)

data class CursorsDto(
    val after: String?
)