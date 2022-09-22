package com.example.youtube_motionlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.youtube_motionlayout.adapter.VideoAdapter
import com.example.youtube_motionlayout.dto.VideoDto
import com.example.youtube_motionlayout.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var videoAdapter : VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()

        videoAdapter = VideoAdapter(callback = { url, title ->
            supportFragmentManager.fragments.find { it is PlayerFragment }?.let {
                (it as PlayerFragment).play(url, title)
            }
        })
        findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        getVideoList()

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


}







































































