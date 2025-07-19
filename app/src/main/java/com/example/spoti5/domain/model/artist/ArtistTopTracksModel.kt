package com.example.spoti5.domain.model.artist

import com.example.spoti5.domain.model.album.TrackItemModel

data class ArtistTopTracksModel(
    val tracks: List<TrackItemModel>?
) {
}