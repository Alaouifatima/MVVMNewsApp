package com.androiddevs.mvvmnewsapp.api

import com.androiddevs.mvvmnewsapp.models.CreatePost
import com.androiddevs.mvvmnewsapp.models.Data
import com.androiddevs.mvvmnewsapp.models.PostsResponse
import retrofit2.Response
import retrofit2.http.*

interface PostsAPI {
    @Headers("app-id: 6278425d1b71c8c987d61648")
    @GET("post")
    suspend fun getNewPosts(
        @Query("page")
        pageNumber: Int
    ): Response<PostsResponse>


    @Headers("app-id: 6278425d1b71c8c987d61648")
    @GET("tag/{id}/post")
    suspend fun searchByTag(@Path("id") id: String,
                            @Query("page")
                            pageNumber: Int
    ): Response<PostsResponse>

    //  delete post by id
    @Headers("app-id: 6278425d1b71c8c987d61648")
    @DELETE("post/{id}")
    suspend fun deletePostById(@Path("id") id: String): Response<PostsResponse>

    // create post
    @Headers("app-id: 6278425d1b71c8c987d61648")
    @POST("post/create")
    suspend fun createPost(@Body postToCreate: CreatePost): Response<Data>





}