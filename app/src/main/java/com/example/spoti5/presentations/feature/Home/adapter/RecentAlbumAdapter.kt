package com.example.spoti5.presentations.feature.Home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.ItemRecentPlayedBinding
import com.example.spoti5.domain.model.player.AlbumModel

class RecentAlbumAdapter: RecyclerView.Adapter<RecentAlbumAdapter.RecentViewHolder>() {

    private var listItem: List<AlbumModel> = emptyList()

    inner class RecentViewHolder(
        private val binding: ItemRecentPlayedBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(album: AlbumModel) {
            binding.run {
                // Assuming AlbumModel has properties like name and imageUrl
                tvTitle.text = album.name

                 Glide.with(binding.root.context)
                     .load(album.images?.firstOrNull()?.url)
                     .into(imgAlbumArt)

                // Set click listener if needed
                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(album)
                }
            }
        }
    }

    private var onItemClickListener: ((AlbumModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (AlbumModel) -> Unit) {
        this.onItemClickListener = listener
    }

    fun submitList(newList: List<AlbumModel>) {
        // Assuming you want to update the list and notify the adapter
        // This is a placeholder, you might want to use DiffUtil for better performance
        listItem = newList
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentAlbumAdapter.RecentViewHolder {
        val binding = ItemRecentPlayedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecentAlbumAdapter.RecentViewHolder,
        position: Int
    ) {
        holder.bind(listItem[position])
    }

    override fun getItemCount() = listItem.size


}