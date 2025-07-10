package com.example.spoti5.data.dto.library

import com.example.spoti5.data.dto.album.AlbumDto
import com.example.spoti5.domain.model.library.SavedAlbumItemModel
import com.example.spoti5.domain.model.library.UserSavedAlbumModel
import com.google.gson.annotations.SerializedName

data class UserSavedAlbumDto(
    @SerializedName("items")
    val items: List<SavedAlbumItemDto>?
){
    fun toDomainModel(): UserSavedAlbumModel {
        return UserSavedAlbumModel(
            items = items?.map { it.toDomainModel() }
        )
    }
}

data class  SavedAlbumItemDto(
    @SerializedName("added_at")
    val addedAt: String?,
    @SerializedName("album")
    val album: AlbumDto?
){
    fun toDomainModel(): SavedAlbumItemModel {
        return SavedAlbumItemModel(
            addedAt = addedAt,
            album = album
        )
    }
}