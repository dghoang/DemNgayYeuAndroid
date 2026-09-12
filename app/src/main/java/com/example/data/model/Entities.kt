package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milestones")
data class MilestoneEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val dateText: String,
  val subtitle: String,
  val categoryTag: String,
  val secondaryTag: String,
  val imageUrl: String,
  val daysRemaining: Int,
  val isPast: Boolean = false,
  val progressPercent: Float? = null,
  val isImportant: Boolean = false,
  val notificationEnabled: Boolean = true,
  val isSaved: Boolean = false,
  val alarmTimeMillis: Long? = null,
  val alarmTimeFormatted: String = ""
)

@Entity(tableName = "gift_ideas")
data class GiftIdeaEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val category: String,
  val badgeText: String,
  val tag: String,
  val description: String,
  val imageUrl: String,
  val isFavorited: Boolean = false,
  val detailsSnippet: String = "",
  val actionText: String = ""
)

@Entity(tableName = "checklist_items")
data class ChecklistItemEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val text: String,
  val iconName: String = "check",
  val isCompleted: Boolean = false
)

@Entity(tableName = "custom_reminders")
data class CustomReminderEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val dateText: String,
  val details: String,
  val daysRemainingText: String,
  val iconType: String = "stars",
  val alarmTimeMillis: Long? = null,
  val alarmTimeFormatted: String = ""
)

@Entity(tableName = "reminder_settings")
data class ReminderCadenceEntity(
  @PrimaryKey val key: String,
  val label: String,
  val isEnabled: Boolean
)

@Entity(tableName = "couple_profile")
data class CoupleProfileEntity(
  @PrimaryKey val id: Int = 1,
  val partner1Name: String,
  val partner1Birthday: String,
  val partner1ProfilePicture: String,
  val partner1Age: Int = 20,
  val partner1Zodiac: String = "Thiên Bình",
  val partner2Name: String,
  val partner2Birthday: String,
  val partner2ProfilePicture: String,
  val partner2Age: Int = 21,
  val partner2Zodiac: String = "Cự Giải",
  val loveTitle: String = "Bámmmm",
  val loveDays: Int = 1349,
  val anniversaryDate: String = "18/12/2022",
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "shared_memories")
data class SharedMemoryEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val dateText: String,
  val note: String = "",
  val photoUri: String,
  val location: String = "",
  val isFavorite: Boolean = false,
  val createdAt: Long = System.currentTimeMillis()
)
