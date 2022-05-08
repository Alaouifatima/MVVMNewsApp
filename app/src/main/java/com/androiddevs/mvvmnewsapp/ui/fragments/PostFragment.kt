package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.Owner
import com.androiddevs.mvvmnewsapp.ui.PostsActivity
import com.androiddevs.mvvmnewsapp.ui.PostsViewModel
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.item_post_preview.view.*

class PostFragment : Fragment(R.layout.fragment_post) {

    lateinit var viewModel: PostsViewModel
    val args: PostFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as PostsActivity).viewModel

        val post = args.post
        post_page.apply {

            Glide.with(this).load(post.owner?.picture).into(userImage_page)
            Glide.with(this).load(post.image).into(postImage_page)
            userName_page.text = post.owner?.title +". "+post.owner?.firstName+" "+post.owner?.lastName
            timestamp_page.text = post.publishDate
            postText_page.text = post.text
            tag1_page.text = post.tags?.get(0)
            tag2_page.text = post.tags?.get(1)
            tag3_page.text = post.tags?.get(2)
        }
    }


}