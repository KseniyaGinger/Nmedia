package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

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
        }


        val post = Post (
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            published = "21 мая в 18:36",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            likedByMe = true,
            likes = 1000,
            shared = 1200,
            views = 5000000
        )

        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            likes.text = post.likes.toString()
            shared.text = post.shared.toString()
            likes.text = formatCount(post.likes)
            shared.text = formatCount(post.shared)
            views.text = formatCount(post.views)

            if (post.likedByMe) {
                like.setImageResource(R.drawable.baseline_favorite_24)
            }

            like.setOnClickListener {
                post.likedByMe = !post.likedByMe
                post.likes += if (post.likedByMe) 1 else -1
                like.setImageResource(if (post.likedByMe) (R.drawable.baseline_favorite_24) else R.drawable.baseline_favorite_border_24)
                likes.text = post.likes.toString()
                likes.text = formatCount(post.likes)
            }

            share.setOnClickListener{
                post.shared += 1
                shared.text = post.shared.toString()
                shared.text = formatCount(post.shared)
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