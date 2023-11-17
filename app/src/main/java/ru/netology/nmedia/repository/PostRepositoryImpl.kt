package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {
    override fun getAll(): LiveData<List<Post>> = dao.getAll().map { posts ->
        posts.map(PostEntity::toDto)
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun sharedById(id: Long) {
        dao.sharedById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}