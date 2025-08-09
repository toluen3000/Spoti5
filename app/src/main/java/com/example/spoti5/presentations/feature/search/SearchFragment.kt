package com.example.spoti5.presentations.feature.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentSearchBinding
import com.example.spoti5.presentations.feature.search.UiState.SearchUiState
import com.example.spoti5.presentations.feature.search.adapter.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewmodel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpSearchRecyclerView()
        // search view changes to fetch results
        searchViewChangeText()
        observeSearchResult()

    }

    private fun searchViewChangeText() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewmodel.fetchSearchResult(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewmodel.onSearchQueryChanged(it)
                }
                return true
            }
        })
    }

    private fun setUpSearchRecyclerView() {
        searchAdapter = SearchAdapter(
            onTrackClick = { track ->
                Log.d("SearchFragment", "Track clicked: ${track.name}")
                val action = SearchFragmentDirections.actionSearchScreenFragmentToPlayMusicFragment(track.uri, track.id)
                findNavController().navigate(action)
            },
            onArtistClick = { artist ->
                Log.d("SearchFragment", "Artist clicked: ${artist.name}")
                val action = SearchFragmentDirections.actionSearchScreenFragmentToArtistDetailFragment(artist.id,artist.image?.url)

                findNavController().navigate(action)
            },
            onAlbumClick = { album ->
                Log.d("SearchFragment", "Album clicked: ${album.name}")
                val action = SearchFragmentDirections.actionSearchScreenFragmentToDetailsAlbumFragment(album.id, album.images.firstOrNull()?.url)
                findNavController().navigate(action)
            }
        )

        binding.rvItemSearch.adapter = searchAdapter
    }

    private fun observeSearchResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.searchResult.collect { result ->
                    when (result) {
                        is SearchUiState.Loading -> {
                            Log.d("SearchFragment", "Loading search results...")
                        }
                        is SearchUiState.Empty -> {
                            Log.d("SearchFragment", "No search results found.")
                        }
                        is SearchUiState.Error -> {
                            Log.e("SearchFragment", "Error fetching search results: ${result.message}")
                        }
                        is SearchUiState.Success -> {
                            Log.d("SearchFragment", "Search results fetched successfully: ${result.data.size} items")
                            // submit list
                            searchAdapter.submitList(result.data)
                        }
                    }
                }
            }
        }
    }



}