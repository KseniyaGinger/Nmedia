package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.AndroidUtils.focusAndShowKeyboard
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
        println(resources.displayMetrics.heightPixels) // 1794
        println(resources.displayMetrics.widthPixels) // 1080
        println(resources.displayMetrics.densityDpi) // 420
        println(resources.displayMetrics.density) // 2.625

        binding.root.setOnClickListener {
        }

        val viewModel: PostViewModel by viewModels()

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun LikeListener(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun ShareListener(post: Post) {
                viewModel.sharedById(post.id)
            }
            override fun RemoveListener(post: Post) {
                viewModel.removeById(post.id)
            }
            override fun EditListener(post: Post) {
                viewModel.edit(post)
            }
        })
        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.currentList.size < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }


        viewModel.edited.observe(this) {post ->
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

            viewModel.changeContent(text)
            viewModel.save()

            binding.content.setText("")
            binding.group.visibility = View.GONE
            binding.content.clearFocus()
            AndroidUtils.hideKeyboard(it)
        }

        binding.clear.setOnClickListener {
            binding.group.visibility = View.GONE
            binding.content.clearFocus()
            binding.content.text.clear()
            AndroidUtils.hideKeyboard(it)
        }
    }
}



