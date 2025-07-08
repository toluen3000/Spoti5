package com.example.spoti5.presentations.feature.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentDetailsAlbumBinding
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.presentations.feature.details.adapter.DetailsAlbumAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsAlbumFragment : BaseFragment<FragmentDetailsAlbumBinding>() {




    private lateinit var albumId: String
    private var imgUrl: String? = null


    private val viewModel: DetailsViewModel by viewModels()
    private var trackAdapter = DetailsAlbumAdapter( imgUrl = imgUrl)

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsAlbumBinding {
        return FragmentDetailsAlbumBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // receive arguments from the previous fragment
        val args: DetailsAlbumFragmentArgs by navArgs()
        albumId = args.albumId
        imgUrl = args.imageUrl
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        Log.d("DetailsAlbumFragment", "Album ID: $albumId")
        Log.d("DetailsAlbumFragment", "Image URL: $imgUrl")
        viewModel.fetchAlbumById(albumId)
        viewModel.fetchAlbumTracksById(albumId)

        // Observe the album state
        oberserveAlbumState()
        observeTrackItemsState()
    }

    /**
     * Observe the track items state from the ViewModel and update the UI accordingly.
     */

    private fun observeTrackItemsState() {
       viewLifecycleOwner.lifecycleScope.launch {
           repeatOnLifecycle(Lifecycle.State.STARTED){
               viewModel.trackItemsState.collect { state ->
                   when(state){
                       is MainUiState.Error -> {
                           Log.e("DetailsAlbumFragment", "Error fetching track items: ${state.message}")
                       }
                       MainUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            Log.d("DetailsAlbumFragment", "Loading track items...")
                       }
                       is MainUiState.Success-> {
                            Log.d("DetailsAlbumFragment", "Fetched track items: ${state.data}")
                            binding.progressBar.visibility = View.GONE
                            trackAdapter.submitList(state.data)
                            binding.rvTracks.adapter = trackAdapter


                            // Set up click listener for each track item
                            trackAdapter.setOnItemClickListener { track ->
                                 Log.d("DetailsAlbumFragment", "Track clicked: ${track.name}")
                                 Toast.makeText(context, "Clicked on: ${track.name}", Toast.LENGTH_SHORT).show()
                            }
                       }
                   }
               }
           }
       }
    }

    /**
     * Observe the album state from the ViewModel and update the UI accordingly.
     */
    private fun oberserveAlbumState() {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.albumState.collect { state ->
                    when (state) {
                        is MainUiState.Error -> {
                            Log.e("DetailsAlbumFragment", "Error fetching album: ${state.message}")
                        }

                        MainUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is MainUiState.Success -> {
                            Log.d("DetailsAlbumFragment", "Fetched album: ${state.data}")
                            binding.progressBar.visibility = View.GONE
                            binding.txtAlbumName.text = state.data.name
                            binding.txtAlbumDesc.text =
                                "Spotify API not support description for album"

                            val imageUrl = state.data.images?.firstOrNull()?.url
                            if (!imageUrl.isNullOrEmpty()) {
                                Glide.with(this@DetailsAlbumFragment)
                                    .load(imageUrl)
                                    .into(binding.imgAlbum)
                            }
                        }
                    }
                }

            }


        }

    }

    private fun setUpRecyclerView() = with(binding) {

        binding.rvTracks.apply {
            trackAdapter = DetailsAlbumAdapter(imgUrl = imgUrl)
            adapter = trackAdapter
            trackAdapter.setOnItemClickListener { track ->
                Log.d("DetailsAlbumFragment", "Track clicked: ${track.name}")
                Toast.makeText(context, "Clicked on: ${track.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}