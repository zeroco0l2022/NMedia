package ru.netology.nmedia.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity

class FCMService : FirebaseMessagingService() {
    private val channelId = "remote"
    private val action = "action"
    private val content = "content"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
/*

*/
override fun onMessageReceived(message: RemoteMessage) {

    message.data[action]?.let {
        when (Action.valueOf(it)) {
            Action.LIKE -> handleLike(gson.fromJson(message.data[content], Like::class.java))
            Action.NEW_POST-> handleNewPost(gson.fromJson(message.data[content], NewPost::class.java))
            else->return
        }
    }
}


    private fun handleNewPost(post: NewPost) {
        val resultIntent = Intent(this, AppActivity::class.java)
        val resultPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }


        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_message,
                    post.postAuthor
                )
            )
            .setContentText(post.content)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(post.content))
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)

            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        NotificationManagerCompat.from(this).notify(1, notification)
    }

    private fun handleLike(like: Like) {
        val resultIntent = Intent(this, AppActivity::class.java)
        val resultPendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }


        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_likes,
                    like.userName,
                    like.postAuthor
                )
            )
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        NotificationManagerCompat.from(this).notify(1, notification)
    }

    override fun onNewToken(token: String) {
        println(token)
    }
}

enum class Action {
    LIKE,
    NEW_POST
}

data class Like(
    val userId: Int,
    val userName: String,
    val postId: Int,
    val postAuthor: String
)

data class NewPost(
    val postId: Int,
    val postAuthor: String,
    val content: String
)