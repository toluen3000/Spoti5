package com.example.spoti5.presentations.feature.play.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.ItemLibraryRecylBinding
import com.example.spoti5.domain.model.player.TrackItemModel

class TrackItemAdapter: RecyclerView.Adapter<TrackItemAdapter.TrackItemViewHolder>() {

    private var listItem : List<TrackItemModel> = emptyList()

    inner class TrackItemViewHolder(
        private val binding: ItemLibraryRecylBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(track: TrackItemModel) {

            if (position == 0){
                binding.run {

                    txtArtistName.text = track.artists?.joinToString(", ") { it.name ?: "Unknown Artist" }
                    txtAlbumName.text = "... ${track.name}"

                    txtAlbumName.setTextColor(Color.GREEN)
                }
            }else{
                binding.run {
                    txtArtistName.text = track.artists?.joinToString(", ") { it.name ?: "Unknown Artist" }
                    txtAlbumName.text = track.name
                }
            }



            val imgUrl = track.album?.images?.firstOrNull()?.url

            Glide.with(binding.root)
                .load(imgUrl)
                .into(binding.imgTrack)

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(track)
            }

        }

    }

    // Listener for item click
    private var onItemClickListener: ((TrackItemModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (TrackItemModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun submitList(newList: List<TrackItemModel>) {
        listItem = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackItemAdapter.TrackItemViewHolder {
        val binding = ItemLibraryRecylBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TrackItemAdapter.TrackItemViewHolder,
        position: Int
    ) {
        holder.bind(listItem[position])
    }

    override fun getItemCount() = listItem.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_NORMAL
    }


    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_NORMAL = 1
    }


}

