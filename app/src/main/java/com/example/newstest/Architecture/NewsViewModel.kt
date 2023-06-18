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



    //get news from API, changed from MutableLiveData<List<NewsModel>>? for both newsLiveData variable and getNews

    fun getNews(Country:String, Category:String?, NewsList:MutableLiveData<ErrorHandling<NewsDataFromJson?>?> ){
        Log.d("ViewModel"," getNews " )
        viewModelScope.launch(Dispatchers.IO) {
            ErrorGetNewsCall(Country, Category,NewsList )
        }

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


    fun getNewsCall(response: Response<NewsDataFromJson>): ErrorHandling<NewsDataFromJson?> {
        Log.d("ViewModel"," getNewsCall " )
        if(response.isSuccessful){
            val body = response.body()

            return ErrorHandling.Success(body)
        }else{
            return ErrorHandling.Error(response.message())
        }
    }


    suspend fun ErrorGetNewsCall(country: String, Category: String?, NewsList:MutableLiveData<ErrorHandling<NewsDataFromJson?>?>){
        Log.d("ViewModel"," ErrorGetNewsCall " )
        try{

            val responseFromApi = NewsApiCall.api.getNews(country,
                Category,
                "3af34c4172814610afe0a3e59dafafe4")
            NewsList.postValue(getNewsCall(responseFromApi))



        }catch (t:Throwable){
            when(t){
                is IOException -> NewsList.postValue(ErrorHandling.Error("Network Failure"))
                else -> NewsList.postValue(ErrorHandling.Error("Conversion Error"))


            }
        }
    }
    //SETTLE THE NETWORKCONNECTION BELOW
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getApplication<application_class>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // For below 29 api
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }

//    private fun hasInternetConnection(): Boolean {
//        val connectivityManager = getApplication<application_class>().getSystemService(
//            Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val activeNetwork = connectivityManager.activeNetwork ?: return false
//            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
//            return when {
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                else -> false
//            }
//        } else {
//            connectivityManager.activeNetworkInfo?.run {
//                return when(type) {
//                    ConnectivityManager.TYPE_WIFI -> true
//                    ConnectivityManager.TYPE_MOBILE -> true
//                    ConnectivityManager.TYPE_ETHERNET -> true
//                    else -> false
//                }
//            }
//        }
//        return false
//    }
}





class NewsViewModelProviderFactory(
    val app: Application,
    val newsRepository: NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository,app) as T
    }
}
