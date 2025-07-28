package com.example.spoti5.presentations.feature.library.fragviewpager2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.spoti5.R
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentTrackLibraryBinding
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.presentations.feature.library.LibraryFragmentDirections
import com.example.spoti5.presentations.feature.library.LibraryViewModel
import com.example.spoti5.presentations.feature.library.adapter.TrackItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class TrackLibraryFragment: BaseFragment<FragmentTrackLibraryBinding>() {

    private val TAG: String = TrackLibraryFragment::class.java.simpleName

    private val viewModel: LibraryViewModel by viewModels()

    private lateinit var trackAdapter: TrackItemAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrackLibraryBinding {
        return FragmentTrackLibraryBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchUserSavedTracks()
        setUpRecyclerView()

        observerUserSavedTracks()
    }

    private fun observerUserSavedTracks() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userTracksSavedState.collect { state ->
                    when (state) {
                        is MainUiState.Loading -> {
                            Log.d(TAG, " Loading tracks")
                        }
                        is MainUiState.Success -> {
                            // Update the adapter with the list of tracks
                            trackAdapter.submitList(state.data)
                            Log.d(TAG, "Tracks loaded successfully: ${state.data.size} tracks")
                        }
                        is MainUiState.Error -> {
                            Log.e(TAG, "Error loading tracks: ${state.message}")
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() = with(binding) {
        binding.rvTracks.apply {
            trackAdapter = TrackItemAdapter()
            adapter = trackAdapter

            trackAdapter.setOnItemClickListener { track ->
                // Handle item click, navigate to details
                Log.d(TAG, "Track clicked: ${track.name}")
                val action = LibraryFragmentDirections.actionLibraryScreenFragmentToPlayMusicFragment(uri = track.uri, idTrack = track.id)
                findNavController().navigate(action)

            }
        }


    }

}