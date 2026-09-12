package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AlarmNotificationScheduler {

  private const val TAG = "AlarmScheduler"

  fun scheduleAlarm(
    context: Context,
    reminderId: Long,
    title: String,
    message: String,
    triggerAtMillis: Long
  ): Boolean {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
      ?: return false

    val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
      action = ReminderAlarmReceiver.ACTION_REMINDER_ALARM
      putExtra(ReminderAlarmReceiver.EXTRA_TITLE, title)
      putExtra(ReminderAlarmReceiver.EXTRA_MESSAGE, message)
      putExtra(ReminderAlarmReceiver.EXTRA_REMINDER_ID, reminderId)
    }

    val pendingIntent = PendingIntent.getBroadcast(
      context,
      reminderId.toInt(),
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
          alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
          )
        } else {
          alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
          )
        }
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
          AlarmManager.RTC_WAKEUP,
          triggerAtMillis,
          pendingIntent
        )
      } else {
        alarmManager.setExact(
          AlarmManager.RTC_WAKEUP,
          triggerAtMillis,
          pendingIntent
        )
      }
      Log.d(TAG, "Scheduled alarm for reminder $reminderId at $triggerAtMillis: $title")
      return true
    } catch (e: SecurityException) {
      Log.e(TAG, "SecurityException scheduling alarm: ${e.message}, falling back to windowed alarm")
      try {
        alarmManager.set(
          AlarmManager.RTC_WAKEUP,
          triggerAtMillis,
          pendingIntent
        )
        return true
      } catch (ex: Exception) {
        Log.e(TAG, "Failed fallback alarm", ex)
        return false
      }
    } catch (e: Exception) {
      Log.e(TAG, "Failed to schedule alarm", e)
      return false
    }
  }

  fun cancelAlarm(context: Context, reminderId: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
    val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
      action = ReminderAlarmReceiver.ACTION_REMINDER_ALARM
    }
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      reminderId.toInt(),
      intent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
    Log.d(TAG, "Canceled alarm for reminder $reminderId")
  }

  fun triggerInstantTest(context: Context, title: String, message: String) {
    ReminderAlarmReceiver.showNotification(
      context = context,
      title = title,
      message = message,
      notificationId = 9999
    )
  }

  fun formatAlarmTime(millis: Long): String {
    val formatter = SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
  }
}
