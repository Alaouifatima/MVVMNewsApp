package com.androiddevs.mvvmnewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androiddevs.mvvmnewsapp.repository.PostsRepository

class PostsViewModelProviderFactory(
    val app: Application,
    val postsRepository: PostsRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostsViewModel(app, postsRepository) as T
    }
}