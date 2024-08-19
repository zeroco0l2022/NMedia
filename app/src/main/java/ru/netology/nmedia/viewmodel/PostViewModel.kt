package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFilesImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFilesImpl(application)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    val postId = MutableLiveData(0)

    fun openPost(post: Post) {
        repository.openPost(post.id)
        postId.value = post.id
    }

    fun closePost() {
        postId.value = 0
    }

    fun back() {
        postId.value = 0
        edited.value = empty

    }

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun likeById(id: Int) = repository.likeById(id)
    fun shareById(id: Int) = repository.shareById(id)
    fun removeById(id: Int) = repository.removeById(id)
    fun playVideo(id: Int) = repository.playVideo(id)
}