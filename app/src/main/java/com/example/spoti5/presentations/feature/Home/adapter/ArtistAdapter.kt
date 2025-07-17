package com.example.spoti5.presentations.feature.Home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.ItemArtistRecylBinding
import com.example.spoti5.domain.model.album.AlbumModel
import com.example.spoti5.domain.model.album.ArtistModel

class ArtistAdapter: RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    var listArtist: List<ArtistModel> = emptyList()

    inner class ArtistViewHolder(val binding: ItemArtistRecylBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(artistModel: ArtistModel) {
            binding.run {
                Glide.with(binding.root.context)
                    .load(artistModel.imageUrl)
                    .into(imgArtist)

                txtArtistName.text  = artistModel.name

                binding.root.setOnClickListener {
                    onItemClickListener?.invoke(artistModel)
                }
            }
        }

    }

    private var onItemClickListener: ((ArtistModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (ArtistModel) -> Unit) {
        this.onItemClickListener = listener
    }

    fun submitList(newList: List<ArtistModel>) {
        listArtist = newList
        notifyDataSetChanged() // hoặc dùng DiffUtil nếu muốn mượt hơn
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistViewHolder {
        val binding = ItemArtistRecylBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ArtistViewHolder,
        position: Int
    ) {
        holder.bind(listArtist[position])
    }

    override fun getItemCount() = listArtist.size


}