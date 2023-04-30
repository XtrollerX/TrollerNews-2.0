package com.example.newstest.retrofit

data class NewsDataFromJson(
    val articles: MutableList<Article>,//
    val status: String,
    val totalResults: Int
)