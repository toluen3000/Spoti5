package com.example.spoti5.data.dto.track

import com.google.gson.annotations.SerializedName

class RecommendationResponse {
    @SerializedName("tracks")
    val tracks : List<TrackDto>? = null
}