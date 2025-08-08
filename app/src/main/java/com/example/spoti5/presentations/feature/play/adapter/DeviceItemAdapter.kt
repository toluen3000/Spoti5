package com.example.spoti5.presentations.feature.play.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spoti5.R
import com.example.spoti5.databinding.ItemLibraryRecylBinding
import com.example.spoti5.databinding.ItemSearchArtistBinding
import com.example.spoti5.domain.model.player.AvailableDevicesModel
import com.example.spoti5.domain.model.player.DeviceModel
import com.example.spoti5.domain.model.player.TrackItemModel

class DeviceItemAdapter: RecyclerView.Adapter<DeviceItemAdapter.DeviceItemViewHolder>() {

    private var listItem : List<DeviceModel> = emptyList()

    inner class DeviceItemViewHolder(
        private val binding: ItemSearchArtistBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(device: DeviceModel) {

            binding.root.setBackgroundColor(Color.TRANSPARENT)

           if (device.isActive == true){
               binding.apply {
                   txtArtistName.text = device.name
                   txtArtistName.setTextColor(Color.GREEN)


                   if (device.type == "Smartphone"){
                       imgArtist.setImageResource(R.drawable.ic_smartphone_active)
                   }else{
                       imgArtist.setImageResource(R.drawable.ic_computer_active)
                   }

               }
           }else{
               binding.apply {
                   txtArtistName.text = device.name

                   if (device.type == "Smartphone"){
                       imgArtist.setImageResource(R.drawable.ic_smartphone_notactive)
                   }else{
                       imgArtist.setImageResource(R.drawable.ic_computer_notactive)
                   }
               }
           }


            binding.root.setOnClickListener {
                onItemClickListener?.invoke(device)
            }

        }

    }

    // Listener for item click
    private var onItemClickListener: ((DeviceModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (DeviceModel) -> Unit) {
        this.onItemClickListener = listener
    }
    fun submitList(newList: List<DeviceModel>) {
        listItem = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DeviceItemAdapter.DeviceItemViewHolder {
        val binding = ItemSearchArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DeviceItemAdapter.DeviceItemViewHolder,
        position: Int
    ) {
        holder.bind(listItem[position])
    }

    override fun getItemCount() = listItem.size



}

