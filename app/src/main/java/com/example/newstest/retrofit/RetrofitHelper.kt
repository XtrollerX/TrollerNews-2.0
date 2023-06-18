package com.example.newstest.retrofit

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.newstest.application_class
import okhttp3.OkHttpClient

import okhttp3.internal.platform.android.AndroidLogHandler.setLevel
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class RetrofitHelper {
    companion object {
        val BASE_URL = "https://newsapi.org/v2/"

        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        val httpclient by lazy {
            val builder1 = OkHttpClient.Builder().addInterceptor(logging).build()

            val builder = OkHttpClient.Builder()
//                .connectTimeout(10,TimeUnit.MILLISECONDS). readTimeout(10, TimeUnit.MILLISECONDS)


            val interceptor = builder.addInterceptor(HttpLoggingInterceptor().apply {
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


            Retrofit.Builder()
                .client(interceptor.build())
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }


    }
}

object NewsApiCall {
        val api by lazy {
            RetrofitHelper.httpclient.create(NewsApi::class.java)
        }
}
