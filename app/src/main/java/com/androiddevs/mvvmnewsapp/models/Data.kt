package com.androiddevs.mvvmnewsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
data class Data(
    val id: String?,
    val image: String?,
    val likes: Int?,
    val owner: Owner?,
    val publishDate: String?,
    val tags: List<String>?,
    val text: String?,
) : Serializable