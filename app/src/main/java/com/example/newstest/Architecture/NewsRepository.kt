import android.content.Context
import android.telecom.CallScreeningService
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.newstest.MainActivity
import com.example.newstest.retrofit.*
import kotlinx.coroutines.CoroutineScope

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import com.example.newstest.retrofit.Article




import androidx.lifecycle.LiveData
import com.example.newstest.Database.RoomDatabases

import kotlinx.coroutines.Dispatchers.IO

import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class NewsRepository(val db:RoomDatabases ) {

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)



    suspend fun empty() = db.getArticleDao().isEmpty()

    suspend fun nukeTable() = db.getArticleDao().nukeTable()




}


