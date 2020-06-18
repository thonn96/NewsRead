package com.example.newsread.Interface

import com.example.newsread.Model.News
import com.example.newsread.Model.WebSite
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {
    @get:GET("v2/sources?apiKey=e725e8955a864f4391a74edbfbfddbe8")
    val sources:Call<WebSite>

    @GET
    fun getNewsFromSource(@Url url:String):Call<News>

}