package com.example.youtube_motionlayout.service

import com.example.youtube_motionlayout.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("v3/2dfef786-a435-41d2-8ab8-ab39297c1648")
    fun listVideios(

    ) : Call<VideoDto>

}













































