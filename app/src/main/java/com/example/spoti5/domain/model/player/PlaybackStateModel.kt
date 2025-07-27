package com.example.spoti5.domain.model.player

data class PlaybackStateModel(
    val device: DeviceModel?,

    val repeatState: String?,

    val shuffleState: Boolean?,

    val context: ContextModel?,

    val timestamp: Long?,

    val progressMs: Int?,

    val isPlaying: Boolean?,

    val item: TrackItemModel?,

    val currentlyPlayingType: String?,

    val actions: ActionsModel?
)

data class DeviceModel(

    val id: String,

    val isActive: Boolean?,

    val isPrivateSession: Boolean?,

    val isRestricted: Boolean?,

    val name: String?,

    val type: String?,

    val volumePercent: Int?,

    val supportsVolume: Boolean?
)

// --- Context ---
data class ContextModel(
    val type: String?,

    val href: String?,

    val externalUrls: ExternalUrlsModel?,

    val uri: String?
)

// --- ExternalUrls ---
data class ExternalUrlsModel(
    val spotify: String?
)

// --- Track Item ---
data class TrackItemModel(
    val album: AlbumModel?,

    val artists: List<ArtistModel>?,

    val availableMarkets: List<String>?,

    val discNumber: Int?,

    val durationMs: Int?,

    val explicit: Boolean?,

    val externalIds: ExternalIdsModel?,

    val externalUrls: ExternalUrlsModel?,

    val href: String?,

    val id: String,

    val isPlayable: Boolean?,

    val linkedFrom: LinkedFromModel?,

    val restrictions: RestrictionsModel?,

    val name: String?,

    val popularity: Int?,

    val previewUrl: String?,

    val trackNumber: Int?,

    val type: String?,

    val uri: String?,

    val isLocal: Boolean?
)

// --- Album ---
data class AlbumModel(
    val albumType: String?,

    val totalTracks: Int?,

    val availableMarkets: List<String>?,

    val externalUrls: ExternalUrlsModel?,

    val href: String?,

    val id: String,

    val images: List<ImageModel>?,

    val name: String?,

    val releaseDate: String?,

    val releaseDatePrecision: String?,

    val restrictions: RestrictionsModel?,

    val type: String?,

    val uri: String?,

    val artists: List<ArtistModel>?
)

// --- Artist ---
data class ArtistModel(

    val externalUrls: ExternalUrlsModel?,

    val href: String?,

    val id: String,

    val name: String?,

    val type: String?,

    val uri: String?
)

// --- Image ---
data class ImageModel(
    val url: String?,

    val height: Int?,

    val width: Int?
)

// --- Restrictions ---
data class RestrictionsModel(
    val reason: String?
)

// --- LinkedFrom ---
data class LinkedFromModel(
    val externalUrls: ExternalUrlsModel?,

    val href: String?,

    val id: String,

    val type: String?,

    val uri: String?
)

// --- ExternalIds ---
data class ExternalIdsModel(
    val isrc: String?,

    val ean: String?,

    val upc: String?
)

// --- Actions ---
data class ActionsModel(
    val interruptingPlayback: Boolean?,

    val pausing: Boolean?,

    val resuming: Boolean?,

    val seeking: Boolean?,

    val skippingNext: Boolean?,

    val skippingPrev: Boolean?,

    val togglingRepeatContext: Boolean?,

    val togglingShuffle: Boolean?,

    val togglingRepeatTrack: Boolean?,

    val transferringPlayback: Boolean?
)
