package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.Post

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
    fun view()
}