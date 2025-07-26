import com.example.spoti5.domain.player.AlbumModel
import com.example.spoti5.domain.player.ArtistModel
import com.example.spoti5.domain.player.ContextModel
import com.example.spoti5.domain.player.DeviceModel
import com.example.spoti5.domain.player.ExternalUrlsModel
import com.example.spoti5.domain.player.PlaybackStateModel
import com.example.spoti5.domain.player.TrackItemModel
import com.google.gson.annotations.SerializedName

data class PlaybackStateDto(
    @SerializedName("device")
    val device: DeviceDto?,

    @SerializedName("repeat_state")
    val repeatState: String?,

    @SerializedName("shuffle_state")
    val shuffleState: Boolean?,

    @SerializedName("context")
    val context: ContextDto?,

    @SerializedName("timestamp")
    val timestamp: Long?,

    @SerializedName("progress_ms")
    val progressMs: Int?,

    @SerializedName("is_playing")
    val isPlaying: Boolean?,

    @SerializedName("item")
    val item: TrackItemDto?,

    @SerializedName("currently_playing_type")
    val currentlyPlayingType: String?,

    @SerializedName("actions")
    val actions: ActionsDto?
){
    fun toDomainModel(): PlaybackStateModel {
        return PlaybackStateModel(
            device = device?.toDomainModel(),
            repeatState = repeatState,
            shuffleState = shuffleState,
            context = context?.toDomainModel(),
            timestamp = timestamp,
            progressMs = progressMs,
            isPlaying = isPlaying,
            item = item?.toDomainModel(),
            currentlyPlayingType = currentlyPlayingType,
            actions = actions?.toDomainModel()
        )
    }
}

// --- Device ---
data class DeviceDto(
    @SerializedName("id")
    val id: String?,

    @SerializedName("is_active")
    val isActive: Boolean?,

    @SerializedName("is_private_session")
    val isPrivateSession: Boolean?,

    @SerializedName("is_restricted")
    val isRestricted: Boolean?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("volume_percent")
    val volumePercent: Int?,

    @SerializedName("supports_volume")
    val supportsVolume: Boolean?
){
    fun toDomainModel(): DeviceModel {
        return DeviceModel(
            id = id ?: "Unknown",
            isActive = isActive ?: false,
            isPrivateSession = isPrivateSession ?: false,
            isRestricted = isRestricted ?: false,
            name = name ?: "Unknown",
            type = type ?: "Unknown",
            volumePercent = volumePercent ?: 0,
            supportsVolume = supportsVolume ?: false
        )
    }
}

// --- Context ---
data class ContextDto(
    @SerializedName("type")
    val type: String?,

    @SerializedName("href")
    val href: String?,

    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,

    @SerializedName("uri")
    val uri: String?
){
    fun toDomainModel(): ContextModel {
        return ContextModel(
            type = type ?: "Unknown",
            href = href ?: "Unknown",
            externalUrls = externalUrls?.toDomainModel(),
            uri = uri ?: "Unknown"
        )
    }
}

// --- ExternalUrls ---
data class ExternalUrlsDto(
    @SerializedName("spotify")
    val spotify: String?
){
    fun toDomainModel(): ExternalUrlsModel {
        return ExternalUrlsModel(
            spotify = spotify ?: "https://open.spotify.com/"
        )
    }
}

// --- Track Item ---
data class TrackItemDto(
    @SerializedName("album")
    val album: AlbumDto?,

    @SerializedName("artists")
    val artists: List<ArtistDto>?,

    @SerializedName("available_markets")
    val availableMarkets: List<String>?,

    @SerializedName("disc_number")
    val discNumber: Int?,

    @SerializedName("duration_ms")
    val durationMs: Int?,

    @SerializedName("explicit")
    val explicit: Boolean?,

    @SerializedName("external_ids")
    val externalIds: ExternalIdsDto?,

    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,

    @SerializedName("href")
    val href: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("is_playable")
    val isPlayable: Boolean?,

    @SerializedName("linked_from")
    val linkedFrom: LinkedFromDto?,

    @SerializedName("restrictions")
    val restrictions: RestrictionsDto?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("popularity")
    val popularity: Int?,

    @SerializedName("preview_url")
    val previewUrl: String?,

    @SerializedName("track_number")
    val trackNumber: Int?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("uri")
    val uri: String?,

    @SerializedName("is_local")
    val isLocal: Boolean?
){
    fun toDomainModel(): TrackItemModel {
        return TrackItemModel(
            album = album?.toDomainModel(),
            artists = artists?.map { it.toDomainModel() },
            availableMarkets = availableMarkets,
            discNumber = discNumber,
            durationMs = durationMs,
            explicit = explicit,
            externalIds = externalIds?.toDomainModel(),
            externalUrls = externalUrls?.toDomainModel(),
            href = href,
            id = id ?: "Unknown",
            isPlayable = isPlayable,
            linkedFrom = linkedFrom?.toDomainModel(),
            restrictions = restrictions?.toDomainModel(),
            name = name,
            popularity = popularity,
            previewUrl = previewUrl,
            trackNumber = trackNumber,
            type = type,
            uri = uri,
            isLocal = isLocal
        )
    }
}

// --- Album ---
data class AlbumDto(
    @SerializedName("album_type")
    val albumType: String?,

    @SerializedName("total_tracks")
    val totalTracks: Int?,

    @SerializedName("available_markets")
    val availableMarkets: List<String>?,

    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,

    @SerializedName("href")
    val href: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("images")
    val images: List<ImageDto>?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("release_date_precision")
    val releaseDatePrecision: String?,

    @SerializedName("restrictions")
    val restrictions: RestrictionsDto?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("uri")
    val uri: String?,

    @SerializedName("artists")
    val artists: List<ArtistDto>?
){
    fun toDomainModel(): AlbumModel {
        return AlbumModel(
            albumType = albumType ?: "Unknown",
            totalTracks = totalTracks ?: 0,
            availableMarkets = availableMarkets ?: emptyList(),
            externalUrls = externalUrls?.toDomainModel(),
            href = href ?: "Unknown",
            id = id ?: "Unknown",
            images = images?.map { it.toDomainModel() } ?: emptyList(),
            name = name ?: "Unknown",
            releaseDate = releaseDate ?: "Unknown",
            releaseDatePrecision = releaseDatePrecision ?: "Unknown",
            restrictions = restrictions?.toDomainModel(),
            type = type ?: "Unknown",
            uri = uri ?: "Unknown",
            artists = artists?.map { it.toDomainModel() } ?: emptyList()
        )
    }
}

// --- Artist ---
data class ArtistDto(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,

    @SerializedName("href")
    val href: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("uri")
    val uri: String?
){
    fun toDomainModel(): ArtistModel {
        return ArtistModel(
            externalUrls = externalUrls?.toDomainModel(),
            href = href ?: "Unknown",
            id = id ?: "Unknown",
            name = name ?: "Unknown",
            type = type ?: "Unknown",
            uri = uri ?: "Unknown"
        )
    }
}

// --- Image ---
data class ImageDto(
    @SerializedName("url")
    val url: String?,

    @SerializedName("height")
    val height: Int?,

    @SerializedName("width")
    val width: Int?
){
    fun toDomainModel(): com.example.spoti5.domain.player.ImageModel {
        return com.example.spoti5.domain.player.ImageModel(
            url = url ?: "Unknown",
            height = height ?: 0,
            width = width ?: 0
        )
    }
}

// --- Restrictions ---
data class RestrictionsDto(
    @SerializedName("reason")
    val reason: String?
){
    fun toDomainModel(): com.example.spoti5.domain.player.RestrictionsModel {
        return com.example.spoti5.domain.player.RestrictionsModel(
            reason = reason ?: "Unknown"
        )
    }
}

// --- LinkedFrom ---
data class LinkedFromDto(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrlsDto?,

    @SerializedName("href")
    val href: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("uri")
    val uri: String?
){
    fun toDomainModel(): com.example.spoti5.domain.player.LinkedFromModel {
        return com.example.spoti5.domain.player.LinkedFromModel(
            externalUrls = externalUrls?.toDomainModel(),
            href = href ?: "Unknown",
            id = id ?: "Unknown",
            type = type ?: "Unknown",
            uri = uri ?: "Unknown"
        )
    }
}

// --- ExternalIds ---
data class ExternalIdsDto(
    @SerializedName("isrc")
    val isrc: String?,

    @SerializedName("ean")
    val ean: String?,

    @SerializedName("upc")
    val upc: String?
){
    fun toDomainModel(): com.example.spoti5.domain.player.ExternalIdsModel {
        return com.example.spoti5.domain.player.ExternalIdsModel(
            isrc = isrc ?: "Unknown",
            ean = ean ?: "Unknown",
            upc = upc ?: "Unknown"
        )
    }
}

// --- Actions ---
data class ActionsDto(
    @SerializedName("interrupting_playback")
    val interruptingPlayback: Boolean?,

    @SerializedName("pausing")
    val pausing: Boolean?,

    @SerializedName("resuming")
    val resuming: Boolean?,

    @SerializedName("seeking")
    val seeking: Boolean?,

    @SerializedName("skipping_next")
    val skippingNext: Boolean?,

    @SerializedName("skipping_prev")
    val skippingPrev: Boolean?,

    @SerializedName("toggling_repeat_context")
    val togglingRepeatContext: Boolean?,

    @SerializedName("toggling_shuffle")
    val togglingShuffle: Boolean?,

    @SerializedName("toggling_repeat_track")
    val togglingRepeatTrack: Boolean?,

    @SerializedName("transferring_playback")
    val transferringPlayback: Boolean?
){
    fun toDomainModel(): com.example.spoti5.domain.player.ActionsModel {
        return com.example.spoti5.domain.player.ActionsModel(
            interruptingPlayback = interruptingPlayback ?: false,
            pausing = pausing ?: false,
            resuming = resuming ?: false,
            seeking = seeking ?: false,
            skippingNext = skippingNext ?: false,
            skippingPrev = skippingPrev ?: false,
            togglingRepeatContext = togglingRepeatContext ?: false,
            togglingShuffle = togglingShuffle ?: false,
            togglingRepeatTrack = togglingRepeatTrack ?: false,
            transferringPlayback = transferringPlayback ?: false
        )
    }
}
