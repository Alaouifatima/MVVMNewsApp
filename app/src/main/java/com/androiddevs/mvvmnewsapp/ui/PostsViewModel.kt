package com.androiddevs.mvvmnewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.PostsApplication
import com.androiddevs.mvvmnewsapp.models.CreatePost
import com.androiddevs.mvvmnewsapp.models.Data
import com.androiddevs.mvvmnewsapp.models.PostResponse
import com.androiddevs.mvvmnewsapp.models.PostsResponse
import com.androiddevs.mvvmnewsapp.repository.PostsRepository
import com.androiddevs.mvvmnewsapp.util.Resource
import com.bumptech.glide.Glide.init
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PostsViewModel(
    app: Application,
    val postsRepository: PostsRepository
)  : AndroidViewModel(app){
    //
    val newPosts: MutableLiveData<Resource<PostsResponse>>  = MutableLiveData()
    var newPostsPage = 0
    var newPostsResponse: PostsResponse? =null

    val searchPosts: MutableLiveData<Resource<PostsResponse>> = MutableLiveData()
    var searchPostsPage = 0
    var searchPostsResponse: PostsResponse? =null

    fun getNewPosts() = viewModelScope.launch {
        safeNewPostsCall()
    }

    fun searchPosts(id : String) = viewModelScope.launch {
        safeSearchNewPostsCall(id)
    }

    private fun handleNewPostsResponse(response: Response<PostsResponse>): Resource<PostsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse->
                // incrementer le nombre de page newpostspage
                newPostsPage++
                if(newPostsResponse == null) {
                    newPostsResponse = resultResponse
                }else{
                    val oldPosts = newPostsResponse?.data
                    val newPosts = resultResponse.data
                    oldPosts?.addAll(newPosts)
                }

                return Resource.Success(newPostsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleSearchNewPostsResponse(response: Response<PostsResponse>): Resource<PostsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse->
                // incrementer le nombre de page newpostspage
                searchPostsPage++
                if(searchPostsResponse == null) {
                    searchPostsResponse = resultResponse
                }else{
                    val oldPosts = searchPostsResponse?.data
                    val newPosts = resultResponse.data
                    oldPosts?.addAll(newPosts)
                }

                return Resource.Success(searchPostsResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun deletePostById(id: String) = viewModelScope.launch {
        postsRepository.deletePostById(id)
    }

    fun createPost(postToCreate: CreatePost) = viewModelScope.launch {
        postsRepository.createPost(postToCreate)
    }



    private suspend fun safeSearchNewPostsCall(id: String) {
        searchPosts.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = postsRepository.searchPosts(id,searchPostsPage)
                searchPosts.postValue((handleSearchNewPostsResponse(response)))
            } else {
                searchPosts.postValue(Resource.Error("pas de connection internet"))
            }


        } catch (t: Throwable) {
            when(t){
                is IOException -> searchPosts.postValue(Resource.Error("Connection échouée"))
                else -> searchPosts.postValue(Resource.Error("Convertion Error"))
            }

        }
    }

    private suspend fun safeNewPostsCall() {
        newPosts.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = postsRepository.getNewPosts(newPostsPage)
                newPosts.postValue((handleNewPostsResponse(response)))
            } else {
                newPosts.postValue(Resource.Error("pas de connection internet"))
            }


        } catch (t: Throwable) {
            when(t){
                is IOException -> newPosts.postValue(Resource.Error("Connection échouée"))
                else -> newPosts.postValue(Resource.Error("Convertion Error"))
            }

        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<PostsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }



}