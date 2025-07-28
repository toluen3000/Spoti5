package com.example.spoti5.presentations.feature.play

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.spoti5.R
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentPlayMusicBinding
import com.example.spoti5.presentations.feature.artist.ArtistDetailFragmentArgs
import com.example.spoti5.presentations.feature.play.UiState.ItemUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class PlayMusicFragment : BaseFragment<FragmentPlayMusicBinding>() {

    private val viewModel: PlayMusicViewModel by viewModels()


    private lateinit var idTrack: String
    private var uri: String? = null
    private val TAG = "PlayMusicFragment"

    // hide bottom navigation when navigating to this fragment
    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<View>(R.id.bottomNavigationView).visibility = View.GONE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: PlayMusicFragmentArgs by navArgs()
        idTrack = args.idTrack
        uri = args.uri

    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayMusicBinding {
        return FragmentPlayMusicBinding.inflate(inflater,  container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "Track ID: $idTrack")
        Log.d(TAG, "Track URI: $uri")

        viewModel.fetchTrackById(idTrack)



        observerTrackState()

        btnBackSetOnClick()

    }

    private fun btnBackSetOnClick() {
        binding.btnBackFromPlay.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observerTrackState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.trackState.collect { state ->
                    when (state) {
                        is ItemUiState.Loading -> {
                            Log.d(TAG, "Loading track data...")
                        }
                        is ItemUiState.Success -> {
                            Log.d(TAG, "Track data loaded successfully: ${state.data}")
                            Glide.with(requireContext())
                                .load(state.data.album?.images?.firstOrNull()?.url)
                                .into(binding.imgTrackCover)

                            binding.apply {
                                binding.txtTitle.text = state.data.name
                                binding.txtArtist.text = state.data.artists?.joinToString(", ") { it.name.toString() }
                                binding.txtTotalDuration.text = state.data.durationMs.let { duration ->
                                    // Convert milliseconds to a formatted string (mm:ss)
                                    val minutes = (duration / 1000) / 60
                                    val seconds = (duration / 1000) % 60
                                    String.format("%02d:%02d", minutes, seconds)

                                }

                                binding.txtTitle.isSelected = true // For marquee effect
                            }
                        }
                        is ItemUiState.Error -> {
                            Log.e(TAG, "Error loading track data: ${state.message}")
                        }
                        is ItemUiState.Empty -> {
                            Log.d(TAG, "No track data found.")
                        }
                    }
                }
            }
        }
    }


}