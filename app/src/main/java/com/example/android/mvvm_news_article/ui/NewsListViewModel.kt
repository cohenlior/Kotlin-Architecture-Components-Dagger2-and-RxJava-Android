package com.example.android.mvvm_news_article.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.mvvm_news_article.BaseViewModel
import com.example.android.mvvm_news_article.model.Article
import com.example.android.mvvm_news_article.model.ArticleDao
import com.example.android.mvvm_news_article.network.ArticleApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsListViewModel(
    private val articleDao: ArticleDao, application: Application
) : BaseViewModel(application) {
    @Inject
    lateinit var articleApi: ArticleApi

    private lateinit var subscription: Disposable

    private val _isLoaderVisible = MutableLiveData<Boolean>()
    val isLoaderVisible: LiveData<Boolean>
        get() = _isLoaderVisible

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>>
        get() = _articles

    private val _errorMassage = MutableLiveData<Boolean>()
    val errorMassage: LiveData<Boolean>
        get() = _errorMassage

    init {
        loadArticles()
    }

    private fun loadArticles() {
        subscription = Observable.fromCallable { articleDao.all }
            .concatMap { dbArticleList ->
                if (dbArticleList.isEmpty())
                    articleApi.getArticles().concatMap { apiArticleList ->
                        articleDao.insertAll(*apiArticleList.articles.toTypedArray())
                        Observable.just(apiArticleList.articles)
                    }
                else
                    Observable.just(dbArticleList)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onFetchArticleListStart() }
            .doOnTerminate { onFetchPostListFinish() }
            .subscribe(
                { result -> onFetchArticleListSuccess(result) },
                { errorMassage -> onFetchArticleListError(errorMassage) }
            )
    }

    private fun onFetchArticleListStart() {
        _isLoaderVisible.value = true
    }

    private fun onFetchPostListFinish() {
        _isLoaderVisible.value = false
    }

    private fun onFetchArticleListSuccess(result: List<Article>) {
        _articles.value = result
    }

    private fun onFetchArticleListError(errorMassage: Throwable) {
        _errorMassage.value = true
        Log.e("ErrorMassage", errorMassage.message)
    }

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}