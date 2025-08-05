package com.example.spoti5.presentations.feature.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spoti5.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.spoti5.data.result.Result
import com.example.spoti5.presentations.feature.search.UiState.SearchUiState
import com.example.spoti5.presentations.feature.search.adapter.SearchItem
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _searchResult = MutableStateFlow<SearchUiState<List<SearchItem>>>(SearchUiState.Loading)
    val searchResult : StateFlow<SearchUiState<List<SearchItem>>> = _searchResult.asStateFlow()

    // avoid call api on every text change
    private val _query = MutableStateFlow("")


    init {
        viewModelScope.launch {
            _query
                .debounce(600)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collect { query ->
                    fetchSearchResult(query)
                }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    fun fetchSearchResult(
        query: String,
        limit: Int = 20,
        offset: Int = 0
    ) {
        viewModelScope.launch {
            _searchResult.value = SearchUiState.Loading

            when (val result = searchRepository.searchResult(query, limit, offset)) {

                Result.Empty -> {
                    _searchResult.value = SearchUiState.Empty
                    Log.d("SearchViewModel", "Empty result")
                }

                is Result.Error -> {
                    _searchResult.value = SearchUiState.Error(result.message ?: "Unknown error")
                    Log.d("SearchViewModel", "Error fetching search result: ${result.message}")
                }

                is Result.Success -> {
                    val data = result.data

                    // limit the number of items to display to 7
                    val searchItems = mutableListOf<SearchItem>().apply {
                        data.tracks.take(5).forEach { add(SearchItem.TrackItem(it)) }
                        data.artists.take(3).forEach { add(SearchItem.ArtistItem(it)) }
                        data.albums.take(3).forEach { add(SearchItem.AlbumItem(it)) }
                    }
                    _searchResult.value = SearchUiState.Success(searchItems)
                    Log.d("SearchViewModel", "Search result fetched successfully: ${searchItems.size} items")
                }
            }
        }
    }

}