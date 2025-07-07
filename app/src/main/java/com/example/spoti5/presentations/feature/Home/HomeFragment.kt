package com.example.spoti5.presentations.feature.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentHomeBinding
import com.example.spoti5.presentations.feature.Home.adapter.AlbumAdapter
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val TAG: String = HomeFragment::class.java.simpleName

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var albumAdapter: AlbumAdapter


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.fetchUserInfo()
        viewModel.fetchNewAlbums()



        // Fetch user info
        lifecycleScope.launchWhenStarted {
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

        // Fetch new albums
        lifecycleScope.launchWhenStarted {
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

    private fun setUpRecyclerView() = with(binding) {
        binding.rvAlbums.apply {
           albumAdapter = AlbumAdapter()
            adapter = albumAdapter
            albumAdapter.setOnItemClickListener { album ->

                Toast.makeText(context, "Clicked on: ${album.name}", Toast.LENGTH_SHORT).show()
            }
        }


    }
}

