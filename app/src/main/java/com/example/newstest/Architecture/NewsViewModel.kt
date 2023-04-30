package com.example.newstest.Architecture

import NewsRepository
import android.content.Context
import androidx.lifecycle.*
import com.example.newstest.retrofit.Article
import com.example.newstest.retrofit.NewsModel
import kotlinx.coroutines.launch

class NewsViewModel(var NewsRepositorys:NewsRepository) : ViewModel() {

    private var newsLiveData: MutableLiveData<MutableList<Article>>? = null

    //get news from API, changed from MutableLiveData<List<NewsModel>>? for both newsLiveData variable and getNews
    fun getNews(Country:String,category: String?): MutableLiveData<MutableList<Article>>? {

        newsLiveData =  NewsRepositorys.getNewsCall(Country,category)

        return newsLiveData
    }

//    var newsData: LiveData<List<NewsModel>>? = null

    fun insertNews(news: Article) = viewModelScope.launch {
        NewsRepositorys.upsert(news)
    }

//
    fun deleteNews(news:Article) = viewModelScope.launch{
    NewsRepositorys.deleteArticle( news)}

//
    fun getNewsFromDB(): LiveData<List<Article>>? {
        var newsData = NewsRepositorys.getSavedNews()
        return newsData
    }
}

class NewsViewModelProviderFactory(
    val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}
