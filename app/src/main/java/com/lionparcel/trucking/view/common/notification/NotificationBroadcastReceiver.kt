package com.lionparcel.trucking.view.common.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.RemoteMessage
import com.lionparcel.trucking.view.common.BundleConstants

class NotificationBroadcastReceiver(private val onNotificationReceived: (RemoteMessage) -> Unit) :
    BroadcastReceiver() {

    companion object {
        internal const val NOTIFICATION_BROADCAST_INTENT_FILTER_STRING =
            "com.lionparcel.trucking.REMOTE_MESSAGE_RECEIVED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        intent.getParcelableExtra<RemoteMessage>(BundleConstants.REMOTE_MESSAGE)?.let {
            onNotificationReceived(it)
        }
    }
}
