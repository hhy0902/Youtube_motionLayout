package com.example.youtube_motionlayout

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtube_motionlayout.adapter.VideoAdapter
import com.example.youtube_motionlayout.databinding.FragmentPlayerBinding
import com.example.youtube_motionlayout.dto.VideoDto
import com.example.youtube_motionlayout.service.VideoService
import com.google.android.exoplayer2.DefaultControlDispatcher
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private lateinit var videoAdapter : VideoAdapter
    private var binding : FragmentPlayerBinding? = null
    private var player : SimpleExoPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)

        binding = fragmentPlayerBinding

        initMotionLayoutEvent(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)
        initPlayer(fragmentPlayerBinding)

        getVideoList()

    }

    private fun initMotionLayoutEvent(fragmentPlayerBinding : FragmentPlayerBinding) {
        fragmentPlayerBinding.playerMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
            }
            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
                binding?.let {
                    (activity as MainActivity).also { mainActivity ->
                        mainActivity.findViewById<MotionLayout>(R.id.mainMotionLayout).progress = abs(progress)
                    }
                }
            }
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            }
            override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {
            }

        })

    }

    private fun initPlayer(fragmentPlayerBinding: FragmentPlayerBinding) {
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }

        fragmentPlayerBinding.playerView.player = player
    }

    private fun initRecyclerView(fragmentPlayerBinding : FragmentPlayerBinding) {

        videoAdapter = VideoAdapter(callback = { url, title ->
            play(url, title)
        })

        fragmentPlayerBinding.fragmentRecyclerView.apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also {
            it.listVideios().enqueue(object : Callback<VideoDto> {
                override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                    if(response.isSuccessful.not()) {
                        Log.d("mainActivity","response fail")
                        return
                    }

                    response.body()?.let { VideoDto ->
                        Log.d("mainActivity",VideoDto.toString())

                        videoAdapter.submitList(VideoDto.videos)
                    }

                }

                override fun onFailure(call: Call<VideoDto>, t: Throwable) {
                    // 예외처리
                }

            })
        }
    }

    fun play(url : String, title : String) {
        context?.let {
            val dataSourceFactory = DefaultDataSourceFactory(it)
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(url)))
            player?.setMediaSource(mediaSource)
            player?.prepare()
            player?.play()
        }

        binding?.let {
            it.playerMotionLayout.transitionToEnd()
            it.bottomTitleTextView.text = title
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
















































