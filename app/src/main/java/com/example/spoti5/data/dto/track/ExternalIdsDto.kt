package com.example.spoti5.data.dto.track


import com.example.spoti5.domain.model.track.ExternalIdsModel
import com.google.gson.annotations.SerializedName

data class ExternalIdsDto(
    @SerializedName("isrc")
    val isrc: String?
){
    fun toDomainModel(): ExternalIdsModel {
        return ExternalIdsModel(
            isrc = isrc ?: "Unknown"
        )
    }
}