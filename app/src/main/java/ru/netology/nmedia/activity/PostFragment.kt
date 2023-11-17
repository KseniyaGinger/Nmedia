package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostViewHolder
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
    ): View {
        val viewModel by activityViewModels<PostViewModel>()
        val binding = FragmentPostBinding.inflate(layoutInflater, container, false)

        val id = requireArguments().longArg
        viewModel.data.observe(viewLifecycleOwner) {
            val post: Post? = it.find { it.id == id }
            if (post == null) {
                findNavController().navigateUp()
                return@observe
            }
            PostViewHolder(
                binding.onePost,
                object : OnInteractionListener {
                    override fun likeListener(post: Post) {
                        viewModel.likeById(post.id)
                    }

                    override fun shareListener(post: Post) {
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, post.content)
                            type = "text/*"
                        }

                       viewModel.sharedById(post.id)

                        val chooser = Intent.createChooser(intent, null)
                        startActivity(chooser)
                    }

                    override fun removeListener(post: Post) {
                        viewModel.removeById(post.id)
                    }

                    override fun editListener(post: Post) {
                        viewModel.edit(post)
                        findNavController().navigate(
                            R.id.action_postFragment_to_newPostFragment,
                            Bundle().apply { textArg = post.content })
                    }

                    override fun playVideo(post: Post) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                        startActivity(intent)
                    }

                    override fun showPost(post: Post) {
                        findNavController().navigate(
                            //R.id.action_feedFragment_to_postFragment,
                            R.id.action_postFragment_to_PostFragment,
                            Bundle().apply { longArg = post.id })
                    }
                }
            ).bind(post)
        }
        return binding.root
    }
}



