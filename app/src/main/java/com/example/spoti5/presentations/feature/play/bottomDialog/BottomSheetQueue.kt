package com.example.spoti5.presentations.feature.play.bottomDialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.spoti5.R
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentBottomSheetQueueBinding
import com.example.spoti5.domain.model.player.TrackItemModel
import com.example.spoti5.presentations.feature.play.PlayMusicViewModel
import com.example.spoti5.presentations.feature.play.UiState.ItemUiState
import com.example.spoti5.presentations.feature.play.adapter.TrackItemAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
open class BottomSheetQueue : BottomSheetDialogFragment() {

    private val viewModel: PlayMusicViewModel by activityViewModels()
    private var itemAdapter = TrackItemAdapter()
    private val TAG = "BottomSheet"

    private var _binding: FragmentBottomSheetQueueBinding? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetQueueBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup rv
        setUpRecycleView()

        viewModel.fetchUserQueue()

        // observe
        observeUserQueue()


    }

    private fun setUpRecycleView() = with(binding) {
        rvQueue.apply {
            adapter = itemAdapter

            itemAdapter.setOnItemClickListener { track ->
                Log.d(TAG,"item click ${track.name}")
            }

        }
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

                            val current = state.data.currentlyPlaying
                            val queue = state.data.queue ?: emptyList()

                            val fullList = if (current != null) {
                                listOf(current) + queue
                            } else {
                                queue
                            }

                            itemAdapter.submitList(fullList)

                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}