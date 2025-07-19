package com.example.spoti5.presentations.feature.artist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.ItemArtistRecylBinding
import com.example.spoti5.domain.model.artist.ArtistDetailModel

class SeveralArtistAdapter():
    RecyclerView.Adapter<SeveralArtistAdapter.SeveralArtistViewHolder>(){
        private var listArtist: List<ArtistDetailModel> = emptyList()

        inner class SeveralArtistViewHolder(
            private val binding: ItemArtistRecylBinding
        ): RecyclerView.ViewHolder(binding.root) {
            fun bind(artist: ArtistDetailModel) {

                binding.run {

                    txtArtistName.text = artist.name
                    val imgUrl = artist.images?.firstOrNull()?.url ?: ""

                    Glide.with(binding.root.context)
                        .load(imgUrl)
                        .into(binding.imgArtist)

                    binding.root.setOnClickListener {
                        onItemClickListener?.invoke(artist)
                    }
                }
            }
        }

    // Listener for item click
    private var onItemClickListener: ((ArtistDetailModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (ArtistDetailModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun submitList(newList: List<ArtistDetailModel>) {
        listArtist = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeveralArtistAdapter.SeveralArtistViewHolder {
        val binding = ItemArtistRecylBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SeveralArtistViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SeveralArtistAdapter.SeveralArtistViewHolder,
        position: Int
    ) {
        holder.bind(listArtist[position])
    }

    override fun getItemCount() = listArtist.size

}