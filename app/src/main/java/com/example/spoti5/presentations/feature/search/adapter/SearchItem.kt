package com.example.spoti5.presentations.feature.search.adapter

import com.example.spoti5.domain.model.search.AlbumModel
import com.example.spoti5.domain.model.search.ArtistModel
import com.example.spoti5.domain.model.search.TrackModel

sealed class SearchItem {
    data class TrackItem(val track: TrackModel) : SearchItem()
    data class ArtistItem(val artist: ArtistModel) : SearchItem()
    data class AlbumItem(val album: AlbumModel) : SearchItem()
    object TrackHeader : SearchItem()
    object ArtistHeader : SearchItem()
    object AlbumHeader : SearchItem()
}
