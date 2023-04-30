package com.example.newstest

import NewsRepository
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.newstest.Adapter.FragmentAdapter
import com.example.newstest.Architecture.NewsViewModel
import com.example.newstest.Architecture.NewsViewModelProviderFactory
import com.example.newstest.Constants.BUSINESS
import com.example.newstest.Constants.ENTERTAINMENT
import com.example.newstest.Constants.GENERAL
import com.example.newstest.Constants.HEALTH
import com.example.newstest.Constants.HOME
import com.example.newstest.Constants.SCIENCE
import com.example.newstest.Constants.SPORTS
import com.example.newstest.Constants.TECHNOLOGY
import com.example.newstest.Constants.TOTAL_NEWS_TAB
import com.example.newstest.Database.RoomDatabases
import com.example.newstest.retrofit.Article
import org.json.JSONException
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {
    private val newsCategories = arrayOf(
        HOME, BUSINESS,
        ENTERTAINMENT, SCIENCE,
        SPORTS, TECHNOLOGY, HEALTH
    )
    private lateinit var FragmentContainer:FragmentContainerView
    lateinit var viewModel: NewsViewModel
//    private lateinit var tabLayout: TabLayout
//    private lateinit var viewPager: ViewPager2
    private lateinit var fragmentAdapter: FragmentAdapter

    private lateinit var ProgresBar: ProgressBar

    private var totalRequestCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Set Action Bar
        val Maintoolbar: Toolbar = findViewById(R.id.MenuToolBar)
        val SecondaryToolbar:Toolbar = findViewById(R.id.topAppBarthesecond)
        val MenuSaved:ImageButton = findViewById(R.id.MenuSavedButton)

        Maintoolbar.visibility = View.VISIBLE
        SecondaryToolbar.visibility = View.GONE






        MenuSaved.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_savedFragment)

        }

        ProgresBar = findViewById(R.id.progresBar)
        FragmentContainer = findViewById(R.id.nav_host_fragment)

        val newsRepository = NewsRepository(RoomDatabases(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]

        supportActionBar?.setDisplayShowTitleEnabled(false)


        if (!isNetworkAvailable(applicationContext)) {
            FragmentContainer.visibility = View.GONE
            ProgresBar.visibility = View.GONE
            val showError: TextView = findViewById(R.id.display_error)
            showError.setText("Internet not avaialable ")
            showError.visibility = View.VISIBLE
        }

        // Send request call for news data
        requestNews(GENERAL, generalNews,"us")
        requestNews(TECHNOLOGY,TechNews,"us")
        requestNews(HEALTH,healthNews,"us")
        requestNews(SPORTS, SportsNews,"us")
        requestNews(ENTERTAINMENT, EntertainmentNews,"us")
        requestNews(SCIENCE, ScienceNews,"us")
        requestNews(BUSINESS, BusinessNews,"us")
    }
//RequestNews function changed from newsData: MutableList<NewsModel>
    private fun requestNews(newsCategory: String, newsData: MutableList<Article>,country:String) {
        viewModel.getNews(category = newsCategory, Country = country)?.observe(this) {
            newsData.addAll(it)
            totalRequestCount += 1

//             If main fragment loaded then attach the fragment to viewPager
//            if (newsCategory == GENERAL) {
//                ProgresBar.visibility = View.GONE
//                setViewPager()
//            }

            if(!apiRequestError){
                if(totalRequestCount == 7){
                    ProgresBar.visibility = View.GONE
                    ProgresBar.visibility = View.GONE
                    setViewPager()
                }

            }else if(apiRequestError){
                ProgresBar.visibility = View.GONE
                FragmentContainer.visibility = View.GONE
                val showError: TextView = findViewById(R.id.display_error)
                showError.text = errorMessage
                showError.visibility = View.VISIBLE
            }




//            if(totalRequestCount == 7){
//                ProgresBar.visibility = View.GONE
//                setViewPager()
//            }else{
//                ProgresBar.visibility = View.GONE
//                FragmentContainer.visibility = View.GONE
//                val showError: TextView = findViewById(R.id.display_error)
//                showError.text = "Server error"
//                showError.visibility = View.VISIBLE
//
//            }
        }
    }

    private fun setViewPager() {
            if (apiRequestError) {
                ProgresBar.visibility = View.GONE
                FragmentContainer.visibility = View.GONE
                val showError: TextView = findViewById(R.id.display_error)
                showError.text = errorMessage
                showError.visibility = View.VISIBLE
            }
            else if(!apiRequestError) {
                FragmentContainer.visibility = View.VISIBLE
            }
    }



    // Check internet connection
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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



    companion object{
        //var generalNews changed from ArrayList<NewsModel>
        var ScienceNews: MutableList<Article> = mutableListOf()
        var EntertainmentNews: MutableList<Article> = mutableListOf()
        var SportsNews: MutableList<Article> = mutableListOf()
        var BusinessNews: MutableList<Article> = mutableListOf()
        var healthNews: MutableList<Article> = mutableListOf()
        var generalNews: MutableList<Article> = mutableListOf()
        var TechNews: MutableList<Article> = mutableListOf()
        var apiRequestError = false
        var errorMessage = "error"
        var SocketTimeout: JSONException? = null
    }
}