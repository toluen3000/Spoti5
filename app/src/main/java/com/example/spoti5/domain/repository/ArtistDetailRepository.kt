package com.example.spoti5.domain.repository

import com.example.spoti5.domain.model.album.TrackItemModel
import com.example.spoti5.domain.model.artist.ArtistDetailModel
import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.album.AlbumModel


interface ArtistDetailRepository {

    suspend fun getArtistDetail(artistId: String): Result<ArtistDetailModel>

    suspend fun getArtistTopTracks(artistId: String):Result<List<TrackItemModel>>

    suspend fun getArtistRelatedArtists(artistId: String): Result<List<ArtistDetailModel>>

    suspend fun  getArtistAlbums(artistId: String): Result<List<AlbumModel>>
}