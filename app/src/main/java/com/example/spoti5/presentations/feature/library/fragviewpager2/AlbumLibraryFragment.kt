package com.example.spoti5.presentations.feature.library.fragviewpager2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentAlbumLibraryBinding
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.presentations.feature.library.LibraryFragmentDirections
import com.example.spoti5.presentations.feature.library.LibraryViewModel
import com.example.spoti5.presentations.feature.library.adapter.LibraryItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class AlbumLibraryFragment : BaseFragment<FragmentAlbumLibraryBinding>() {

    private val viewModel : LibraryViewModel by viewModels()
    private val TAG: String = AlbumLibraryFragment::class.java.simpleName


    private var libraryItemAdapter = LibraryItemAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAlbumLibraryBinding {
        return FragmentAlbumLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setUpRecyclerView()
        setUpRecyclerView()

        viewModel.fetchUserSavedAlbums()
        observeUserSavedAlbums()
    }

    private fun setUpRecyclerView() = with(binding){
        binding.rvAlbums.apply {
            libraryItemAdapter = LibraryItemAdapter()
            adapter = libraryItemAdapter
            libraryItemAdapter.setOnItemClickListener { album ->
                // Handle item click, navigate to details
                Log.d(TAG, "onViewCreated: Album clicked: ${album.name}")
                val action = LibraryFragmentDirections.actionLibraryScreenFragmentToDetailsAlbumFragment(album.id,
                    album.images?.firstOrNull()?.url)
                findNavController().navigate(action)
            }


        }

    }

    private fun observeUserSavedAlbums() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.albumsSavedState.collect { state ->
                    when (state) {
                        is MainUiState.Error -> {
                            Log.d(TAG, "onViewCreated: ${state.message}")
                        }

                        MainUiState.Loading -> {
                            Log.d(TAG, "onViewCreated: Loading albums")
                        }

                        is MainUiState.Success-> {
                            // test data response
                            Log.d(TAG, "onViewCreated: ${state.data}")
                            val itemAlbumList: List<AlbumModel> = state.data.map { it.album }
                            Log.d(TAG, "onViewCreated: itemAlbumList: $itemAlbumList")
                            libraryItemAdapter.submitList(itemAlbumList)

                        }
                    }
                }
            }
        }
    }


}