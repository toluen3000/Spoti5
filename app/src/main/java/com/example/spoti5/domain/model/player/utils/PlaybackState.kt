package com.example.spoti5.domain.model.player.utils

import android.graphics.Bitmap

data class PlaybackState(
    val isPlaying: Boolean,
    val trackName: String,
    val artistName: String,
    val albumName: String,
    val albumImageUrl: Bitmap? = null,
    val trackUri: String,
    val positionMs: Long,
    val durationMs: Long
)
