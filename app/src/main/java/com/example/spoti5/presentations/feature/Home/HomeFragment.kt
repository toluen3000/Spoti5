package com.example.spoti5.presentations.feature.Home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentHomeBinding
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val TAG: String = HomeFragment::class.java.simpleName

    private val viewModel: HomeViewModel by viewModels()




    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchUserInfo()

        lifecycleScope.launchWhenStarted {
            viewModel.userState.collect { state ->
                when (state) {
                    is MainUiState.Loading -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is MainUiState.Success -> {
                        // Hiển thị thông tin người dùng
                        Log.d(TAG, "onViewCreated: ${state.data?.name}")
                        binding.tvGreetings.text = "Hello ${state.data?.name ?: "User"}"
                    }
                    is MainUiState.Error -> {
                        // Hiển thị lỗi
                        Log.d(TAG, "onViewCreated: ${state.message}")
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

