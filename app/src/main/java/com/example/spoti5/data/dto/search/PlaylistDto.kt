package com.example.spoti5.data.dto.search


import com.google.gson.annotations.SerializedName

data class PlaylistDto(
    @SerializedName("collaborative") val collaborative: Boolean?,
    @SerializedName("description") val description: String?,
    @SerializedName("external_urls") val externalUrls: ExternalUrlsDto?,
    @SerializedName("href") val href: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("images") val images: List<ImageDto>?,
    @SerializedName("name") val name: String?,
    @SerializedName("owner") val owner: OwnerDto?,
    @SerializedName("public") val public: Boolean?,
    @SerializedName("snapshot_id") val snapshotId: String?,
    @SerializedName("tracks") val tracks: TracksRefDto?,
    @SerializedName("type") val type: String?,
    @SerializedName("uri") val uri: String?
)
{
    fun toDomainModel(): com.example.spoti5.domain.model.search.PlaylistModel {
        val ownerName = owner?.toDomainModel()?.name ?: "Unknown Owner"
        return com.example.spoti5.domain.model.search.PlaylistModel(
            id = id ?: "",
            name = name ?: "",
            description = description ?: "",
            images = images?.map { it.toDomainModel() } ?: emptyList(),
            ownerName = ownerName
        )
    }
}


