package com.mksolution.newsshortadmin

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class FirebaseMessaging : FirebaseMessagingService() {
    private val channelID = "class-update"
    private val channelName = "class-updates"

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val imageUrl = message.data["imageUrl"]
        val title = message.data["title"]
        //val body = message.data["body"]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val builder = NotificationCompat.Builder(applicationContext, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setColor(applicationContext.getColor(R.color.black))
            .setContentTitle(title)
            //.setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(false)
            .setLights(
                ContextCompat.getColor(applicationContext, R.color.black),
                5000,
                5000
            )

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(applicationContext)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        NotificationManagerCompat.from(applicationContext)
                            .notify(Random().nextInt(3000), builder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle if needed, e.g., placeholder while image loads
                    }
                })
        } else {
            // Randomly select a drawable image (img, img1, img2) if imageUrl is empty
            val drawableResources = listOf(R.drawable.img, R.drawable.img_1, R.drawable.img_2,R.drawable.img_3)
            val randomDrawable = drawableResources.random() // Select a random image

            Glide.with(applicationContext)
                .asBitmap()
                .load(randomDrawable)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        NotificationManagerCompat.from(applicationContext)
                            .notify(Random().nextInt(3000), builder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // Handle if needed, e.g., placeholder while image loads
                    }
                })
        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH,
        )
        notificationManager.createNotificationChannel(channel)
    }
}