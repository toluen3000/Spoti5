package com.example.spoti5.presentations.feature.Home

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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentHomeBinding
import com.example.spoti5.presentations.feature.Home.adapter.AlbumAdapter
import com.example.spoti5.presentations.feature.Home.adapter.ArtistAdapter
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val TAG: String = HomeFragment::class.java.simpleName

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var albumAdapter: AlbumAdapter

    private lateinit var artistAdapter: ArtistAdapter


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        setUpArtistRecycleView()

        viewModel.fetchUserInfo()
        viewModel.fetchNewAlbums()
        viewModel.fetchArtistsFromNewAlbumsRelease()

        // observe artist
        observeArtist()



        // Fetch user info
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.userState.collect { state ->
                    when (state) {
                        is MainUiState.Loading -> {
                            //
                            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                        }
                        is MainUiState.Success -> {
                            // Hiển thị thông tin người dùng
                            Log.d(TAG, "onViewCreated: ${state.data?.name}")
                            binding.tvGreetings.text = "Hello ${state.data?.name ?: "User"}!"
                            Glide.with(this@HomeFragment)
                                .load(state.data?.imageUrl)
                                .into(binding.imgUserAvatar)
                        }
                        is MainUiState.Error -> {
                            Log.d(TAG, "onViewCreated: ${state.message}")
                        }
                    }
                }
            }
        }

        // Fetch new albums
            viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.albumsState.collect { state ->
                    when (state) {
                        is MainUiState.Loading -> {
                            Log.d(TAG, "Loading albums...")
                        }
                        is MainUiState.Success -> {
                            Log.d(TAG, "Fetched ${state.data.size} albums")
                            albumAdapter.submitList(state.data)
                        }
                        is MainUiState.Error -> {
                            Log.d(TAG, "Error fetching albums: ${state.message}")
                        }
                    }
                }
            }
        }
    }

    private fun observeArtist() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.artistState.collect { state ->
                    when (state) {
                        is MainUiState.Error -> {
                            Log.d(TAG,"Error fetching Artist : ${state.message}")
                        }
                        MainUiState.Loading -> {
                            Log.d(TAG, "Loading Artist...")
                        }
                        is MainUiState.Success-> {
                            Log.d(TAG, "Fetched ${state.data.size} Artist")
                            artistAdapter.submitList(state.data)
                        }
                    }
                }
            }
        }
    }

    private fun setUpArtistRecycleView() = with(binding) {
        binding.rvArtists.apply {
            artistAdapter = ArtistAdapter()
            adapter = artistAdapter

            artistAdapter.setOnItemClickListener { artist ->
                // get artist id when click
                Log.d(TAG, "Artist clicked: ${artist.name} with ID: ${artist.id}")
                val action = HomeFragmentDirections.actionHomeScreenFragmentToArtistDetailFragment(artistId = artist.id,
                    artist.imageUrl)
                findNavController().navigate(action)
            }
        }
    }

    private fun setUpRecyclerView() = with(binding) {
        binding.rvAlbums.apply {
           albumAdapter = AlbumAdapter()
            adapter = albumAdapter
            albumAdapter.setOnItemClickListener { album ->

                // get album id and move to details screen
                Log.d(TAG, "Album clicked: ${album.name} with ID: ${album.id}")

                //show imgUrl
                val imageUrl = album.images?.firstOrNull()?.url
                Log.d(TAG, "Album image URL: ${imageUrl ?: "No image available"}")
                // Navigate to album details screen
                val action = HomeFragmentDirections.actionHomeScreenFragmentToDetailsAlbumFragment(album.id,imageUrl)
                findNavController().navigate(action)
            }
        }


    }
}

