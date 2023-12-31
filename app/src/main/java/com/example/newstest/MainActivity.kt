package com.example.newstest

import NewsRepository
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
import com.example.newstest.ExtraPackage.ErrorHandling
import com.example.newstest.retrofit.Article
import com.example.newstest.retrofit.NewsDataFromJson
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import org.json.JSONException
import java.net.Socket
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {
  lateinit var errorDialog:ConstraintLayout


    private lateinit var FragmentContainer: FragmentContainerView
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val Maintoolbar: Toolbar = findViewById(R.id.MenuToolBar)
        val SecondaryToolbar: Toolbar = findViewById(R.id.topAppBarthesecond)
        val MenuSaved: ImageButton = findViewById(R.id.MenuSavedButton)
        errorDialog = findViewById(R.id.InternetError)

        Maintoolbar.visibility = View.VISIBLE
        SecondaryToolbar.visibility = View.GONE

        //Saved Menu
        MenuSaved.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_savedFragment)
        }

        FragmentContainer = findViewById(R.id.nav_host_fragment)

        val newsRepository = NewsRepository(RoomDatabases(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Send request call for news data
        NewsInitiation()

        //Check if internet is available
        if(isInternetAvailable(this) == false){
            FragmentContainer.visibility = View.GONE
            errorDialog.visibility = View.VISIBLE
            MenuSaved.visibility = View.GONE

        }
    }

    //Initiating Api Calls
    fun NewsInitiation() {
        lifecycleScope.launch(Dispatchers.Main) {
            Log.d("MainActivity", " Inititation ")

            viewModel.getNews("us", GENERAL, viewModel.GeneralNews)
            viewModel.getNews("us", BUSINESS, viewModel.BusinessNews)
            viewModel.getNews("us", ENTERTAINMENT, viewModel.EntertainmentNews)
            viewModel.getNews("us", HEALTH, viewModel.HealthNews)
            viewModel.getNews("us", SCIENCE, viewModel.ScienceNews)
            viewModel.getNews("us", SPORTS, viewModel.SportsNews)
            viewModel.getNews("us", TECHNOLOGY, viewModel.TechNews)
        }
    }

        private fun isInternetAvailable(context:Context): Boolean {
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


}