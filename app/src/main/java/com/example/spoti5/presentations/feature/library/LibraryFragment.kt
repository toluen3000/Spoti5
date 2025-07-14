package com.example.spoti5.presentations.feature.library

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.spoti5.R
import com.example.spoti5.base.BaseFragment
import com.example.spoti5.databinding.FragmentLibraryBinding
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.presentations.feature.auth.ViewModel.MainUiState
import com.example.spoti5.presentations.feature.library.adapter.LibraryItemAdapter
import com.example.spoti5.presentations.feature.library.adapter.ViewPager2Adapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {


    // view pager 2
    private lateinit var viewPager2Adapter: ViewPager2Adapter


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up ViewPager2 with TabLayout
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val adapter = ViewPager2Adapter(this)
        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Albums"
                1 -> "Artists"
                2 -> "Podcasts"
                else -> "Tab $position"
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        // Albums

                        binding.viewPager2.setCurrentItem(0, true)
                    }
                    1 -> {
                        // Artists

                        binding.viewPager2.setCurrentItem(1, true)
                    }
                    2 -> {
                        // Podcasts

                        binding.viewPager2.setCurrentItem(2, true)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


    }



}