package com.example.spoti5.domain.model.track

data class UserSavedTrackModel(
    val added_at: String,
    val track: TrackModel
)

data class UserSavedTrackResponseModel(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<UserSavedTrackModel>
)
