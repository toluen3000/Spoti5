package com.example.spoti5.presentations.feature.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.TrackItemRecylBinding
import com.example.spoti5.domain.model.album.TrackItemModel
import com.example.spoti5.domain.model.track.TrackModel

class TrackItemAdapter: RecyclerView.Adapter<TrackItemAdapter.TrackItemViewHolder>() {

    private var listTrackItem: List<TrackModel> = emptyList()

    inner class TrackItemViewHolder(
         val binding: TrackItemRecylBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindTrack(track: TrackModel) {
            binding.run {
                txtTrackName.text = track.name
                txtArtistName.text = track.artists?.joinToString(", ") { it.name ?: "Unknown Artist" }

                // Assuming the image URL is available in the track model
                Glide.with(binding.root.context)
                    .load(track.album?.images?.firstOrNull()?.url)
                    .into(binding.imgTrack)
            }

            // Set click listener for the item
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(track)
            }
        }
    }

    // Listener for item click
    private var onItemClickListener: ((TrackModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (TrackModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun submitList(newList: List<TrackModel>) {
        listTrackItem = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackItemViewHolder {
        val binding = TrackItemRecylBinding.inflate(
            parent.context.getSystemService(LayoutInflater::class.java),
            parent,
            false
        )
        return TrackItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TrackItemViewHolder,
        position: Int
    ) {

        holder.bindTrack(listTrackItem[position])
    }

    override fun getItemCount() = listTrackItem.size


}