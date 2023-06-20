package com.example.newstest.retrofit


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun getNews(@Query("country") country : String, @Query("category") category : String?, @Query("apiKey") key : String) : Response<NewsDataFromJson>

}