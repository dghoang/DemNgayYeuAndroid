package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Data model representing a calculated Love Anniversary Milestone.
 */
data class LoveMilestoneInfo(
  val id: Long,
  val title: String,
  val milestoneDays: Int,
  val targetDateMillis: Long,
  val formattedDate: String,
  val daysRemaining: Int,
  val isAnnual: Boolean = false,
  val emoji: String = "💖",
  val description: String = ""
)

/**
 * Local Notification Scheduler that alerts users about upcoming love anniversary
 * milestones based on the couple start date stored in Firestore.
 */
object LoveAnniversaryMilestoneScheduler {

  private const val TAG = "MilestoneScheduler"

  // Base ID offsets for notification alarms to avoid collision
  private const val ADVANCE_3_DAYS_OFFSET = 300000L
  private const val ADVANCE_1_DAY_OFFSET = 400000L
  private const val DAY_OF_OFFSET = 500000L

  /**
   * Standard key day-count milestones to monitor
   */
  val STANDARD_DAY_MILESTONES = listOf(
    30 to "🌱 1 Tháng Yêu Nhau (30 Ngày)",
    50 to "🌸 50 Ngày Gắn Kết",
    100 to "💎 Bách Nhật Yêu (100 Ngày)",
    150 to "🎀 150 Ngày Đồng Điệu",
    200 to "🌹 200 Ngày Nồng Nàn",
    300 to "✨ 300 Ngày Chung Đôi",
    365 to "🎂 Tròn 1 Năm Yêu (365 Ngày)",
    500 to "🌿 500 Ngày Tri Kỷ",
    730 to "💍 Tròn 2 Năm Yêu (730 Ngày)",
    1000 to "👑 1000 Ngày Trọn Vẹn",
    1500 to "🏆 1500 Ngày Vững Bền",
    2000 to "🌟 2000 Ngày Hạnh Phúc"
  )

  /**
   * Parses start date into millis, supporting timestamp, date string ("dd/MM/yyyy", "yyyy-MM-dd", etc.)
   */
  fun parseStartDateMillis(startDateMillis: Long, startDateText: String): Long {
    if (startDateMillis > 0) return startDateMillis

    val trimmed = startDateText.trim()
    if (trimmed.isEmpty()) return System.currentTimeMillis()

    val formats = listOf("dd/MM/yyyy", "dd-MM-yyyy", "yyyy-MM-dd", "d/M/yyyy")
    for (fmt in formats) {
      try {
        val sdf = SimpleDateFormat(fmt, Locale.getDefault()).apply { isLenient = false }
        val date = sdf.parse(trimmed)
        if (date != null) return date.time
      } catch (_: Exception) {}
    }
    return System.currentTimeMillis()
  }

  /**
   * Computes all upcoming milestones from the Firestore start date for the next 2 years.
   */
  fun getUpcomingMilestones(
    startDateMillis: Long,
    startDateText: String
  ): List<LoveMilestoneInfo> {
    val startMillis = parseStartDateMillis(startDateMillis, startDateText)
    val now = System.currentTimeMillis()
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val results = mutableListOf<LoveMilestoneInfo>()

    // 1. Calculate Day-Count Milestones
    for ((days, name) in STANDARD_DAY_MILESTONES) {
      val targetMillis = startMillis + (days.toLong() * 24L * 60L * 60L * 1000L)
      if (targetMillis > now) {
        val daysRemaining = TimeUnit.MILLISECONDS.toDays(targetMillis - now).toInt().coerceAtLeast(0)
        results.add(
          LoveMilestoneInfo(
            id = days.toLong(),
            title = name,
            milestoneDays = days,
            targetDateMillis = targetMillis,
            formattedDate = sdf.format(Date(targetMillis)),
            daysRemaining = daysRemaining,
            isAnnual = false,
            emoji = if (days >= 365) "👑" else if (days >= 100) "💎" else "💖",
            description = "Cột mốc $days ngày bên nhau đong đầy kỷ niệm"
          )
        )
      }
    }

    // 2. Calculate Annual Anniversaries for the next 5 years
    val startCal = Calendar.getInstance().apply { timeInMillis = startMillis }
    val startDay = startCal.get(Calendar.DAY_OF_MONTH)
    val startMonth = startCal.get(Calendar.MONTH) // 0-11
    val startYear = startCal.get(Calendar.YEAR)

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    for (year in currentYear..(currentYear + 4)) {
      val targetCal = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, startMonth)
        set(Calendar.DAY_OF_MONTH, startDay)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
      }

      val targetMillis = targetCal.timeInMillis
      val yearsInLove = year - startYear
      if (yearsInLove > 0 && targetMillis > now) {
        val daysRemaining = TimeUnit.MILLISECONDS.toDays(targetMillis - now).toInt().coerceAtLeast(0)
        results.add(
          LoveMilestoneInfo(
            id = 10000L + yearsInLove,
            title = "🎉 Kỷ Niệm $yearsInLove Năm Yêu Nhau",
            milestoneDays = yearsInLove * 365,
            targetDateMillis = targetMillis,
            formattedDate = sdf.format(Date(targetMillis)),
            daysRemaining = daysRemaining,
            isAnnual = true,
            emoji = "💍",
            description = "Tròn $yearsInLove năm tình yêu bền vững và hạnh phúc"
          )
        )
      }
    }

    return results.sortedBy { it.targetDateMillis }
  }

  /**
   * Schedules alarms for upcoming milestones based on the Firestore start date.
   * Sets up 3 alerts per milestone:
   * 1. Advance: 3 days before at 09:00 AM
   * 2. Advance: 1 day before at 09:00 AM
   * 3. Day of: On the milestone day at 08:30 AM
   */
  fun scheduleMilestonesFromFirestore(
    context: Context,
    startDateMillis: Long,
    startDateText: String,
    partnerName: String = "người ấy"
  ): Int {
    val milestones = getUpcomingMilestones(startDateMillis, startDateText)
    var scheduledCount = 0

    val now = System.currentTimeMillis()

    for (ms in milestones) {
      val targetCal = Calendar.getInstance().apply {
        timeInMillis = ms.targetDateMillis
      }

      // 1. Day-of Alert at 08:30 AM
      val dayOfCal = Calendar.getInstance().apply {
        set(Calendar.YEAR, targetCal.get(Calendar.YEAR))
        set(Calendar.MONTH, targetCal.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, targetCal.get(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, 8)
        set(Calendar.MINUTE, 30)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
      }

      if (dayOfCal.timeInMillis > now) {
        val scheduled = AlarmNotificationScheduler.scheduleAlarm(
          context = context,
          reminderId = DAY_OF_OFFSET + ms.id,
          title = "🎉 Hôm Nay: ${ms.title}! ❤️",
          message = "Hôm nay là ${ms.title} (${ms.formattedDate}) của bạn và $partnerName! Chúc hai bạn có một ngày kỷ niệm thật hạnh phúc và ngập tràn yêu thương! 🥂✨",
          triggerAtMillis = dayOfCal.timeInMillis,
          action = ReminderAlarmReceiver.ACTION_ANNIVERSARY_ALARM,
          channelId = ReminderAlarmReceiver.CHANNEL_ANNIVERSARIES_ID,
          targetTab = "home"
        )
        if (scheduled) scheduledCount++
      }

      // 2. Advance: 1 Day Before at 09:00 AM
      val oneDayBeforeCal = Calendar.getInstance().apply {
        timeInMillis = dayOfCal.timeInMillis
        add(Calendar.DAY_OF_YEAR, -1)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
      }
      if (oneDayBeforeCal.timeInMillis > now) {
        val scheduled = AlarmNotificationScheduler.scheduleAlarm(
          context = context,
          reminderId = ADVANCE_1_DAY_OFFSET + ms.id,
          title = "🌹 Ngày Mai: ${ms.title}!",
          message = "Chỉ còn 24 giờ nữa là đến ${ms.title}! Hãy sẵn sàng đón một ngày thật lãng mạn bên $partnerName nhé! 💕",
          triggerAtMillis = oneDayBeforeCal.timeInMillis,
          action = ReminderAlarmReceiver.ACTION_ANNIVERSARY_ALARM,
          channelId = ReminderAlarmReceiver.CHANNEL_ANNIVERSARIES_ID,
          targetTab = "calendar"
        )
        if (scheduled) scheduledCount++
      }

      // 3. Advance: 3 Days Before at 09:00 AM
      val threeDaysBeforeCal = Calendar.getInstance().apply {
        timeInMillis = dayOfCal.timeInMillis
        add(Calendar.DAY_OF_YEAR, -3)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
      }
      if (threeDaysBeforeCal.timeInMillis > now) {
        val scheduled = AlarmNotificationScheduler.scheduleAlarm(
          context = context,
          reminderId = ADVANCE_3_DAYS_OFFSET + ms.id,
          title = "🔔 Sắp Đến: ${ms.title}!",
          message = "Còn 3 ngày nữa là đến cột mốc ${ms.title} (${ms.formattedDate})! Đừng quên chuẩn bị một món quà hoặc kế hoạch hẹn hò bất ngờ cho $partnerName nhé! 🎁💖",
          triggerAtMillis = threeDaysBeforeCal.timeInMillis,
          action = ReminderAlarmReceiver.ACTION_ANNIVERSARY_ALARM,
          channelId = ReminderAlarmReceiver.CHANNEL_ANNIVERSARIES_ID,
          targetTab = "calendar"
        )
        if (scheduled) scheduledCount++
      }
    }

    Log.d(TAG, "Scheduled $scheduledCount Firestore love anniversary milestone alerts.")
    return scheduledCount
  }

  /**
   * Fires an immediate test milestone alert so the user can test their notifications,
   * sound, vibration, and banner.
   */
  fun triggerInstantTestMilestone(
    context: Context,
    partnerName: String = "người ấy",
    sampleMilestoneTitle: String = "💎 Bách Nhật Yêu (100 Ngày)"
  ) {
    ReminderAlarmReceiver.showNotification(
      context = context,
      title = "🔔 Thử Nghiệm: Cột Mốc $sampleMilestoneTitle ❤️",
      message = "Thông báo nhắc nhở cột mốc kỷ niệm tình yêu từ Firestore hoạt động hoàn hảo! Bạn sẽ nhận được thông báo trước 3 ngày, 1 ngày và vào đúng ngày kỷ niệm cùng $partnerName! ✨",
      notificationId = 8888,
      channelId = ReminderAlarmReceiver.CHANNEL_ANNIVERSARIES_ID,
      targetTab = "calendar"
    )
  }
}
