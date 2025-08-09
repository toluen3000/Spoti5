package com.example.spoti5.presentations.feature.play.bottomDialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.spoti5.data.apis.SpotifyApi
import com.example.spoti5.databinding.FragmentBottomSheetDeviceBinding
import com.example.spoti5.domain.model.player.utils.BottomSheetListener
import com.example.spoti5.presentations.feature.play.PlayMusicViewModel
import com.example.spoti5.presentations.feature.play.UiState.ItemUiState
import com.example.spoti5.presentations.feature.play.adapter.DeviceItemAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class BottomSheetDevice : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetDeviceBinding? = null
    protected val binding get() = _binding!!

    private val viewModel: PlayMusicViewModel by activityViewModels()
    private val TAG = "BottomSheet"
    private var deviceAdapter = DeviceItemAdapter()

    private var listener: BottomSheetListener? = null

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = FragmentBottomSheetDeviceBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

        viewModel.fetchAvailableDevices()

        observeAvailableDevices()
        observeTransferPlayback()


    }

    private fun observeTransferPlayback() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.tramsPlayback.collect { state ->
                    when (state){
                        ItemUiState.Empty -> {
                            Log.d(TAG,"Empty")
                        }
                        is ItemUiState.Error -> {
                            Log.d(TAG,"Error")
                        }
                        ItemUiState.Loading -> {
                            Log.d(TAG,"Loading")
                        }
                        is ItemUiState.Success -> {
                            Log.d(TAG,"Success")
                        }
                    }
                }
            }
        }
    }

    private fun observeAvailableDevices() {
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
                            deviceAdapter.submitList(state.data)

                        }
                    }
                }
            }
        }
    }

    private fun setUpRecycleView() = with(binding){
        rvDevices.apply {
            adapter = deviceAdapter

            deviceAdapter.setOnItemClickListener {
                Log.d(TAG,"Clicked on ${deviceAdapter.itemCount}")
                val bodyTrans = SpotifyApi.TransferPlaybackBody(
                    listOf(it.id),
                    true
                )
                viewModel.transferPlayback(bodyTrans)

                dismiss()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}