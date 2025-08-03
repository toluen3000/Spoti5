package com.example.spoti5.presentations.feature.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.ItemSearchArtistBinding
import com.example.spoti5.domain.model.artist.ArtistDetailModel

class ArtistItemAdapter(
): RecyclerView.Adapter<ArtistItemAdapter.ArtistItemViewHolder>() {

    private var artistList: List<ArtistDetailModel> = emptyList()



    inner class ArtistItemViewHolder(
        private val binding: ItemSearchArtistBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(artist: ArtistDetailModel) {

            binding.apply {
                txtArtistName.text = artist.name

                Glide.with(binding.root.context)
                    .load(artist.images?.firstOrNull()?.url)
                    .into(binding.imgArtist)

            }


            binding.root.setOnClickListener {
                onItemClickListener?.invoke(artist)
            }
        }



    }

    // Listener for item click
    private var onItemClickListener: ((ArtistDetailModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (ArtistDetailModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun submitList(newList: List<ArtistDetailModel>) {
        artistList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistItemViewHolder {
        val binding = ItemSearchArtistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArtistItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ArtistItemViewHolder,
        position: Int
    ) {
        holder.bind(artistList[position])
    }

    override fun getItemCount() = artistList.size


}