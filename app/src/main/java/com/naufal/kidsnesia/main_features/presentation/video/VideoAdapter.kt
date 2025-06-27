package com.naufal.kidsnesia.main_features.presentation.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.ItemVideoBinding
import com.naufal.kidsnesia.main_features.data.source.remote.response.DataItem
import com.naufal.kidsnesia.util.UrlHelper

class VideoAdapter(
    private var videos: List<DataItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private fun truncateText(text: String?, maxLength: Int): String {
        if (text.isNullOrEmpty()) return ""
        return if (text.length <= maxLength) text else text.take(maxLength) + "..."
    }

    fun updateData(newVideos: List<DataItem>) {
        this.videos = newVideos
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(private val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(video: DataItem) {
            binding.tvTitle.text = truncateText(video.judulVideo, 30)
            binding.tvDescription.text = truncateText(video.deskripsiVideo, 100)
            Glide.with(binding.root.context)
                .load(video.thumbnail)
                .into(binding.ivThumbnail)

            binding.root.setOnClickListener {
                video.idVideo?.toString()?.let { onItemClick(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }
}




