package com.example.spoti5.data.dto.album

import com.example.spoti5.domain.model.album.AlbumsModel
import com.example.spoti5.domain.model.album.NewAlbumsReleaseModel
import com.google.gson.annotations.SerializedName

data class NewAlbumsReleaseDto (
    @SerializedName("albums")
    val albums: AlbumsDto
){
    fun toDomainModel(): NewAlbumsReleaseModel {
        return NewAlbumsReleaseModel(
            albums = albums.toDomainModel()
        )
    }
}

data class AlbumsDto(
    val href: String?,
    val limit: Int?,
    val next: String?,
    val offset: Int?,
    val previous: String?,
    val total: Int?,
    val items: List<AlbumDto>
){
    fun toDomainModel(): AlbumsModel {
        return AlbumsModel(
            href = href,
            limit = limit,
            next = next,
            offset = offset,
            previous = previous,
            total = total,
            items = items.map { it.toDomainModel() }
        )
    }
}
