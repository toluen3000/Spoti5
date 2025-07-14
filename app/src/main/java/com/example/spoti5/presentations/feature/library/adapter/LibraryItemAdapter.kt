package com.example.spoti5.presentations.feature.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.ItemLibraryRecylBinding
import com.example.spoti5.domain.model.album.AlbumModel

class LibraryItemAdapter(
): RecyclerView.Adapter<LibraryItemAdapter.LibraryItemViewHolder>() {

    private var albumList: List<AlbumModel> = emptyList()



    inner class LibraryItemViewHolder(
        private val binding: ItemLibraryRecylBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(album: AlbumModel) {
            binding.run {
                txtArtistName.text = album.artists?.joinToString(", ") { it.name ?: "Unknown Artist" }
                txtAlbumName.text = album.name

                Glide.with(binding.root.context)
                    .load(album.images?.firstOrNull()?.url)
                    .into(binding.imgTrack)
            }
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(album)
            }
        }



    }

    // Listener for item click
    private var onItemClickListener: ((AlbumModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (AlbumModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun submitList(newList: List<AlbumModel>) {
        albumList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LibraryItemViewHolder {
        val binding = ItemLibraryRecylBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LibraryItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LibraryItemViewHolder,
        position: Int
    ) {
        return holder.bind(albumList[position])
    }

    override fun getItemCount() = albumList.size


}