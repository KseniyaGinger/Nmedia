package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.LongArg
import ru.netology.nmedia.viewmodel.PostViewModel


class PostFragment : Fragment() {
    companion object {
        var Bundle.longArg: Long by LongArg
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel by activityViewModels<PostViewModel>()
        val binding = FragmentPostBinding.inflate(layoutInflater, container, false)

        val id = requireArguments().longArg
        viewModel.data.observe(viewLifecycleOwner) {
            val post: Post? = it.find { it.id == id }
            if (post == null) {
                findNavController().navigateUp()
                return@observe
            }
        }
        return binding.root
    }

 fun fillOnePost(binding: CardPostBinding, onInteractionListener: OnInteractionListener, post: Post) {
     with(binding) {
         author.text = post.author
         content.text = post.content
         published.text = post.published

         share.text = formatCount(post.shared)
         view.text = formatCount(post.views)

         like.isChecked = post.likedByMe
         like.text = formatCount(post.likes)

         attach.isVisible = post.video != null

         root.setOnClickListener {}

         like.setOnClickListener {
             onInteractionListener.likeListener(post)
         }

         share.setOnClickListener {
             onInteractionListener.shareListener(post)
         }

         play.setOnClickListener {
             onInteractionListener.playVideo(post)
         }

         image.setOnClickListener {
             onInteractionListener.playVideo(post)
         }

         //здесь должен быть fragment post?
         cardPost.setOnClickListener {
             onInteractionListener.showPost(post)
         }

         menu.setOnClickListener {
             PopupMenu(it.context, it).apply {
                 inflate(R.menu.options_post)
                 setOnMenuItemClickListener { menuItem ->
                     when (menuItem.itemId) {
                         R.id.edit -> {
                             onInteractionListener.editListener(post)
                             true
                         }

                         R.id.remove -> {
                             onInteractionListener.removeListener(post)
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



