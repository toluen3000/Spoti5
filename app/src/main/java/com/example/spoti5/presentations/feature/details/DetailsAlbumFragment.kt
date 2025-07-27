package com.example.spoti5.presentations.feature.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LongDef
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.spoti5.R
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentDetailsAlbumBinding
import com.example.spoti5.presentations.feature.artist.ArtistDetailFragmentDirections
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.presentations.feature.details.UiState.CheckSavedAlbumState
import com.example.spoti5.presentations.feature.details.UiState.DeleteUiState
import com.example.spoti5.presentations.feature.details.UiState.SaveUiState
import com.example.spoti5.presentations.feature.details.adapter.DetailsAlbumAdapter
import com.google.android.play.integrity.internal.a
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
        // Check if the album is already saved
        viewModel.checkIfAlbumIsSaved(albumId)

        // Observe the album state
        observeAlbumState()
        observeTrackItemsState()
        // Observe the state of checking if the album is saved
        observeCheckIfAlbumIsSavedState()
        // Observe the state of saving the album
        observeSaveAlbumState()
        // Observe the state of deleting the album
        observeDeleteAlbumState()




        // Back btn
        backBtnOnClick()

        // Save btn
        saveBtnOnClick()



    }

    private fun observeDeleteAlbumState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.deleteAlbumState.collect { state ->
                    when (state){
                        is DeleteUiState.DeleteError -> {
                            Log.d("DetailsAlbumFragment", "Error deleting album: ${state.message}")
                        }
                        DeleteUiState.Deleted -> {
                            Log.d("DetailsAlbumFragment", "Album deleted successfully")
                            Toast.makeText(context, "Album removed from your library", Toast.LENGTH_SHORT).show()
                        }
                        DeleteUiState.Deleting -> {
                            Log.d("DetailsAlbumFragment", "Deleting album...")
                            Toast.makeText(context, "Deleting album...", Toast.LENGTH_SHORT).show()
                        }
                        DeleteUiState.Idle -> {
                            Log.d("DetailsAlbumFragment", "Delete state is idle")
                        }
                    }
                }
            }
        }
    }

    private fun saveBtnOnClick() {
         // save album to the user's library
        binding.btnLike.setOnClickListener {
            viewModel.toggleAlbum(albumId)
            Log.d("DetailsAlbumFragment", "Save/Remove button clicked for album ID: $albumId")
        }

    }

    private fun observeCheckIfAlbumIsSavedState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isAlbumSavedState.collect { state ->
                    when(state){

                        is CheckSavedAlbumState.Error -> {
                            Log.e("DetailsAlbumFragment", "Error checking saved album state: ${state.message}")
                            Toast.makeText(context, "Error checking saved album state", Toast.LENGTH_SHORT).show()
                        }
                        CheckSavedAlbumState.Idle -> {
                            Log.d("DetailsAlbumFragment", "Check saved album state is idle")
                            // This state can be used to reset the UI or perform any other action
                        }
                        CheckSavedAlbumState.Loading -> {
                            Log.d("DetailsAlbumFragment", "Checking if album is saved...")

                        }
                        is CheckSavedAlbumState.Success -> {
                            Log.d("DetailsAlbumFragment", "Album is saved: ${state.isSaved}")
                            if (state.isSaved) {
                                // If the album is already saved, you might want to change the button state or show a message
                                val icon = R.drawable.ic_red_heart
                                binding.btnLike.setImageResource(icon)
                                Log.d("DetailsAlbumFragment", "Album is already saved")
                            } else {
                                Log.d("DetailsAlbumFragment", "Album is not saved")
                                val icon = R.drawable.ic_heart_thin
                                binding.btnLike.setImageResource(icon)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeSaveAlbumState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.saveAlbumState.collect { state ->
                    when (state) {
                        SaveUiState.Idle -> {
                            Log.d("DetailsAlbumFragment", "Save state is idle")
                        }
                        SaveUiState.Saving -> {
                            Log.d("DetailsAlbumFragment", "Saving album to user library...")
                            Toast.makeText(context, "Saving album...", Toast.LENGTH_SHORT).show()
                        }
                        SaveUiState.Saved -> {
                            Log.d("DetailsAlbumFragment", "Album saved successfully")
                            Toast.makeText(
                                context,
                                "Album saved to your library",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        is SaveUiState.SaveError -> {
                            Log.e("DetailsAlbumFragment", "Error saving album: ${state.message}")
                        }
                    }
                }
            }
        }
    }

    private fun backBtnOnClick() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
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

                       }
                   }
               }
           }
       }
    }

    /**
     * Observe the album state from the ViewModel and update the UI accordingly.
     */
    private fun observeAlbumState() {

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
                Log.d("DetailsAlbumFragment", "Track clicked: ${track.uri}")
                // navigate to playback
                val action = DetailsAlbumFragmentDirections.actionDetailsAlbumFragmentToPlayMusicFragment(
                    track.uri ?: "",
                    track.id,
                )
                findNavController().navigate(action)
                Toast.makeText(context, "Clicked on: ${track.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}