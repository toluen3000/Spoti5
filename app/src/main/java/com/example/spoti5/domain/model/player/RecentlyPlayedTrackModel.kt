package com.example.spoti5.domain.model.player

data class RecentlyPlayedTrackModel(
    val href: String?,

    val limit: Int?,

    val next: String?,

    val cursors: CursorsModel?,

    val total: Int?,

    val items: List<PlayedTrackItemModel>?
)
data class CursorsModel(
    val after: String?,

    val before: String?
)


data class PlayedTrackItemModel(
    val track: TrackItemModel?,

    val playedAt: String?,

    val context: ContextModel?
)



