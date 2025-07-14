package com.example.spoti5.domain.model.library

import com.example.spoti5.domain.model.album.AlbumModel

data class UserSavedAlbumModel(
    val items: List<SavedAlbumItemModel>?
)

data class  SavedAlbumItemModel(
    val addedAt: String?,
    val album: AlbumModel
)