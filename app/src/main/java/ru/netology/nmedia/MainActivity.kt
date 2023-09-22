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
            likes = 90,
            shared = 20
        )

        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            likes.text = post.likes.toString()
            shared.text = post.shared.toString()

            if (post.likedByMe) {
                like.setImageResource(R.drawable.baseline_favorite_24)
            }

            like.setOnClickListener {
                post.likedByMe = !post.likedByMe
                post.likes += if (post.likedByMe) 1 else -1
                like.setImageResource(if (post.likedByMe) (R.drawable.baseline_favorite_24) else R.drawable.baseline_favorite_border_24)
                likes.text = post.likes.toString()
            }

            share.setOnClickListener{
                post.shared += 1
                shared.text = post.shared.toString()
            }

        }

    }

}