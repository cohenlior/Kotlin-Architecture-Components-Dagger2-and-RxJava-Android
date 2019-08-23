package com.example.android.mvvm_news_article.injection

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.android.mvvm_news_article.model.database.AppDatabase
import com.example.android.mvvm_news_article.ui.NewsListViewModel

class ViewModelFactory(private val activity: AppCompatActivity,
                       private val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsListViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java, "articles").build()
            @Suppress("UNCHECKED_CAST")
            return NewsListViewModel(db.articleDao(), application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}