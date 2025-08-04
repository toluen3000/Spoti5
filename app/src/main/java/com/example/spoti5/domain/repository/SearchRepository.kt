package com.example.spoti5.domain.repository

import com.example.spoti5.data.result.Result
import com.example.spoti5.domain.model.search.AlbumModel
import com.example.spoti5.domain.model.search.ArtistModel
import com.example.spoti5.domain.model.search.PlaylistModel
import com.example.spoti5.domain.model.search.SearchResultModel
import com.example.spoti5.domain.model.search.TrackModel

interface SearchRepository {

    suspend fun searchResult(query: String, limit: Int = 20, offset: Int = 0): Result<SearchResultModel>

}