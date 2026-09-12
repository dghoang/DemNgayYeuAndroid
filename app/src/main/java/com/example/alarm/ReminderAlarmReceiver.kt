package com.example.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

class ReminderAlarmReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    val title = intent.getStringExtra(EXTRA_TITLE) ?: "Nhắc nhở tình yêu ❤️"
    val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "Đã đến thời gian kỷ niệm đặc biệt của hai bạn!"
    val reminderId = intent.getLongExtra(EXTRA_REMINDER_ID, System.currentTimeMillis())

    showNotification(context, title, message, reminderId.toInt())
  }

  companion object {
    const val ACTION_REMINDER_ALARM = "com.example.inlove.ACTION_REMINDER_ALARM"
    const val CHANNEL_ID = "love_reminders_channel"
    const val CHANNEL_NAME = "InLove Reminders & Anniversaries"
    const val EXTRA_TITLE = "extra_reminder_title"
    const val EXTRA_MESSAGE = "extra_reminder_message"
    const val EXTRA_REMINDER_ID = "extra_reminder_id"

    fun showNotification(context: Context, title: String, message: String, notificationId: Int = 1001) {
      val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

      // Create Notification Channel for Android O+
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
          CHANNEL_ID,
          CHANNEL_NAME,
          NotificationManager.IMPORTANCE_HIGH
        ).apply {
          description = "Romantic notifications and alarms for important anniversaries and couple dates"
          enableLights(true)
          lightColor = Color.rgb(255, 64, 129) // Hot pink light
          enableVibration(true)
          vibrationPattern = longArrayOf(0, 300, 150, 300) // Romantic double heartbeat rhythm
        }
        notificationManager.createNotificationChannel(channel)
      }

      val launchIntent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra("from_notification", true)
        putExtra("notification_id", notificationId)
      }

      val pendingIntent = PendingIntent.getActivity(
        context,
        notificationId,
        launchIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )

      val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

      val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(message)
        .setStyle(NotificationCompat.BigTextStyle().bigText(message))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setVibrate(longArrayOf(0, 300, 150, 300))
        .setColor(0xFFFF4081.toInt())
        .setContentIntent(pendingIntent)
        .build()

      notificationManager.notify(notificationId, notification)
    }
  }
}
