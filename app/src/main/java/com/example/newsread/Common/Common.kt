package com.example.newsread.Common

import com.example.newsread.Interface.NewsService
import com.example.newsread.Remote.RetrofitClient
import java.lang.StringBuilder

object Common {
    val BASE_URL="https://newsapi.org/"
    val API_KEY = "e725e8955a864f4391a74edbfbfddbe8"

    val newsService:NewsService
    get()=RetrofitClient.getClient(BASE_URL).create(NewsService::class.java)

    fun getNewsAPI(source:String):String{
        val apiUrl = StringBuilder("https://newsapi.org/v2/top-headlines?sources=")
            .append(source)
            .append("&apiKey=")
            .append(API_KEY)
            .toString()
        return  apiUrl

    }

}