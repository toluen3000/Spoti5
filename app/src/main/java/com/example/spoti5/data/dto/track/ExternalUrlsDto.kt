package com.example.spoti5.data.dto.track


import com.google.gson.annotations.SerializedName

data class ExternalUrlsDto(
    @SerializedName("spotify")
    val spotify: String?
){
    fun toDomainModel(): com.example.spoti5.domain.model.track.ExternalUrlsModel {
        return com.example.spoti5.domain.model.track.ExternalUrlsModel(
            spotify = spotify ?: "Unknown"
        )
    }
}