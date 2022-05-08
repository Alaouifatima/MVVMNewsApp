package com.androiddevs.mvvmnewsapp.models

import com.androiddevs.mvvmnewsapp.models.Data

data class PostsResponse(
    val `data`: MutableList<Data>,
    val limit: Int,
    val page: Int,
    val total: Int
)