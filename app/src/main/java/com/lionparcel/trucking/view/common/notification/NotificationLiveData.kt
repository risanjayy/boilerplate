package com.lionparcel.trucking.view.common.notification

import android.content.Context
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import com.google.firebase.messaging.RemoteMessage
import com.lionparcel.trucking.view.common.notification.NotificationBroadcastReceiver.Companion.NOTIFICATION_BROADCAST_INTENT_FILTER_STRING

class NotificationLiveData(private val context: Context) : LiveData<RemoteMessage>() {

    private val filter = IntentFilter(NOTIFICATION_BROADCAST_INTENT_FILTER_STRING)

    private val notificationBroadcastReceiver = NotificationBroadcastReceiver { value = it }

    override fun onActive() {
        context.registerReceiver(notificationBroadcastReceiver, filter)
    }

    override fun onInactive() {
        context.unregisterReceiver(notificationBroadcastReceiver)
    }
}
