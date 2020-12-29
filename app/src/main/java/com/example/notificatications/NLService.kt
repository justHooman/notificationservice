package com.example.notificatications;

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NLService : NotificationListenerService() {

    private val TAG = this.javaClass.simpleName;

    private val TARGET_APP_PACKAGE = "com.mservice.momotransfer"

    override fun onListenerConnected() {
        super.onListenerConnected()
        // connected đến system notifications service,
        // check các notifications đang active
        Log.e("FIXME", "onListenerConnected")
        val found = activeNotifications.any { sbn -> isTargetAppNoti(sbn) }
        if (found) {
            startTargetApp()
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.e("FIXME", "onNotificationPosted")
        sbn?.let { if (isTargetAppNoti(sbn)) { startTargetApp() } }
    }

    private fun startTargetApp() {
        kotlin.runCatching {
            val intent = packageManager.getLaunchIntentForPackage(TARGET_APP_PACKAGE)
            intent?.let {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun isTargetAppNoti(sbn: StatusBarNotification): Boolean {
        val pkg = sbn.packageName
        return (pkg == TARGET_APP_PACKAGE).also {
            Log.e("FIXME", "check noti isTarget=$it pkg=$pkg")
        }
    }
}
