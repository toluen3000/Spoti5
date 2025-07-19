package com.example.spoti5.presentations.feature.artist

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentArtistDetailBinding
import com.example.spoti5.presentations.feature.Home.adapter.AlbumAdapter
import com.example.spoti5.presentations.feature.Home.adapter.ArtistAdapter
import com.example.spoti5.presentations.feature.artist.UiState.ArtistUiState
import com.example.spoti5.presentations.feature.artist.ViewModel.ArtistDetailViewModel
import com.example.spoti5.presentations.feature.artist.adapter.ArtistDetailAdapter
import com.example.spoti5.presentations.feature.artist.adapter.SeveralArtistAdapter
import com.example.spoti5.presentations.feature.details.DetailsAlbumFragmentArgs
import com.example.spoti5.presentations.feature.details.adapter.DetailsAlbumAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class ArtistDetailFragment : BaseFragment<FragmentArtistDetailBinding>() {

    private lateinit var artistId: String
    private var imgUrl: String? = null
    private val TAG = "ArtistDetailFragment"

    private val viewmodel: ArtistDetailViewModel by viewModels()
    private var trackAdapter = ArtistDetailAdapter(imgUrl)
    private var artistAdapter = SeveralArtistAdapter()
    private var artistAlbumAdapter = AlbumAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentArtistDetailBinding {
        return FragmentArtistDetailBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: ArtistDetailFragmentArgs by navArgs()
        artistId = args.artistId
        imgUrl = args.imgUrl

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "Artist ID: $artistId")

        setupSongRecycleView()
        setUpArtistRecycleView()
        setUpAlbumArtistRecycleView()

        viewmodel.fetchArtistInfor(artistId)

        // cant fetch related artists due to API limitations, i will replacing with dummy data
        // viewmodel.fetchRelatedArtists(artistId)
        viewmodel.fetchSeveralArtists(DUMMY_ARTIST_ID)
        viewmodel.fetchArtistTopSongs(artistId)
        viewmodel.fetchArtistAlbums(artistId)


        observeArtistState()
//        observeRelatedArtistState()
        observeSeveralArtists()
        observerTopSongState()
        observerArtistAlbumsState()

        backButtonClickListener()
    }

    private fun setUpAlbumArtistRecycleView() = with(binding) {
        binding.rvArtistAlbum.apply {
            artistAlbumAdapter = AlbumAdapter()
            adapter = artistAlbumAdapter

            artistAlbumAdapter.setOnItemClickListener { album ->
                Log.d(TAG, "Album clicked: ${album.name} , ${album.images?.firstOrNull()?.url}")
                val action = ArtistDetailFragmentDirections.actionArtistDetailFragmentToDetailsAlbumFragment(
                    album.id,
                    album.images?.firstOrNull()?.url ?: ""
                )
                findNavController().navigate(action)
            }
        }
    }

    private fun observerArtistAlbumsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.artistAlbums.collect { state ->
                    when (state) {
                        ArtistUiState.Empty -> {
                            Log.d(TAG, "Artist albums is empty")
                        }
                        is ArtistUiState.Error -> {
                            Log.e(TAG, "Error fetching artist albums: ${state.message}")
                        }
                        ArtistUiState.Loading -> {
                            Log.d(TAG, "Loading artist albums...")
                        }
                        is ArtistUiState.Success -> {
                            Log.d(TAG, "Fetched ${state.data.size} artist albums")
                            artistAlbumAdapter.submitList(state.data)

                        }
                    }
                }
            }
        }
    }

    private fun observeSeveralArtists() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.severalArtists.collect { state ->
                    when (state) {
                        ArtistUiState.Empty -> {
                            Log.d(TAG, "Several artists is empty")
                        }
                        is ArtistUiState.Error -> {
                            Log.e(TAG, "Error fetching several artists: ${state.message}")
                        }
                        ArtistUiState.Loading -> {
                            Log.d(TAG, "Loading several artists...")
                        }
                        is ArtistUiState.Success -> {

                            binding.rvRelatedArtist.adapter = artistAdapter
                            artistAdapter.submitList(state.data)
                            Log.d(TAG, "Fetched ${state.data.size} several artists")
                            Log.d(TAG, "Several artists: ${state.data.joinToString { it.name ?: "Unknown Artist" }}")
                        }
                    }
                }
            }
        }
    }

    private fun backButtonClickListener() {
        binding.btnBack.setOnClickListener {
            Log.d(TAG, "Back button clicked")
            findNavController().navigateUp()
        }
    }

    private fun setUpArtistRecycleView() = with(binding) {
        binding.rvRelatedArtist.apply {
            artistAdapter = SeveralArtistAdapter()
            adapter = artistAdapter
            artistAdapter.setOnItemClickListener { artist ->
                Log.d(TAG, "Artist clicked: ${artist.name} , ${artist.images?.firstOrNull()?.url}")
                val imageUrl = artist.images?.firstOrNull()?.url ?: ""
                val action = ArtistDetailFragmentDirections.actionArtistDetailFragmentSelf(imageUrl,artist.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun setupSongRecycleView() = with(binding) {
        binding.rvArtistSong.apply {
            trackAdapter = ArtistDetailAdapter(imgUrl)
            adapter = trackAdapter
            trackAdapter.setOnItemClickListener { track ->
                Log.d(TAG, "Track clicked: ${track.name}")
                Toast.makeText(context, "Clicked on: ${track.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observerTopSongState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.artistTopSong.collect { state ->

                    when(state){
                        ArtistUiState.Empty -> {
                            Log.d(TAG, "Artist top song is empty")
                        }
                        is ArtistUiState.Error -> {
                            Log.e(TAG, "Error fetching artist top songs: ${state.message}")
                        }
                        ArtistUiState.Loading -> {
                            Log.d(TAG, "Loading artist top songs...")
                        }
                        is ArtistUiState.Success -> {
                            trackAdapter.submitList(state.data)
                            binding.rvArtistSong.adapter = trackAdapter
                            Log.d(TAG, "Fetched ${state.data.size} artist top songs")
                        }
                    }

                }
            }
        }
    }

    private fun observeRelatedArtistState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.relatedArtist.collect { state ->
                    when (state){
                        ArtistUiState.Empty -> {
                            Log.d(TAG, "Related artist is empty")
                        }
                        is ArtistUiState.Error -> {
                            Log.e(TAG, "Error fetching related artists: ${state.message}")
                        }
                        ArtistUiState.Loading -> {
                            Log.d(TAG, "Loading related artists...")
                        }
                        is ArtistUiState.Success -> {
//                            artistAdapter.submitList(state.data)
                            binding.rvRelatedArtist.adapter = artistAdapter
                            Log.d(TAG, "Fetched ${state.data.size} related artists")
                        }
                    }
                }
            }
        }
    }

    private fun observeArtistState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewmodel.artistState.collect { state ->
                    when (state) {
                        ArtistUiState.Empty -> {
                            Log.d(TAG,"Empty Artist")
                        }
                        is ArtistUiState.Error -> {
                            Log.e(TAG,"Fetch Artist infor Error")
                        }
                        ArtistUiState.Loading -> {
                            Log.d(TAG,"Loading fetch A Infor")
                        }
                        is ArtistUiState.Success -> {
                            binding.txtArtistName.text = state.data.name.toString()
                            val imageUrl = state.data.images?.firstOrNull()?.url
                            Glide.with(this@ArtistDetailFragment)
                                .load(imageUrl)
                                .into(binding.imageView4)
                        }
                    }
                }
            }
        }

    }

    companion object{
        private const val DUMMY_ARTIST_ID = "6eUKZXaKkcviH0Ku9w2n3V,06HL4z0CvFAxyc27GXpf02,6qqNVTkY8uBg9cP3Jd7DAH,3TVXtAsR1Inumwj472S9r4,1Xyo4u8uXC1ZmMpatF05PJ,66CXWjxzNUsdJxJ2JdwvnR,04gDigrS5kc9YWfZHwBETP,5cj0lLjcoR7YOSnhnX0Po5"
    }

}