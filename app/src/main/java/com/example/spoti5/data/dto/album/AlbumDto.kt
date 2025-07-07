package com.example.spoti5.data.dto.album

import com.example.spoti5.data.dto.ImageDto
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.model.album.ArtistModel
import com.example.spoti5.domain.model.album.TrackItemModel
import com.example.spoti5.domain.model.album.TracksModel
import com.google.gson.annotations.SerializedName

data class AlbumDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("album_type")
    val albumType: String?,
    @SerializedName("images")
    val images: List<ImageDto>?,
    @SerializedName("artists")
    val artists: List<ArtistDto>?,
    @SerializedName("tracks")
    val tracks: TracksDto?,
    @SerializedName("total_tracks")
    val total_tracks: Int,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?
){
    // mapper

    fun toDomainModel(): AlbumModel {
        return AlbumModel(
            id = id ?: "",
            name = name ?: "Unknown",
            releaseDate = releaseDate ?: "Unknown",
            albumType = albumType ?: "Unknown",
            images = images ?: emptyList(),
            artists = artists?.map { it.toDomainModel() } ?: emptyList(),
            tracks = tracks?.toDomainModel(),
            total_tracks = total_tracks,
            uri = uri,
            externalUrls = externalUrls?.toUrlOrDefault(id ?: "")
        )
    }


}

data class ExternalUrlsDto(
    @SerializedName("spotify")
    val spotify: String?
){
    fun toUrlOrDefault(id: String): String {
        return spotify ?: "https://open.spotify.com/album/"
    }
}

data class TrackItemDto(
    @SerializedName("name")
    val name: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("uri")
    val uri: String?,
    @SerializedName("artists")
    val artists: List<ArtistDto>?,
    @SerializedName("duration_ms")
    val durationMs: Int?,
    @SerializedName("track_number")
    val trackNumber: Int?

){
    fun toDomainModel(): TrackItemModel {
        return TrackItemModel(
            name = name,
            id = id,
            uri = uri,
            artists = artists?.map { it.toDomainModel() },
            durationMs = durationMs,
            trackNumber = trackNumber
        )
    }
}

data class TracksDto(
    @SerializedName("items")
    val items: List<TrackItemDto>?
){
    fun toDomainModel(): TracksModel {
       return TracksModel(
            items = items?.map { it.toDomainModel() } ?: emptyList()
        )
    }
}


data class ArtistDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
){
    fun toDomainModel(): ArtistModel {
        return ArtistModel(
            id = id,
            name = name
        )
    }
}

