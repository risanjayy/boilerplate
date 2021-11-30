package com.lionparcel.trucking.view.common.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.RemoteMessage
import com.lionparcel.trucking.R

class NotificationHelper {

    fun showNotification(context: Context, remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        val notificationTitle = notification?.title
            ?: data[NotificationConstants.ARG_TITLE]
            ?: data[NotificationConstants.ATTRIBUTE_NOTIFICATION_TITLE]
        val intentExtras = Bundle()
        remoteMessage.data.mapKeys { intentExtras.putString(it.key, it.value) }
        val intent = Intent(
            notification?.clickAction
        ).apply { putExtras(intentExtras).putExtra(
            NotificationConstants.ARG_TITLE,
            notificationTitle
        ) }
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val requestCodeId = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(
            context, requestCodeId, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        showNotification(
            notificationTitle,
            notification?.body ?: data[NotificationConstants.ARG_BODY],
            context,
            remoteMessage.hashCode(),
            pendingIntent
        )
    }

    private fun showNotification(
        title: String?,
        body: String?,
        context: Context,
        notifyId: Int,
        pendingIntent: PendingIntent
    ) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(
            context,
            context.getString(R.string.default_notification_channel_id)
        ).setContentTitle(title)
            .setColor(ContextCompat.getColor(context, R.color.interpack6))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        initNotificationChannel(context)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notifyId, notificationBuilder.build())
    }

    companion object {

        fun initNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.non_translatable_notification_channel_name)
                val channel = NotificationChannel(
                    context.getString(R.string.default_notification_channel_id),
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val notificationManager = context.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager.createNotificationChannel(channel)
            }
        }

        const val KEY_SEND_BIRD = "sendbird"
        const val KEY_NAME = "name"
        const val KEY_MESSAGE = "message"
        const val KEY_CHANNEL = "channel"
    }
}
