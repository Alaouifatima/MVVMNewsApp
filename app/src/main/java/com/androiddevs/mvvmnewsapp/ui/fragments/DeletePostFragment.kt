package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.PostsAdapter
import com.androiddevs.mvvmnewsapp.ui.PostsActivity
import com.androiddevs.mvvmnewsapp.ui.PostsViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_delete_post.*
import kotlinx.android.synthetic.main.fragment_delete_post.delete_no
import kotlinx.android.synthetic.main.fragment_delete_post.delete_question
import kotlinx.android.synthetic.main.fragment_delete_post.delete_yes
import kotlinx.android.synthetic.main.fragment_delete_post.view.*
import kotlinx.android.synthetic.main.fragment_new_posts.*
import kotlinx.android.synthetic.main.fragment_post.*

class DeletePostFragment : Fragment(R.layout.fragment_delete_post) {

    lateinit var viewModel: PostsViewModel
    lateinit var postsAdapter: PostsAdapter
    val args: PostFragmentArgs by navArgs()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as PostsActivity).viewModel


        val post = args.post

        delete_page.apply {
            delete_question.text = "Voulez-vous supprimmer ce post?"
            delete_yes.text = "oui"
            delete_no.text="no"

            delete_yes.setOnClickListener {
                viewModel.deletePostById(post.id.toString())

                viewModel.newPostsResponse=null
                viewModel.newPostsPage=0

                findNavController().navigate(
                    R.id.action_deletePostFragment_to_newPostsFragment
                )

            }
            delete_no.setOnClickListener {
                findNavController().navigate(
                    R.id.action_deletePostFragment_to_newPostsFragment
                )
            }

        }

    }


}