package com.example.spoti5.presentations.feature.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.databinding.ItemArtistRecylBinding
import com.example.spoti5.databinding.ItemLibraryRecylBinding
import com.example.spoti5.databinding.ItemSearchArtistBinding
import com.example.spoti5.domain.model.search.AlbumModel
import com.example.spoti5.domain.model.search.ArtistModel
import com.example.spoti5.domain.model.search.TrackModel

class SearchAdapter (
    private val onTrackClick: (TrackModel) -> Unit,
    private val onArtistClick: (ArtistModel) -> Unit,
    private val onAlbumClick: (AlbumModel) -> Unit
) : ListAdapter<SearchItem, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val TYPE_TRACK_HEADER = 0
        const val TYPE_TRACK = 1
        const val TYPE_ARTIST_HEADER = 2
        const val TYPE_ARTIST = 3
        const val TYPE_ALBUM_HEADER = 4
        const val TYPE_ALBUM = 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchItem.TrackHeader -> TYPE_TRACK_HEADER
            is SearchItem.TrackItem -> TYPE_TRACK
            is SearchItem.ArtistHeader -> TYPE_ARTIST_HEADER
            is SearchItem.ArtistItem -> TYPE_ARTIST
            is SearchItem.AlbumHeader -> TYPE_ALBUM_HEADER
            is SearchItem.AlbumItem -> TYPE_ALBUM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_TRACK_HEADER -> HeaderViewHolder(TextView(parent.context).apply { text = "Tracks" })
            TYPE_ARTIST_HEADER -> HeaderViewHolder(TextView(parent.context).apply { text = "Artists" })
            TYPE_ALBUM_HEADER -> HeaderViewHolder(TextView(parent.context).apply { text = "Albums" })

            TYPE_TRACK -> TrackViewHolder(ItemLibraryRecylBinding.inflate(inflater, parent, false), onTrackClick)
            TYPE_ARTIST -> ArtistViewHolder(ItemSearchArtistBinding.inflate(inflater, parent, false), onArtistClick)
            TYPE_ALBUM -> AlbumViewHolder(ItemLibraryRecylBinding.inflate(inflater, parent, false), onAlbumClick)

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SearchItem.TrackItem -> (holder as TrackViewHolder).bind(item.track)
            is SearchItem.ArtistItem -> (holder as ArtistViewHolder).bind(item.artist)
            is SearchItem.AlbumItem -> (holder as AlbumViewHolder).bind(item.album)
            SearchItem.AlbumHeader -> {
                (holder as HeaderViewHolder).itemView.findViewById<TextView>(android.R.id.text1).text = "Albums"
            }
            SearchItem.ArtistHeader -> {
                (holder as HeaderViewHolder).itemView.findViewById<TextView>(android.R.id.text1).text = "Artists"
            }
            SearchItem.TrackHeader -> {
                (holder as HeaderViewHolder).itemView.findViewById<TextView>(android.R.id.text1).text = "Tracks"
            }
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class TrackViewHolder(private val binding: ItemLibraryRecylBinding,
        private val onTrackClick: (TrackModel) -> Unit
        ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(track: TrackModel) {
            binding.txtAlbumName.text = track.name
            binding.txtArtistName.text = track.artists.joinToString(", ") { it.name }
            val img = track.album?.images?.firstOrNull()?.url
            Glide.with(binding.root.context)
                .load(img)
                .into(binding.imgTrack)

            binding.root.setOnClickListener {
                onTrackClick(track)
            }
        }
    }

    class ArtistViewHolder(private val binding: ItemSearchArtistBinding,
        private val onArtistClick: (ArtistModel) -> Unit

    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: ArtistModel) {
            binding.txtArtistName.text = artist.name
            val imgUrl = artist.image?.url
            Glide.with(binding.root.context)
                .load(imgUrl)
                .into(binding.imgArtist)

            binding.root.setOnClickListener {
                onArtistClick(artist)
            }
        }
    }

    class AlbumViewHolder(private val binding: ItemLibraryRecylBinding,
        private val onAlbumClick: (AlbumModel) -> Unit
        ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(album: AlbumModel) {
            binding.txtAlbumName.text = album.name
            binding.txtArtistName.text = album.artists.joinToString(", ") { it.name ?: "Unknown Artist" }
            val imgUrl = album.images.firstOrNull()?.url
            Glide.with(binding.root.context)
                .load(imgUrl)
                .into(binding.imgTrack)

            binding.root.setOnClickListener {
                onAlbumClick(album)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchItem>() {
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return when {
                oldItem is SearchItem.TrackItem && newItem is SearchItem.TrackItem ->
                    oldItem.track.id == newItem.track.id
                oldItem is SearchItem.ArtistItem && newItem is SearchItem.ArtistItem ->
                    oldItem.artist.id == newItem.artist.id
                oldItem is SearchItem.AlbumItem && newItem is SearchItem.AlbumItem ->
                    oldItem.album.id == newItem.album.id
                else -> oldItem::class == newItem::class
            }
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem == newItem
        }
    }
}
