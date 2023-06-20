package com.example.newstest.Architecture

import NewsRepository
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.*
import com.bumptech.glide.load.engine.Resource
import com.example.newstest.ExtraPackage.ErrorHandling
import com.example.newstest.MainActivity
import com.example.newstest.R
import com.example.newstest.UI.EntertainmentFragment
import com.example.newstest.application_class
import com.example.newstest.retrofit.*
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class NewsViewModel(var NewsRepositorys:NewsRepository, app:Application) : AndroidViewModel(app) {

    val GeneralNews:MutableLiveData<ErrorHandling<NewsDataFromJson?>?> = MutableLiveData()

    val BusinessNews: MutableLiveData<ErrorHandling<NewsDataFromJson?>?> = MutableLiveData()

    val EntertainmentNews : MutableLiveData<ErrorHandling<NewsDataFromJson?>?> = MutableLiveData()

    val HealthNews : MutableLiveData<ErrorHandling<NewsDataFromJson?>?> = MutableLiveData()

    val ScienceNews: MutableLiveData<ErrorHandling<NewsDataFromJson?>?> = MutableLiveData()

    val SportsNews :  MutableLiveData<ErrorHandling<NewsDataFromJson?>?> = MutableLiveData()

    val TechNews : MutableLiveData<ErrorHandling<NewsDataFromJson?>?> = MutableLiveData()

    var isDataBaseEmpty: MutableLiveData<Boolean> = MutableLiveData()


    //get news from API, changed from MutableLiveData<List<NewsModel>>? for both newsLiveData variable and getNews
    fun getNews(Country:String, Category:String, NewsList:MutableLiveData<ErrorHandling<NewsDataFromJson?>?> ){
        Log.d("ViewModel"," getNews " )
        viewModelScope.launch(Dispatchers.IO) {
            ErrorGetNewsCall(Country, Category,NewsList )
        }
    }
    //Check if database is empty
    fun IsDataEmpty(){
        viewModelScope.launch(Dispatchers.IO) {
            isDataBaseEmpty.postValue(NewsRepositorys.empty())
        }
    }
    //Inserting news to database
    fun insertNews(news: Article) = viewModelScope.launch {
        NewsRepositorys.upsert(news)
    }
    //Deleting news from database
    fun deleteNews(news:Article) = viewModelScope.launch{
    NewsRepositorys.deleteArticle( news)}

   //Get news fromd atabase
   fun getNewsFromDB(): LiveData<List<Article>>? {
        var newsData = NewsRepositorys.getSavedNews()
        return newsData
    }

    //If the response has no ErrorException, provide a success response or a error response such as too many requests, etc
    fun getNewsCall(response: Response<NewsDataFromJson>): ErrorHandling<NewsDataFromJson?> {
        Log.d("ViewModel"," getNewsCall " )
        if(response.isSuccessful){
            val body = response.body()

            return ErrorHandling.Success(body)
        }else{
            return ErrorHandling.Error(response.message())
        }
    }

    //If there is ErrorException handle it, for example SockettimeoutException
    suspend fun ErrorGetNewsCall(country: String, Category: String, NewsList:MutableLiveData<ErrorHandling<NewsDataFromJson?>?>){
        Log.d("ViewModel"," ErrorGetNewsCall " )
        try{

            val responseFromApi = NewsRepositorys.getNews(country,
                Category,
                //This is a free API key, please use your own from NewsApi.ORG
                "3af34c4172814610afe0a3e59dafafe4")
            NewsList.postValue(getNewsCall(responseFromApi))

        }catch (t:Throwable){
            when(t){
                is IOException -> NewsList.postValue(ErrorHandling.Error("There is a network failure, please refresh the page!"))
                else -> NewsList.postValue(ErrorHandling.Error("Conversion Error"))


            }
        }
    }
}





class NewsViewModelProviderFactory(
    val app: Application,
    val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository,app) as T
    }
}
