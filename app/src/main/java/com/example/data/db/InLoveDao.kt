package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AnniversaryDateEntity
import com.example.data.model.ChecklistItemEntity
import com.example.data.model.CoupleProfileEntity
import com.example.data.model.CustomReminderEntity
import com.example.data.model.GiftIdeaEntity
import com.example.data.model.GiftReminderEntity
import com.example.data.model.LoveBadgeEntity
import com.example.data.model.MilestoneEntity
import com.example.data.model.ReminderCadenceEntity
import com.example.data.model.SharedMemoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InLoveDao {
  // Milestones
  @Query("SELECT * FROM milestones ORDER BY id ASC")
  fun getAllMilestones(): Flow<List<MilestoneEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMilestones(items: List<MilestoneEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMilestone(item: MilestoneEntity): Long

  @Update
  suspend fun updateMilestone(item: MilestoneEntity)

  @Query("DELETE FROM milestones WHERE id = :id")
  suspend fun deleteMilestoneById(id: Long)

  // Gift Ideas
  @Query("SELECT * FROM gift_ideas ORDER BY id ASC")
  fun getAllGiftIdeas(): Flow<List<GiftIdeaEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGiftIdeas(items: List<GiftIdeaEntity>)

  @Update
  suspend fun updateGiftIdea(item: GiftIdeaEntity)

  // Checklist Items
  @Query("SELECT * FROM checklist_items ORDER BY id ASC")
  fun getAllChecklistItems(): Flow<List<ChecklistItemEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChecklistItems(items: List<ChecklistItemEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChecklistItem(item: ChecklistItemEntity): Long

  @Update
  suspend fun updateChecklistItem(item: ChecklistItemEntity)

  @Query("DELETE FROM checklist_items WHERE id = :id")
  suspend fun deleteChecklistItemById(id: Long)

  // Custom Reminders
  @Query("SELECT * FROM custom_reminders ORDER BY id DESC")
  fun getAllCustomReminders(): Flow<List<CustomReminderEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCustomReminders(items: List<CustomReminderEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCustomReminder(item: CustomReminderEntity): Long

  @Query("DELETE FROM custom_reminders WHERE id = :id")
  suspend fun deleteCustomReminderById(id: Long)

  // Reminder Cadences
  @Query("SELECT * FROM reminder_settings")
  fun getAllReminderCadences(): Flow<List<ReminderCadenceEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReminderCadences(items: List<ReminderCadenceEntity>)

  @Update
  suspend fun updateReminderCadence(item: ReminderCadenceEntity)

  // Couple Profile
  @Query("SELECT * FROM couple_profile WHERE id = 1 LIMIT 1")
  fun getCoupleProfile(): Flow<CoupleProfileEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCoupleProfile(profile: CoupleProfileEntity)

  // Shared Memories
  @Query("SELECT * FROM shared_memories ORDER BY id DESC")
  fun getAllSharedMemories(): Flow<List<SharedMemoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSharedMemory(memory: SharedMemoryEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSharedMemories(memories: List<SharedMemoryEntity>)

  @Update
  suspend fun updateSharedMemory(memory: SharedMemoryEntity)

  @Query("DELETE FROM shared_memories WHERE id = :id")
  suspend fun deleteSharedMemoryById(id: Long)

  // Love Milestone Badges
  @Query("SELECT * FROM love_badges ORDER BY targetDays ASC")
  fun getAllLoveBadges(): Flow<List<LoveBadgeEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLoveBadges(items: List<LoveBadgeEntity>)

  @Update
  suspend fun updateLoveBadge(item: LoveBadgeEntity)

  // Anniversary Dates Persistence
  @Query("SELECT * FROM anniversary_dates ORDER BY id ASC")
  fun getAllAnniversaryDates(): Flow<List<AnniversaryDateEntity>>

  @Query("SELECT * FROM anniversary_dates ORDER BY id ASC")
  suspend fun getAnniversaryDatesList(): List<AnniversaryDateEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAnniversaryDates(items: List<AnniversaryDateEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAnniversaryDate(item: AnniversaryDateEntity): Long

  @Update
  suspend fun updateAnniversaryDate(item: AnniversaryDateEntity)

  @Query("DELETE FROM anniversary_dates WHERE id = :id")
  suspend fun deleteAnniversaryDateById(id: Long)

  // Direct suspend queries for background scheduling
  @Query("SELECT * FROM milestones ORDER BY id ASC")
  suspend fun getMilestonesList(): List<MilestoneEntity>

  @Query("SELECT * FROM couple_profile WHERE id = 1 LIMIT 1")
  suspend fun getCoupleProfileSync(): CoupleProfileEntity?

  @Query("SELECT * FROM custom_reminders ORDER BY id DESC")
  suspend fun getCustomRemindersList(): List<CustomReminderEntity>

  // Gift Reminders Persistence
  @Query("SELECT * FROM gift_reminders ORDER BY isCompleted ASC, id DESC")
  fun getAllGiftReminders(): Flow<List<GiftReminderEntity>>

  @Query("SELECT * FROM gift_reminders ORDER BY isCompleted ASC, id DESC")
  suspend fun getGiftRemindersList(): List<GiftReminderEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGiftReminders(items: List<GiftReminderEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGiftReminder(item: GiftReminderEntity): Long

  @Update
  suspend fun updateGiftReminder(item: GiftReminderEntity)

  @Query("DELETE FROM gift_reminders WHERE id = :id")
  suspend fun deleteGiftReminderById(id: Long)
}
