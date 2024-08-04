package ru.netology.nmedia.data

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val shared: Int = 0,
    val views: Int = 0,
    val likedByMe: Boolean = false
)
