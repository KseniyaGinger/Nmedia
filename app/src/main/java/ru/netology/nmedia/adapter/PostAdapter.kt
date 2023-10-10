package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

typealias LikeListener = (Post) -> Unit
typealias ShareListener = (Post) -> Unit

class PostAdapter(private val listener: LikeListener, private val listener1: ShareListener) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, listener, listener1)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(private val binding: CardPostBinding, private val listener: LikeListener, private val listener1: ShareListener) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            likes.text = formatCount(post.likes)
            shared.text = formatCount(post.shared)
            views.text = formatCount(post.views)

            like.setImageResource(if (post.likedByMe) (R.drawable.baseline_favorite_24) else R.drawable.baseline_favorite_border_24)
            like.setOnClickListener {
                listener(post)
            }
            share.setOnClickListener {
                listener1(post)
            }
        }
    }

    fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10000 -> "${count / 1000}K"
            count < 1000000 -> {
                val decimal = count % 1000 / 100
                "${count / 1000}.$decimal" + "K"
            }

            else -> "${count / 1000000}M"
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem

}