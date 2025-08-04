package com.example.spoti5.data.repositories

import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.data.result.Result
import com.example.spoti5.data.result.safeApiCall
import com.example.spoti5.di.IoDispatcher
import com.example.spoti5.domain.model.search.SearchResultModel
import com.example.spoti5.domain.repository.SearchRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher


class SearchRepositoryImpl @Inject constructor(
    private val api : SpotifyApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SearchRepository {
    override suspend fun searchResult(
        query: String,
        limit: Int,
        offset: Int
    ): Result<SearchResultModel> {
        return with(ioDispatcher){
            safeApiCall {
                val response = api.searchAll(
                    query = query,
                    limit = limit,
                    offset = offset
                )
                Result.Success(response.toDomainModel())
            }
        }
    }
}