package com.androiddevs.mvvmnewsapp.repository


import com.androiddevs.mvvmnewsapp.models.CreatePost
import com.androiddevs.mvvmnewsapp.models.Data
import com.androiddevs.mvvmnewsapp.ui.RetrofitInstance

class PostsRepository(

) {
    suspend fun getNewPosts(pageNumber : Int) =
        RetrofitInstance.api.getNewPosts(pageNumber)

    suspend fun searchPosts(id: String, pageNumber: Int) =
        RetrofitInstance.api.searchByTag(id, pageNumber)

    suspend fun createPost(postToCreate : CreatePost)=
        RetrofitInstance.api.createPost(postToCreate)

    suspend fun deletePostById(id: String)=
        RetrofitInstance.api.deletePostById(id)

}