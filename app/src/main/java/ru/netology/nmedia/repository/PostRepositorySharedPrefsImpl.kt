package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.data.Post

class PostRepositorySharedPrefsImpl(
    context: Context

) : PostRepository {
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private var nextId = 1
    private var posts = emptyList<Post>()
        set(value) {
            field = value
            sync()
        }

    private val data = MutableLiveData(posts)

    init {
        prefs.getString(KEY, null)?.let {
            posts = PostRepositorySharedPrefsImpl.gson.fromJson(
                it, typeToken
            )
            nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
            data.value = posts
        }
    }


    override fun getAll(): LiveData<List<Post>> = data
    override fun openPost(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(views = it.views+1)
        }
        data.value = posts
    }

    override fun likeById(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Int) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shared = it.shared + 1)
        }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts
            return
        }

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
        data.value = posts
    }


    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun playVideo(id: Int) {
        TODO()
    }

    private fun sync() {
        prefs.edit().apply {
            putString(KEY, PostRepositorySharedPrefsImpl.gson.toJson(posts))
            apply()
        }


    }

    companion object {
        private const val KEY = "posts"
        private val gson = Gson()
        private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    }

}