package com.example.spoti5.presentations.feature.library.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.spoti5.presentations.feature.library.fragviewpager2.AlbumLibraryFragment
import com.example.spoti5.presentations.feature.library.fragviewpager2.ArtistLibraryFragment
import com.example.spoti5.presentations.feature.library.fragviewpager2.PostcastLibraryFragment

class ViewPager2Adapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {


    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return AlbumLibraryFragment()
        } else if (position == 1) {
            return ArtistLibraryFragment()
        } else {
            return PostcastLibraryFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }


}