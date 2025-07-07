package com.example.spoti5.presentations.feature.Home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.ItemAlbumRecylBinding
import com.example.spoti5.domain.model.album.AlbumModel

class AlbumAdapter(
    private var listAlbum: List<AlbumModel> = emptyList()
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(
        private val binding: ItemAlbumRecylBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: AlbumModel) {
            binding.run {
                Glide.with(binding.root.context)
                    .load(album.images?.firstOrNull()?.url)
                    .into(imgAlbum)

                tvAlbumName.text = album.name

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
        listAlbum = newList
        notifyDataSetChanged() // hoặc dùng DiffUtil nếu muốn mượt hơn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumRecylBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(listAlbum[position])
    }

    override fun getItemCount(): Int = listAlbum.size
}
