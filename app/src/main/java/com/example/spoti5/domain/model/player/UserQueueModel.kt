package com.example.spoti5.domain.model.player


data class UserQueueModel(

    val currentlyPlaying: TrackItemModel?,

    val queue: List<TrackItemModel>?
)