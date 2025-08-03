package com.example.spoti5.presentations.feature.library.fragviewpager2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.spoti5.R
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentArtistLibraryBinding
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.presentations.feature.library.LibraryViewModel
import com.example.spoti5.presentations.feature.library.adapter.ArtistItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistLibraryFragment : BaseFragment<FragmentArtistLibraryBinding>() {

    private val viewModel: LibraryViewModel by viewModels()
    private val TAG: String = ArtistLibraryFragment::class.java.simpleName

    private var artistAdapter = ArtistItemAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentArtistLibraryBinding {
        return FragmentArtistLibraryBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        setUpRecyclerView()

        viewModel.fetchUserSavedArtists()
        // Observe the artists saved state
        observeArtistState()



    }

    private fun setUpRecyclerView() = with(binding) {
        rvArtist.apply {
            artistAdapter = ArtistItemAdapter()
            adapter = artistAdapter

            artistAdapter.setOnItemClickListener { artist ->
                // Handle item click, navigate to details
                Toast.makeText(requireContext(), "Artist clicked: ${artist.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeArtistState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.artistsSavedState.collect { state ->
                    when (state) {
                        is MainUiState.Error -> {
                            Log.d(TAG, "onViewCreated: ${state.message}")
                        }
                        MainUiState.Loading -> {
                            Log.d(TAG, "onViewCreated: Loading artists...")
                        }
                        is MainUiState.Success -> {
                            Log.d(TAG, "onViewCreated: ${state.data}")
                            artistAdapter.submitList(state.data)
                        }
                    }
                }
            }
        }
    }

}