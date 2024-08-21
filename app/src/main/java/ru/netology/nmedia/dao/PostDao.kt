package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query(
        """
        UPDATE PostEntity SET
               views = views + 1
              WHERE id = :id;
    """
    )
    fun openPost(id: Int)

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Int, content: String)

    fun save(post: PostEntity) =
        if (post.id == 0) insert(post) else updateContentById(post.id, post.content)

    @Query(
        """
        UPDATE PostEntity SET
        shared = shared + 1
        WHERE id = :id
    """
    )
    fun shareById(id: Int)

    @Query(
        """
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
    """
    )
    fun likeById(id: Int)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Int)
}