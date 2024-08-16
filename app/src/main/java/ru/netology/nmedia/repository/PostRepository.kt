package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun save(post: Post)
    fun removeById(id: Int)
    fun playVideo(id: Int)
}