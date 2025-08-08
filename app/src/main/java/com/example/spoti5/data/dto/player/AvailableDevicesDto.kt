package com.example.spoti5.data.dto.player

import DeviceDto
import com.google.gson.annotations.SerializedName

data class AvailableDevicesDto(

    @SerializedName("devices")
    val devices: List<DeviceDto>?

) {
    fun toDomainModel(): com.example.spoti5.domain.model.player.AvailableDevicesModel {
        return com.example.spoti5.domain.model.player.AvailableDevicesModel(
            devices = devices?.map { it.toDomainModel() } ?: emptyList()
        )
    }
}