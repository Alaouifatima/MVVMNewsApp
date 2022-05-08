package com.androiddevs.mvvmnewsapp.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupMenu
import androidx.appcompat.view.menu.MenuView
import androidx.core.view.isInvisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.Data
import com.androiddevs.mvvmnewsapp.models.PostsResponse
import com.androiddevs.mvvmnewsapp.ui.PostsActivity
import com.androiddevs.mvvmnewsapp.util.Resource
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_posts.view.*
import kotlinx.android.synthetic.main.item_post_preview.*
import kotlinx.android.synthetic.main.item_post_preview.view.*

class PostsAdapter: RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<Data>(){
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }

    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_post_preview,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(post.owner?.picture).into(userImage)
            Glide.with(this).load(post.image).into(postImage)
            timestamp.text = post.publishDate
            postText.text = post.text
            tag1.text = post.tags?.get(0)
            tag2.text = post.tags?.get(1)
            tag3.text = post.tags?.get(2)
            userName.text = post.owner?.title +". "+post.owner?.firstName+" "+post.owner?.lastName

            setOnClickListener{
                onItemClickListener?.let{it(post)}
            }

            setOnLongClickListener {
                onItemLongClickListener?.let{it(post)}
                true
            }


        }
    }

    //##########################""""

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Data) -> Unit)? = null

    fun setOnItemClickListener(listener: (Data) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((Data) -> Unit)? = null

    fun setOnItemLongClickListener(listener: (Data) -> Unit) {
        onItemLongClickListener = listener
    }
}