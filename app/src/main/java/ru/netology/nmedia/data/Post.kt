package ru.netology.nmedia.data

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int = 999,
    var shared: Int = 9999,
    val views: Int = 1000000,
    var likedByMe: Boolean = false
)
