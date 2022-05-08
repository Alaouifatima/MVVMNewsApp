package com.androiddevs.mvvmnewsapp.models

import android.text.Editable


data class CreatePost(
    var text: String,
    var image: String,
    var likes: Int,
    var tags: List<String>,
    var owner: String
)
