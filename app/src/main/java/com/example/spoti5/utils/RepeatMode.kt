package com.example.spoti5.utils

enum class RepeatMode {
    TRACK, CONTEXT, OFF;
    // TRACK: Repeat the current track
    // CONTEXT: Repeat the current context (e.g., playlist or album)
    // OFF: No repeat

    fun toApiValue(): String = when(this) {
        TRACK -> "track"
        CONTEXT -> "context"
        OFF -> "off"
    }
}