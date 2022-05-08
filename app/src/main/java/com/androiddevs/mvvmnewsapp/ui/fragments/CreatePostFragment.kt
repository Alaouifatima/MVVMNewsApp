package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.CreatePost
import com.androiddevs.mvvmnewsapp.ui.PostsActivity
import com.androiddevs.mvvmnewsapp.ui.PostsViewModel
import com.androiddevs.mvvmnewsapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_post.*


class CreatePostFragment : Fragment(R.layout.fragment_create_post) {


    lateinit var viewModel: PostsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as PostsActivity).viewModel

        title_create_post_page.text = "Ajouter un POST"

        button_create_post.setOnClickListener{
            var text = create_post_text.text.toString()
            var image = create_post_image.text.toString()
            var tag1 = create_post_tag_1.text.toString()
            var tag2 = create_post_tag_2.text.toString()
            var tag3 = create_post_tag_3.text.toString()
            var tags = mutableListOf<String>(tag1, tag2, tag3)
            var owner = "60d0fe4f5311236168a109cb"
            val postToCreate = CreatePost(text,image,0,tags,owner)

            viewModel.createPost(postToCreate)
            Snackbar.make(view, "Post créé avec succès", Snackbar.LENGTH_LONG).apply {
                show()
            }

            viewModel.newPostsResponse=null
            viewModel.newPostsPage=0

            //viewModel.getNewPosts()

            findNavController().navigate(
                R.id.action_createPostFragment_to_newPostsFragment
            )


        }

    }

}