package com.example.spoti5.data.dto.artist

import com.example.spoti5.data.dto.album.ArtistDto
import com.example.spoti5.domain.model.artist.ArtistDetailModel
import com.google.gson.annotations.SerializedName

data class SeveralArtistsResponseDto(
    @SerializedName("artists")
    val artists: List<ArtistDetailDto>
)