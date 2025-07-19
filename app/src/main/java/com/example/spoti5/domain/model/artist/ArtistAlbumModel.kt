package com.example.spoti5.domain.model.artist

import com.example.spoti5.data.dto.album.AlbumDto
import com.example.spoti5.domain.model.album.AlbumModel
import com.google.gson.annotations.SerializedName

data class ArtistAlbumModel(

    val items: List<AlbumModel>,


    val total: Int?,


    val limit: Int?,


    val offset: Int?,


    val href: String?,


    val next: String?,


    val previous: String?
) {

}