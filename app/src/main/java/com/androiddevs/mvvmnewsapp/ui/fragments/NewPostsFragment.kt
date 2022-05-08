package com.androiddevs.mvvmnewsapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.PostsAdapter
import com.androiddevs.mvvmnewsapp.models.Data
import com.androiddevs.mvvmnewsapp.ui.PostsActivity
import com.androiddevs.mvvmnewsapp.ui.PostsViewModel
import com.androiddevs.mvvmnewsapp.util.Resource
import com.bumptech.glide.Glide.init
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_new_posts.*
import kotlinx.android.synthetic.main.fragment_new_posts.paginationProgressBar
import kotlinx.android.synthetic.main.fragment_search_posts.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class NewPostsFragment : Fragment(R.layout.fragment_new_posts) {

    lateinit var viewModel: PostsViewModel
    lateinit var postsAdapter: PostsAdapter

    val TAG = "NewPostsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as PostsActivity).viewModel

        setupRecyclerView()


        postsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("post", it)
            }
            findNavController().navigate(
                R.id.action_newPostsFragment_to_postFragment,
                bundle
            )
        }
        postsAdapter.setOnItemLongClickListener {
            val bundle = Bundle().apply {
                putSerializable("post", it)
            }
            findNavController().navigate(
                R.id.action_newPostsFragment_to_deletePostFragment,
                bundle
            )

        }

        viewModel.newPostsResponse=null
        viewModel.newPostsPage=0
        viewModel.getNewPosts()

        viewModel.newPosts.observe(viewLifecycleOwner, Observer {   response->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { postsResponse ->
                        postsAdapter.differ.submitList(postsResponse.data.toList())
                        val totalPages = postsResponse.total / 20 + 2
                        isLastPage = viewModel.newPostsPage == totalPages
                        if(isLastPage) {
                            rvNewPosts.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {  message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getNewPosts()
                isScrolling = false
            }

        }
    }

    private fun setupRecyclerView() {
        postsAdapter = PostsAdapter()
        rvNewPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewPostsFragment.scrollListener)
        }

    }

}