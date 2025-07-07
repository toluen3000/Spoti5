package com.example.spoti5.domain.model.album

import com.example.spoti5.data.dto.album.AlbumDto
import com.google.gson.annotations.SerializedName

data class NewAlbumsReleaseModel (
    val albums: AlbumsModel
)
data class AlbumsModel(
    val href: String?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?,
    val items: List<AlbumModel>
)
