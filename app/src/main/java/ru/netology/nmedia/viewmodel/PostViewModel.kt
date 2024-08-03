package ru.netology.nmedia.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel(){
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
    fun view() = repository.view()
    fun displayNumber(num: Int): String {
        var numDelta: Float = (num / 100).toFloat() / 10
        var coef: String = "K"
        if (numDelta >= 1000) {
            numDelta = (numDelta / 100).toInt().toFloat() / 10
            coef = "M"
        }

        return if (num < 1000) {
            num.toString()
        } else if (numDelta >= 10 || numDelta == numDelta.toInt().toFloat()) {
            String.format("%d%s", numDelta.toInt(), coef)
        } else {
            String.format("%.${1}f%s", numDelta, coef)
        }

    }
}