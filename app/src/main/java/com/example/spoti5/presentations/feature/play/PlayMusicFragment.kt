package com.example.spoti5.presentations.feature.play

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.spoti5.R
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentPlayMusicBinding
import com.example.spoti5.domain.model.player.utils.PlaybackState
import com.example.spoti5.domain.model.track.TrackModel
import com.example.spoti5.presentations.feature.play.UiState.ItemUiState
import com.example.spoti5.presentations.feature.play.UiState.PlayerUiState
import com.example.spoti5.presentations.feature.play.bottomDialog.BottomSheetDevice
import com.example.spoti5.presentations.feature.play.bottomDialog.BottomSheetQueue
import com.example.spoti5.utils.RepeatMode
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


@AndroidEntryPoint
class PlayMusicFragment : BaseFragment<FragmentPlayMusicBinding>() {

    private val viewModel: PlayMusicViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Connecting to Spotify")
        viewModel.connectSpotify()
    }



    private lateinit var idTrack: String
    private var uri: String? = null
    private var isPlaying: Boolean = false
    private val TAG = "PlayMusicFragment"

    private lateinit var deviceIdPlaying : String

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
        isPlaying = args.isPlaying

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
        Log.d(TAG, "Is Playing: $isPlaying")




        viewModel.fetchTrackById(idTrack)
        viewModel.fetchAvailableDevices()

        if (!isPlaying){
            viewModel.play("spotify:track:$idTrack")
            Log.d(TAG,"spotify:track:$idTrack")
            viewModel.fetchTrackById(idTrack)
        }


        // observe viewmodel
        observeTrackImg()
        observerDeviceState()
        observeRepeatState()
        observeUserQueue()
        observePlayerState()
        observeSeekbar()

        // seekbar changes
        seekBarOnChangeListener()

        // buttons on click
        btnBackSetOnClick()
        btnDeviceSetOnClick()
        btnPlayPauseSetOnClick()
        btnNextSetOnClick()
        btnPreviousSetOnClick()
        btnShuffleSetOnClick()
        btnRepeatSetOnClick()
        btnUserQueueSetOnClick()

    }

    private fun openBottomSheet() {
        val bottomSheet = BottomSheetQueue()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun btnUserQueueSetOnClick() {

        binding.btnQueue.setOnClickListener {
            openBottomSheet()
        }
    }

    private fun btnDeviceSetOnClick() {

        binding.btnDevice.setOnClickListener {
            openBottomDialog()
        }
    }

    private fun openBottomDialog() {
        val bottomSheet = BottomSheetDevice()
        bottomSheet.show(childFragmentManager, bottomSheet.tag)
    }

    private fun observeUserQueue() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.userQueueState.collect { state ->
                    when (state){
                        ItemUiState.Empty -> {
                            Log.d(TAG,"User Queue is Empty")
                        }
                        is ItemUiState.Error -> {
                            Log.d(TAG,"User Queue Error ${state.message}")
                        }
                        ItemUiState.Loading -> {
                            Log.d(TAG,"User Queue Loading")
                        }
                        is ItemUiState.Success -> {
                            Log.d(TAG,"User Queue Success ${state.data.queue?.size ?: "null"}")

                        }
                    }
                }
            }
        }
    }

    private fun observeRepeatState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.repeatModeState.collect { state ->
                    when (state) {
                        ItemUiState.Empty -> {
                            Log.d(TAG, "No repeat state found.")
                        }
                        is ItemUiState.Error -> {
                            Log.e(TAG, "Error fetching repeat state: ${state.message}")
                        }
                        ItemUiState.Loading -> {
                            Log.d(TAG, "Loading repeat state...")
                        }
                        is ItemUiState.Success -> {
                            Log.d(TAG, "Repeat state loaded successfully: ${state.data}")
                        }
                    }
                }
            }
        }
    }


    private fun btnRepeatSetOnClick() {
         var count = 0
        binding.btnRepeat.setOnClickListener {
            count++
            Log.d(TAG, "Repeat button clicked, count: $count")
            when (count) {
                1 -> {
                    viewModel.setRepeatMode(RepeatMode.OFF.toApiValue(), deviceIdPlaying)
                    binding.btnRepeat.setImageResource(R.drawable.ic_repeat)
                }
                2 -> {
                    viewModel.setRepeatMode(RepeatMode.CONTEXT.toApiValue(), deviceIdPlaying)
                    binding.btnRepeat.setImageResource(R.drawable.ic_repeat_album)
                }
                3 -> {
                    viewModel.setRepeatMode(RepeatMode.TRACK.toApiValue(), deviceIdPlaying)
                    binding.btnRepeat.setImageResource(R.drawable.ic_repeat_selected)
                }
                else -> {
                    count = 0
                    viewModel.setRepeatMode(RepeatMode.OFF.toApiValue() , deviceIdPlaying)
                    binding.btnRepeat.setImageResource(R.drawable.ic_repeat)
                }
            }
        }
    }

    private fun btnShuffleSetOnClick() {
        var count = 1
        binding.btnShuffle.setOnClickListener {
            if (count % 2 != 0 ){
                viewModel.toggleShuffleButton()
                binding.btnShuffle.setImageResource(R.drawable.ic_button_shuffle_selected)
                count ++
            }else{
                binding.btnShuffle.setImageResource(R.drawable.ic_button_shuffle)
                count++
            }

        }
    }

    private fun btnPreviousSetOnClick() {
        binding.btnBackward.setOnClickListener {
            Log.d(TAG, "Previous button clicked")
            viewModel.previous()
            Log.d(TAG, "Skipped to previous track")
        }
    }

    private fun btnNextSetOnClick() {
        binding.btnForward.setOnClickListener {
            Log.d(TAG, "Next button clicked")
            viewModel.next()
            Log.d(TAG,"Fetch: ${viewModel.playerState.value.artistName}")
            Log.d(TAG, "Skipped to next track")
        }
    }

    private fun observeTrackImg() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trackState.collect { state ->

                    when (state) {
                        ItemUiState.Empty -> {
                            Log.d(TAG, "No track image found.")
                        }
                        is ItemUiState.Error -> {
                            Log.e(TAG, "Error fetching track image: ${state.message}")
                        }
                        ItemUiState.Loading -> {
                            Log.d(TAG, "Loading track image...")
                        }
                        is ItemUiState.Success -> {
                            Log.d(TAG, "Track image loaded successfully: ${state.data}")
                            Log.d(TAG,"Img url: ${state.data.album?.images?.firstOrNull()?.url}")
                            val imgUrl = state.data.album?.images?.firstOrNull()?.url

                            Glide.with(requireContext())
                                .load(imgUrl)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .dontAnimate()
                                .into(binding.imgTrackCover)

                            Log.d(TAG,"Img url: Loaded")
                        }
                    }
                }
            }
        }
    }

    private fun seekBarOnChangeListener()   {
        binding.musicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.seekTo(progress.toLong())
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun btnPlayPauseSetOnClick() {
        binding.btnPlaying.setOnClickListener {
            if (viewModel.playerState.value.isPlaying) {
                viewModel.pause()
                Log.d(TAG, "Paused playback")
            } else {
                viewModel.resume()
                Log.d(TAG, "Resumed playback")
            }
        }
    }

    private fun observePlayerState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playbackState.collect { state ->

                    if (state is PlayerUiState.Success) {
                       bindPlayer(state.data)
                    } else {
                        Log.d(TAG, "Player state is not successful: $state")
                    }

                }
            }
        }
    }

    private fun observeSeekbar() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerState.collect { state ->
                    binding.musicSeekBar.progress = state.positionMs.toInt()
                    binding.txtStartDuration.text = state.positionMs.let { position ->
                        // Convert milliseconds to a formatted string (mm:ss)
                        val minutes = (position / 1000) / 60
                        val seconds = (position / 1000) % 60
                        String.format("%02d:%02d", minutes, seconds)
                    }

                    binding.txtTotalDuration.text = state.durationMs.let { duration ->
                        // Convert milliseconds to a formatted string (mm:ss)
                        val minutes = (duration / 1000) / 60
                        val seconds = (duration / 1000) % 60
                        String.format("%02d:%02d", minutes, seconds)
                    }
                    binding.musicSeekBar.max = state.durationMs.toInt()
                }
            }
        }
    }


    private fun bindPlayer(state: PlaybackState) {
        binding.txtPlayingFrom.text = "Playing from ${state.albumName}"
        binding.txtTitle.text = state.trackName
        binding.txtArtist.text = state.artistName
        binding.musicSeekBar.progress = state.positionMs.toInt()
        binding.musicSeekBar.max = state.durationMs.toInt().coerceAtLeast(1)

        binding.txtTitle.isSelected = true

        val trackIdFromState = state.trackUri.substringAfter("spotify:track:")
        if (trackIdFromState != idTrack) {
            viewModel.fetchTrackById(trackIdFromState)
            idTrack = trackIdFromState
        }

        if (state.isPlaying){
            binding.btnPlaying.setImageResource(R.drawable.ic_pause)
        } else {
            binding.btnPlaying.setImageResource(R.drawable.ic_play_button)
        }


}





    private fun observerDeviceState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.inforPlaybackState.collect { state ->
                    when (state) {
                        ItemUiState.Empty -> {
                            Log.d(TAG, "No devices found.")

                        }
                        is ItemUiState.Error -> {
                            Log.e(TAG, "Error fetching devices: ${state.message}")
                        }
                        ItemUiState.Loading -> {
                            Log.d(TAG, "Loading devices...")
                        }
                        is ItemUiState.Success -> {
                            Log.d(TAG, "Devices loaded successfully: ${state.data}")

                            // take id of device that is active
                            val activeDevice = state.data.firstOrNull { it.isActive ?: false }
                            deviceIdPlaying = activeDevice?.id ?: "Unknown Device"
                            Log.d("Playing",deviceIdPlaying)
                        }
                    }
                }
            }
        }
    }

    private fun btnBackSetOnClick() {
        binding.btnBackFromPlay.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}