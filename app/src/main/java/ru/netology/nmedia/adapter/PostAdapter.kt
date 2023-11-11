package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

interface OnInteractionListener {
    fun LikeListener (post: Post)
    fun ShareListener (post: Post)
    fun RemoveListener (post: Post)
    fun EditListener (post: Post)
    fun PlayVideo (post: Post)
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published

            share.text = formatCount(post.shared)
            view.text = formatCount(post.views)

            like.isChecked = post.likedByMe
            like.text = formatCount(post.likes)

            attach.isVisible = post.video != null

            like.setOnClickListener {
               onInteractionListener.LikeListener(post)
            }

            share.setOnClickListener {
                onInteractionListener.ShareListener(post)
            }

            play.setOnClickListener{
                onInteractionListener.PlayVideo(post)
            }

            image.setOnClickListener{
                onInteractionListener.PlayVideo(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { menuItem ->
                        when(menuItem.itemId) {
                            R.id.edit -> {
                                onInteractionListener.EditListener(post)
                                true
                            }
                            R.id.remove -> {
                                onInteractionListener.RemoveListener(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
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