package com.example.newstest.retrofit

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.newstest.application_class
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private const val BASE_URL = "https://newsapi.org/v2/"

    private fun httpclient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(10,TimeUnit.MILLISECONDS).readTimeout(10,TimeUnit.MILLISECONDS)

        builder.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)



            builder.addInterceptor(
                ChuckerInterceptor.Builder(application_class.context)
                    .collector(ChuckerCollector(application_class.context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
        })
        return builder.build()
    }

    fun getInstance(): Retrofit {

        return Retrofit.Builder()
            .client(httpclient())
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    object NewsApiCall {
        val api by lazy {
            RetrofitHelper.getInstance().create(NewsApi::class.java)
        }
    }

}