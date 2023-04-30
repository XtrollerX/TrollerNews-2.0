package com.example.newstest

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel


class application_class: Application() {

    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}