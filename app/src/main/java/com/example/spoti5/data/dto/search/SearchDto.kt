package com.example.spoti5.data.dto.search

import com.example.spoti5.domain.model.search.AlbumModel
import com.example.spoti5.domain.model.search.ArtistModel
import com.example.spoti5.domain.model.search.ImageModel
import com.example.spoti5.domain.model.search.PlaylistModel
import com.example.spoti5.domain.model.search.SearchResultModel
import com.example.spoti5.domain.model.search.TrackModel
import com.google.gson.annotations.SerializedName

data class SearchDto(
    @SerializedName("tracks") val tracks: SearchTracksDto?,
    @SerializedName("artists") val artists: SearchArtistsDto?,
    @SerializedName("albums") val albums: SearchAlbumsDto?,
    @SerializedName("playlists") val playlists: SearchPlaylistsDto?
){
    fun toDomainModel(): SearchResultModel {
        return SearchResultModel(
            tracks = tracks?.toDomainModel() ?: emptyList(),
            artists = artists?.toDomainModel() ?: emptyList(),
            albums = albums?.toDomainModel() ?: emptyList(),
        )
    }
}


data class SearchTracksDto(
    @SerializedName("href") val href: String?,
    @SerializedName("limit") val limit: Int?,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int?,
    @SerializedName("items") val items: List<TrackDto>?
){
    fun toDomainModel(): List<TrackModel> {
        return items?.map { it.toDomainModel() } ?: emptyList()
    }
}

data class SearchArtistsDto(
    @SerializedName("href") val href: String?,
    @SerializedName("limit") val limit: Int?,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int?,
    @SerializedName("items") val items: List<ArtistDto>?
){
    fun toDomainModel(): List<ArtistModel> {
        return items?.map { it.toDomainModel() } ?: emptyList()
    }
}

data class SearchAlbumsDto(
    @SerializedName("href") val href: String?,
    @SerializedName("limit") val limit: Int?,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int?,
    @SerializedName("items") val items: List<AlbumDto>?
){
    fun toDomainModel(): List<AlbumModel> {
        return items?.map { it.toDomainModel() } ?: emptyList()
    }
}

data class SearchPlaylistsDto(
    @SerializedName("href") val href: String?,
    @SerializedName("limit") val limit: Int?,
    @SerializedName("next") val next: String?,
    @SerializedName("offset") val offset: Int?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("total") val total: Int?,
    @SerializedName("items") val items: List<PlaylistDto>?
){
    fun toDomainModel(): List<PlaylistModel> {
        return items?.map { it.toDomainModel() } ?: emptyList()
    }
}


data class ImageDto(
    @SerializedName("url") val url: String?,
    @SerializedName("height") val height: Int?,
    @SerializedName("width") val width: Int?
){
    fun toDomainModel(): ImageModel {
        return ImageModel(
            url = url ?: "",
            height = height ?: 0,
            width = width ?: 0
        )
    }
}

data class ExternalUrlsDto(
    @SerializedName("spotify") val spotify: String?
)

data class RestrictionsDto(
    @SerializedName("reason") val reason: String?
)

data class ExternalIdsDto(
    @SerializedName("isrc") val isrc: String?,
    @SerializedName("ean") val ean: String?,
    @SerializedName("upc") val upc: String?
)

data class FollowersDto(
    @SerializedName("href") val href: String?,
    @SerializedName("total") val total: Int?
)

data class OwnerDto(
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDto?,
    @SerializedName("href") val href: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("uri") val uri: String?,
    @SerializedName("display_name") val displayName: String?
){
    fun toDomainModel(): com.example.spoti5.domain.model.search.OwnerModel {
        return com.example.spoti5.domain.model.search.OwnerModel(
            id = id ?: "",
            name = displayName?: "Unknown Owner",
        )
    }
}

data class TracksRefDto(
    @SerializedName("href") val href: String?,
    @SerializedName("total") val total: Int?
)

