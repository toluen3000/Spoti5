package com.example.spoti5.presentations.feature.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.TrackItemRecylBinding
import com.example.spoti5.domain.model.album.TrackItemModel

class DetailsAlbumAdapter(
    private val imgUrl: String?
): RecyclerView.Adapter<DetailsAlbumAdapter.DetailsAlbumViewHolder>() {
    private var listTrackItem: List<TrackItemModel> = emptyList()

    inner class DetailsAlbumViewHolder(
        private val binding: TrackItemRecylBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(track: TrackItemModel) {

            binding.run {
                txtTrackName.text = track.name
                txtArtistName.text = track.artists?.joinToString(", ") { it.name ?: "Unknown Artist" }

                val albumUri =

                Glide.with(binding.root.context)
                    .load(imgUrl)
                    .into(binding.imgTrack)

                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(track)
                }
            }
        }

    }

    // Listener for item click
    private var onItemClickListener: ((TrackItemModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (TrackItemModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun submitList(newList: List<TrackItemModel>) {
        listTrackItem = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsAlbumAdapter.DetailsAlbumViewHolder {
       val binding = TrackItemRecylBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailsAlbumViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DetailsAlbumAdapter.DetailsAlbumViewHolder,
        position: Int
    ) {
        holder.bind(listTrackItem[position])
    }

    override fun getItemCount(): Int  = listTrackItem.size


}