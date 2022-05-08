package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.PostsAdapter
import com.androiddevs.mvvmnewsapp.ui.PostsActivity
import com.androiddevs.mvvmnewsapp.ui.PostsViewModel
import com.androiddevs.mvvmnewsapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_new_posts.*
import kotlinx.android.synthetic.main.fragment_search_posts.*
import kotlinx.android.synthetic.main.fragment_search_posts.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchPostsFragment : Fragment(R.layout.fragment_search_posts) {

    lateinit var viewModel: PostsViewModel

    lateinit var postsAdapter: PostsAdapter

    val TAG = "SearchPostsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as PostsActivity).viewModel
        setupRecyclerView()

        postsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("post", it)
            }
            findNavController().navigate(
                R.id.action_searchPostsFragment_to_postFragment,
                bundle
            )
        }
        postsAdapter.setOnItemLongClickListener {
            val bundle = Bundle().apply {
                putSerializable("post", it)
            }
            findNavController().navigate(
                R.id.action_searchPostsFragment_to_deletePostFragment,
                bundle
            )
        }

        viewModel.searchPostsResponse = null
        viewModel.searchPostsPage=0
        viewModel.searchPosts("0")

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                if(editable.toString().isNotEmpty()){
                    viewModel.searchPosts(editable.toString())
                }
                if(editable.toString().isEmpty()){
                    viewModel.searchPostsResponse = null
                    viewModel.searchPostsPage=0
                    viewModel.searchPosts("0")
                }
            }
        }

        viewModel.searchPosts.observe(viewLifecycleOwner, Observer {   response->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { postsResponse ->
                        postsAdapter.differ.submitList(postsResponse.data.toList())
                        val totalPages = postsResponse.total / 20 + 2
                        isLastPage = viewModel.searchPostsPage == totalPages
                        if(isLastPage) {
                            rvSearchPosts.setPadding(0,0,0,0)
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
                viewModel.searchPosts(etSearch.text.toString())
                isScrolling = false
            }

        }
    }

    private fun setupRecyclerView() {
        postsAdapter = PostsAdapter()
        rvSearchPosts.apply {
            adapter = postsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchPostsFragment.scrollListener)
        }
    }
}