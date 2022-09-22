package com.example.youtube_motionlayout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtube_motionlayout.R
import com.example.youtube_motionlayout.model.VideoModel

class VideoAdapter(val callback : (String, String) -> Unit) : ListAdapter<VideoModel, VideoAdapter.ViewHolder>(diffUtil){

    inner class ViewHolder(val view : View) : RecyclerView.ViewHolder(view) {

        fun bind(item : VideoModel) {
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)
            val subTitleTextView = view.findViewById<TextView>(R.id.subTitleTextView)
            val thumbnailImageView  = view.findViewById<ImageView>(R.id.thumnailImageView)

            titleTextView.text = item.title
            subTitleTextView.text = item.subtitle

            Glide.with(thumbnailImageView.context)
                .load(item.thumb)
                .into(thumbnailImageView)

            view.setOnClickListener {
                callback(item.sources, item.title)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_video, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList.get(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<VideoModel>() {
            override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                return oldItem == newItem
            }

        }
    }

}




































