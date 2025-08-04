package com.example.spoti5.presentations.feature.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.domain.model.search.SearchResultModel
import com.example.spoti5.domain.repository.SearchRepository
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.spoti5.data.result.Result
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState.*
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _searchResult = MutableStateFlow<MainUiState<SearchResultModel>>(MainUiState.Loading)
    val searchResult : StateFlow<MainUiState<SearchResultModel>> = _searchResult.asStateFlow()

    fun fetchSearchResult(
        query: String,
        limit: Int = 20,
        offset: Int = 0
    ) {
        viewModelScope.launch {
            _searchResult.value = MainUiState.Loading
            when (val result = searchRepository.searchResult(query, limit, offset)) {


                Result.Empty -> {
                    Log.d("SearchViewModel", "Empty result")
                }
                is Result.Error -> {
                    Log.d("SearchViewModel", "Error fetching search result: ${result.message}")
                }
                is Result.Success -> {
                    Log.d("SearchViewModel", "Search result fetched successfully ${result.data}")
                }

            }
        }

    }


}