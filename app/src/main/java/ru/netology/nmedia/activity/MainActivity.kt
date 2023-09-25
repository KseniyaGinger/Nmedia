package ru.netology.nmedia.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostVewModel

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

        binding.like.setOnClickListener {
            binding.like.setImageResource(R.drawable.baseline_favorite_24)
            println("like")
        }

        binding.root.setOnClickListener{
            println("root")
        }

        val viewModel by viewModels<PostVewModel>()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published
                likes.text = formatCount(post.likes)
                shared.text = formatCount(post.shared)
                views.text = formatCount(post.views)
                like.setImageResource(if (post.likedByMe) (R.drawable.baseline_favorite_24) else R.drawable.baseline_favorite_border_24)

               /* if (post.likedByMe) {
                    like.setImageResource(R.drawable.baseline_favorite_24)
                } */
            }
        }

        binding.like.setOnClickListener {
            viewModel.like()
            //likes.text = formatCount(post.likes)
        }

       binding.share.setOnClickListener{
            viewModel.share()
            //shared.text = formatCount(post.shared)
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