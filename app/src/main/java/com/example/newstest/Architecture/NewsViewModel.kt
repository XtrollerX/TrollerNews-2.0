package com.example.newstest.Architecture

import NewsRepository
import android.content.Context
import android.provider.Settings.Global
import androidx.lifecycle.*
import com.example.newstest.MainActivity
import com.example.newstest.retrofit.Article
import com.example.newstest.retrofit.NewsModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewsViewModel(var NewsRepositorys:NewsRepository) : ViewModel() {



    //get news from API, changed from MutableLiveData<List<NewsModel>>? for both newsLiveData variable and getNews
    suspend fun getNews(Country:String,Business: String?, Entertainment:String?, General:String?, Health:String?, Science:String?, Sports:String?,Tech:String?):MutableLiveData<Map<String, MutableList<Article>?>> {

        var maps =viewModelScope.async {

            var business = async{ NewsRepositorys.getNewsCall(Country, Business).value}
            var entertainment = async{NewsRepositorys.getNewsCall(Country, Entertainment).value}
            var general = async{NewsRepositorys.getNewsCall(Country, General).value}
            var health = async{NewsRepositorys.getNewsCall(Country, Health).value}
            var science = async{NewsRepositorys.getNewsCall(Country, Science).value}
            var sports = async{NewsRepositorys.getNewsCall(Country, Sports).value}
            var tech = async{NewsRepositorys.getNewsCall(Country, Tech).value}

            var newsMaps = mapOf(
                "Business" to business.await(),
                "Entertainment" to entertainment.await(),
                "General" to general.await(),
                "Health" to health.await(),
                "Science" to science.await(),
                "Sports" to sports.await(),
                "Tech" to tech.await()
            )
            newsMaps
        }
        MainActivity.isdone = true
            return MutableLiveData(maps.await())

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
