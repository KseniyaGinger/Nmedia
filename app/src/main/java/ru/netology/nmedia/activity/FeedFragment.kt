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
import ru.netology.nmedia.activity.PostFragment.Companion.longArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel


class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater, container, false)
        println(resources.displayMetrics.heightPixels) // 1794
        println(resources.displayMetrics.widthPixels) // 1080
        println(resources.displayMetrics.densityDpi) // 420
        println(resources.displayMetrics.density) // 2.625

        binding.root.setOnClickListener {
        }

        val viewModel: PostViewModel by activityViewModels()

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun likeListener(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun removeListener(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun editListener(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply { textArg = post.content })
            }

            override fun playVideo(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intent)
            }

            override fun showPost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_postFragment
                )
                Bundle().apply { longArg = post.id }
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
        })
        binding.list.adapter = adapter

        binding.newPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        return binding.root
    }
}


/* viewModel.edited.observe(this) {post ->
     if (post.id != 0L) {
         binding.group.visibility = View.VISIBLE
         binding.content.setText(post.content)
         binding.content.requestFocus()
         binding.content.focusAndShowKeyboard()
         binding.contentLine.setText(post.content)
     }
 }


 binding.save.setOnClickListener {
     val text = binding.content.text.toString()
     if (text.isBlank()) {
         Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
         return@setOnClickListener
     }

     viewModel.changeContentAndSave(text)


     binding.content.setText("")
     binding.group.visibility = View.GONE
     binding.content.clearFocus()
     AndroidUtils.hideKeyboard(it)
 }

 binding.clear.setOnClickListener {
     binding.group.visibility = View.GONE
     binding.content.clearFocus()
     binding.content.text.clear()

     viewModel.cancelEdit()

     AndroidUtils.hideKeyboard(it)
 }
}
} */



