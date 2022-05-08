package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.PostsAdapter
import com.androiddevs.mvvmnewsapp.repository.PostsRepository
import kotlinx.android.synthetic.main.activity_posts.*
import kotlinx.android.synthetic.main.fragment_new_posts.*
import kotlinx.android.synthetic.main.item_post_preview.*
import kotlinx.android.synthetic.main.item_post_preview.view.*
import kotlinx.coroutines.Job

class PostsActivity : AppCompatActivity() {

    lateinit var viewModel: PostsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        val postsRepository = PostsRepository()
        val viewModelProviderFactory = PostsViewModelProviderFactory(application, postsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PostsViewModel::class.java)
        bottomNavigationView.setupWithNavController(postsNavHostFragment.findNavController())
    }

}
