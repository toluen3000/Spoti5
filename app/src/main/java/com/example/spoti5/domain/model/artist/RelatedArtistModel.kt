package com.example.spoti5.domain.model.artist

import com.example.spoti5.data.dto.artist.RelatedArtistDto
import com.google.gson.annotations.SerializedName

data class RelatedArtistModel(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val genres: List<String>?,
    val popularity: Int?
)

data class RelatedArtistsModel(
    val artists: List<RelatedArtistModel>
)
