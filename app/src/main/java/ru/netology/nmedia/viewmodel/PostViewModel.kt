package ru.netology.nmedia.viewmodel

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    published = "",
    likedByMe = false,
    likes = 0,
    shared = 0,
    views = 0,
    video = null
)

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun sharedById (id: Long) = repository.sharedById(id)
    fun removeById (id: Long) = repository.removeById(id)


    fun edit(post: Post) {
        edited.value = post
    }

    fun cancelEdit () {
        edited.value = empty
    }

    fun changeContentAndSave(content: String) {
            val text = content.trim()
            if (edited.value?.content == text) {
                return
            }
        edited.value.let {
            if (it != null) {
                repository.save(it.copy(content = text))
            }
        }
        edited.value = empty
            }

}



