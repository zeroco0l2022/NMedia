package ru.netology.nmedia.dao

import ru.netology.nmedia.data.Post

interface PostDao {
    fun getAll(): List<Post>
    fun openPost(id: Int)
    fun save(post: Post): Post
    fun shareById(id: Int)
    fun likeById(id: Int)
    fun removeById(id: Int)
}