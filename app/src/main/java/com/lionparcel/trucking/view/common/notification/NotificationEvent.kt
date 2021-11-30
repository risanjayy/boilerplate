package com.lionparcel.trucking.view.common.notification

import com.google.firebase.messaging.RemoteMessage

enum class NotificationEvent(val value: String) {
    POD("com.lionparcel.services.consumer.POD");

    fun getEventName() = value.removePrefix("com.lionparcel.services.consumer.")

    companion object {

        fun getEventByRemovePrefix(remoteMessage: RemoteMessage): NotificationEvent? {
            return values().find { it.getEventName() == remoteMessage.data[NotificationConstants.ARG_EVENT] }
        }
    }
}
