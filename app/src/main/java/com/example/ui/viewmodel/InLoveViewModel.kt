package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
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
import com.example.data.repository.InLoveRepository
import com.example.ui.util.AppLanguage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InLoveViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: InLoveRepository

  val milestones: StateFlow<List<MilestoneEntity>>
  val giftIdeas: StateFlow<List<GiftIdeaEntity>>
  val checklistItems: StateFlow<List<ChecklistItemEntity>>
  val customReminders: StateFlow<List<CustomReminderEntity>>
  val reminderCadences: StateFlow<List<ReminderCadenceEntity>>
  val coupleProfile: StateFlow<CoupleProfileEntity?>
  val sharedMemories: StateFlow<List<SharedMemoryEntity>>
  val loveBadges: StateFlow<List<LoveBadgeEntity>>
  val anniversaryDates: StateFlow<List<AnniversaryDateEntity>>
  val giftReminders: StateFlow<List<GiftReminderEntity>>

  private val _selectedTab = MutableStateFlow(0) // Default to Love Screen matching image.png!
  val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

  private val _calendarFilter = MutableStateFlow("Tất cả (4)")
  val calendarFilter: StateFlow<String> = _calendarFilter.asStateFlow()

  private val _giftCategory = MutableStateFlow("Tất cả")
  val giftCategory: StateFlow<String> = _giftCategory.asStateFlow()

  private val _toastMessage = MutableStateFlow<String?>(null)
  val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

  private val _isAllNotificationsRead = MutableStateFlow(false)
  val isAllNotificationsRead: StateFlow<Boolean> = _isAllNotificationsRead.asStateFlow()

  private val _showAddReminderDialog = MutableStateFlow(false)
  val showAddReminderDialog: StateFlow<Boolean> = _showAddReminderDialog.asStateFlow()

  private val _showAddMilestoneDialog = MutableStateFlow(false)
  val showAddMilestoneDialog: StateFlow<Boolean> = _showAddMilestoneDialog.asStateFlow()

  private val _showAddChecklistDialog = MutableStateFlow(false)
  val showAddChecklistDialog: StateFlow<Boolean> = _showAddChecklistDialog.asStateFlow()

  private val _showAddAnniversaryDialog = MutableStateFlow(false)
  val showAddAnniversaryDialog: StateFlow<Boolean> = _showAddAnniversaryDialog.asStateFlow()

  private val _showAddGiftReminderDialog = MutableStateFlow(false)
  val showAddGiftReminderDialog: StateFlow<Boolean> = _showAddGiftReminderDialog.asStateFlow()

  // Set Alarm Reminder Dialog State
  private val _showSetAlarmDialog = MutableStateFlow(false)
  val showSetAlarmDialog: StateFlow<Boolean> = _showSetAlarmDialog.asStateFlow()

  private val _alarmDialogPresetTitle = MutableStateFlow("")
  val alarmDialogPresetTitle: StateFlow<String> = _alarmDialogPresetTitle.asStateFlow()

  private val _alarmDialogPresetMessage = MutableStateFlow("")
  val alarmDialogPresetMessage: StateFlow<String> = _alarmDialogPresetMessage.asStateFlow()

  private val _alarmDialogReminderId = MutableStateFlow<Long?>(null)
  val alarmDialogReminderId: StateFlow<Long?> = _alarmDialogReminderId.asStateFlow()

  private val _selectedGiftDetail = MutableStateFlow<GiftIdeaEntity?>(null)
  val selectedGiftDetail: StateFlow<GiftIdeaEntity?> = _selectedGiftDetail.asStateFlow()

  private val _showVipProposalDetail = MutableStateFlow(false)
  val showVipProposalDetail: StateFlow<Boolean> = _showVipProposalDetail.asStateFlow()

  private val _sweetNoteLiked = MutableStateFlow(false)
  val sweetNoteLiked: StateFlow<Boolean> = _sweetNoteLiked.asStateFlow()

  // App Language (VI or EN)
  private val _appLanguage = MutableStateFlow(AppLanguage.VI)
  val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

  // Language Dialog State (Shown on entry or when tapped)
  private val _showLanguageDialog = MutableStateFlow(false)
  val showLanguageDialog: StateFlow<Boolean> = _showLanguageDialog.asStateFlow()

  // User Guide Dialog State
  private val _showGuideDialog = MutableStateFlow(false)
  val showGuideDialog: StateFlow<Boolean> = _showGuideDialog.asStateFlow()

  // Wallpaper Picker Dialog State & Selected Wallpaper
  private val _showWallpaperDialog = MutableStateFlow(false)
  val showWallpaperDialog: StateFlow<Boolean> = _showWallpaperDialog.asStateFlow()

  private val _selectedWallpaperUrl = MutableStateFlow(
    "https://images.unsplash.com/photo-1522383225653-ed111181a951?q=80&w=1080&auto=format&fit=crop"
  )
  val selectedWallpaperUrl: StateFlow<String> = _selectedWallpaperUrl.asStateFlow()

  // Memory Capture Dialog State
  private val _showMemoryDialog = MutableStateFlow(false)
  val showMemoryDialog: StateFlow<Boolean> = _showMemoryDialog.asStateFlow()

  // Floating Hearts Overlay Trigger
  private val _floatingHeartsTrigger = MutableStateFlow(1L)
  val floatingHeartsTrigger: StateFlow<Long> = _floatingHeartsTrigger.asStateFlow()

  private val _isMilestoneCelebration = MutableStateFlow(false)
  val isMilestoneCelebration: StateFlow<Boolean> = _isMilestoneCelebration.asStateFlow()

  private val _selectedMemoryDetail = MutableStateFlow<SharedMemoryEntity?>(null)
  val selectedMemoryDetail: StateFlow<SharedMemoryEntity?> = _selectedMemoryDetail.asStateFlow()

  // Anniversary Memories Carousel Filter
  private val _selectedAnniversaryFilter = MutableStateFlow("Tất cả")
  val selectedAnniversaryFilter: StateFlow<String> = _selectedAnniversaryFilter.asStateFlow()

  fun selectAnniversaryFilter(filter: String) {
    _selectedAnniversaryFilter.value = filter
  }

  // Milestone Badge Tracker State
  private val _selectedBadge = MutableStateFlow<LoveBadgeEntity?>(null)
  val selectedBadge: StateFlow<LoveBadgeEntity?> = _selectedBadge.asStateFlow()

  private val _showBadgeShowcaseDialog = MutableStateFlow(false)
  val showBadgeShowcaseDialog: StateFlow<Boolean> = _showBadgeShowcaseDialog.asStateFlow()

  // Couple Profile State (as shown in user's image.png: Mhoang & TLinh)
  private val _boyName = MutableStateFlow("Mhoang")
  val boyName: StateFlow<String> = _boyName.asStateFlow()

  private val _boyBirthDate = MutableStateFlow("15/10/2004")
  val boyBirthDate: StateFlow<String> = _boyBirthDate.asStateFlow()

  private val _boyAge = MutableStateFlow(20)
  val boyAge: StateFlow<Int> = _boyAge.asStateFlow()

  private val _boyZodiac = MutableStateFlow("Thiên Bình")
  val boyZodiac: StateFlow<String> = _boyZodiac.asStateFlow()

  private val _boyAvatarUrl = MutableStateFlow("https://lh3.googleusercontent.com/aida-public/AB6AXuCg-PmA8kAH3aEsx4nS5akuDkkWQeWMutmW8Lc76ASO-JMvtiwMNbsTfuYqBGpez7bHTAYekQNilJ5X5BHaP78pQf4tATX48UynvtOeQWG8kcCF-v9OqIdcm1OAjLGtZsO1ygTFLVd9qW-yngUnwCmOtlFWn_wzhCPvYfzMcFLVzeEoOX9NuP8fk911cjdlzd0yv0FvSo3h7qg26BWqyaqSpVwTuvKoKaZ20NJLOUWeBfHbRlqMZcIwfI1FL31cw1WGBrQ")
  val boyAvatarUrl: StateFlow<String> = _boyAvatarUrl.asStateFlow()

  private val _girlName = MutableStateFlow("TLinh")
  val girlName: StateFlow<String> = _girlName.asStateFlow()

  private val _girlBirthDate = MutableStateFlow("24/07/2003")
  val girlBirthDate: StateFlow<String> = _girlBirthDate.asStateFlow()

  private val _girlAge = MutableStateFlow(21)
  val girlAge: StateFlow<Int> = _girlAge.asStateFlow()

  private val _girlZodiac = MutableStateFlow("Cự Giải")
  val girlZodiac: StateFlow<String> = _girlZodiac.asStateFlow()

  private val _girlAvatarUrl = MutableStateFlow("https://lh3.googleusercontent.com/aida-public/AB6AXuA7FGhAVlT3aua6tzpkBgh-_XXVsY-hCGooIPxOJ0acYyvZV9FZpASsMTgGO0vBFJLrLojRgwwnQEjfWAniwj2FAiKNpUjSRFNIQgmOhVN8fFSYwpOqLF8hAnWk3UM0dWxSVT1FwaK9ovXMP7Jwi-gjfRbUm2ISX1qyUY6bpBKBZnBYk7YIorizTPWI5c6w9XUdTTsMLFWT3c5ns8lYQooC0eEvt6S5zJNNobW_pn1PTJOb0K2REgdfTQ")
  val girlAvatarUrl: StateFlow<String> = _girlAvatarUrl.asStateFlow()

  private val _loveTitle = MutableStateFlow("Bámmmm")
  val loveTitle: StateFlow<String> = _loveTitle.asStateFlow()

  private val _loveDays = MutableStateFlow(1349)
  val loveDays: StateFlow<Int> = _loveDays.asStateFlow()

  private val _anniversaryDate = MutableStateFlow("18/12/2022")
  val anniversaryDate: StateFlow<String> = _anniversaryDate.asStateFlow()

  private val _showEditCoupleDialog = MutableStateFlow(false)
  val showEditCoupleDialog: StateFlow<Boolean> = _showEditCoupleDialog.asStateFlow()

  init {
    val database = AppDatabase.getDatabase(application)
    repository = InLoveRepository(database.inLoveDao())

    milestones = repository.milestones.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    giftIdeas = repository.giftIdeas.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    checklistItems = repository.checklistItems.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    customReminders = repository.customReminders.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    reminderCadences = repository.reminderCadences.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    coupleProfile = repository.coupleProfile.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      null
    )

    sharedMemories = repository.sharedMemories.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    loveBadges = repository.loveBadges.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    anniversaryDates = repository.anniversaryDates.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    giftReminders = repository.giftReminders.stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

    viewModelScope.launch {
      repository.initializeDefaultDataIfEmpty()
      // Automatically schedule all stored anniversaries & milestones in Room DB
      com.example.alarm.AlarmNotificationScheduler.scheduleAllAnniversariesFromDb(application)
    }

    viewModelScope.launch {
      repository.coupleProfile.collect { profile ->
        if (profile != null) {
          _boyName.value = profile.partner1Name
          _boyBirthDate.value = profile.partner1Birthday
          _boyAvatarUrl.value = profile.partner1ProfilePicture
          _boyAge.value = profile.partner1Age
          _boyZodiac.value = profile.partner1Zodiac
          _girlName.value = profile.partner2Name
          _girlBirthDate.value = profile.partner2Birthday
          _girlAvatarUrl.value = profile.partner2ProfilePicture
          _girlAge.value = profile.partner2Age
          _girlZodiac.value = profile.partner2Zodiac
          _loveTitle.value = profile.loveTitle
          _loveDays.value = profile.loveDays
          _anniversaryDate.value = profile.anniversaryDate
        }
      }
    }
  }

  fun triggerFloatingHearts(isMilestone: Boolean = false) {
    _isMilestoneCelebration.value = isMilestone
    _floatingHeartsTrigger.value = System.currentTimeMillis()
  }

  fun celebrateMilestoneAnniversary(days: Int) {
    triggerFloatingHearts(isMilestone = true)
    val celebrationMessage = if (_appLanguage.value == AppLanguage.VI) {
      "🎉 Chúc Mừng Cột Mốc $days Ngày Yêu! Mưa tim ngập tràn chúc phúc đôi bạn! 💕✨"
    } else {
      "🎉 Milestone Reached: $days Days Together! Showering love hearts! 💕✨"
    }
    showToast(celebrationMessage)
  }

  fun setTab(index: Int) {
    _selectedTab.value = index
  }

  fun setCalendarFilter(filter: String) {
    _calendarFilter.value = filter
  }

  fun setGiftCategory(cat: String) {
    _giftCategory.value = cat
  }

  fun showToast(msg: String) {
    _toastMessage.value = msg
  }

  fun clearToast() {
    _toastMessage.value = null
  }

  fun toggleChecklist(item: ChecklistItemEntity) {
    viewModelScope.launch {
      repository.toggleChecklistItem(item)
    }
  }

  fun addChecklist(text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      repository.addChecklistItem(text.trim())
      showToast("Đã thêm vào checklist chuẩn bị!")
    }
  }

  fun toggleGiftFavorite(item: GiftIdeaEntity) {
    viewModelScope.launch {
      repository.toggleGiftFavorite(item)
      showToast(if (!item.isFavorited) "Đã lưu ý tưởng quà tặng này!" else "Đã bỏ lưu ý tưởng")
    }
  }

  fun toggleCadence(item: ReminderCadenceEntity) {
    viewModelScope.launch {
      repository.toggleCadence(item)
      showToast(if (!item.isEnabled) "Đã bật: ${item.label}" else "Đã tắt: ${item.label}")
    }
  }

  fun addCustomReminder(title: String, dateText: String, note: String) {
    if (title.isBlank()) {
      showToast("Vui lòng nhập tên lời nhắc nhé!")
      return
    }
    viewModelScope.launch {
      repository.addCustomReminder(title, dateText, note)
      _showAddReminderDialog.value = false
      triggerFloatingHearts()
      showToast("Đã thêm thành công lời nhắc hẹn hò mới!")
    }
  }

  fun openSetAlarmDialog(title: String = "", message: String = "", reminderId: Long? = null) {
    _alarmDialogPresetTitle.value = title
    _alarmDialogPresetMessage.value = message
    _alarmDialogReminderId.value = reminderId
    _showSetAlarmDialog.value = true
  }

  fun closeSetAlarmDialog() {
    _showSetAlarmDialog.value = false
  }

  fun scheduleReminderAlarm(
    title: String,
    message: String,
    triggerAtMillis: Long,
    reminderId: Long = System.currentTimeMillis()
  ) {
    val context = getApplication<Application>()
    val success = com.example.alarm.AlarmNotificationScheduler.scheduleAlarm(
      context = context,
      reminderId = reminderId,
      title = title,
      message = message,
      triggerAtMillis = triggerAtMillis
    )
    if (success) {
      val formatted = com.example.alarm.AlarmNotificationScheduler.formatAlarmTime(triggerAtMillis)
      showToast("⏰ Đã hẹn giờ báo thức lúc $formatted!")
      triggerFloatingHearts()
    } else {
      showToast("Không thể đặt lịch báo thức. Vui lòng kiểm tra quyền hệ thống.")
    }
    _showSetAlarmDialog.value = false
  }

  fun cancelReminderAlarm(reminderId: Long) {
    val context = getApplication<Application>()
    com.example.alarm.AlarmNotificationScheduler.cancelAlarm(context, reminderId)
  }

  fun triggerInstantTestAlarm(
    title: String = "Thử nghiệm báo thức tình yêu ❤️",
    message: String = "Chuông và thông báo kỷ niệm hoạt động rất tốt!"
  ) {
    val context = getApplication<Application>()
    com.example.alarm.AlarmNotificationScheduler.triggerInstantTest(context, title, message)
    showToast("🔔 Đã phát thử chuông thông báo kỷ niệm!")
  }

  fun deleteCustomReminder(id: Long) {
    viewModelScope.launch {
      cancelReminderAlarm(id)
      repository.deleteCustomReminder(id)
      showToast("Đã xóa lời nhắc hẹn thành công.")
    }
  }

  fun deleteMilestone(id: Long) {
    viewModelScope.launch {
      cancelReminderAlarm(id)
      repository.deleteMilestone(id)
      showToast("Đã xóa ngày kỷ niệm khỏi lịch.")
    }
  }

  fun deleteChecklistItem(id: Long) {
    viewModelScope.launch {
      repository.deleteChecklistItem(id)
      showToast("Đã xóa công việc chuẩn bị quà.")
    }
  }

  fun addMilestone(
    title: String,
    dateText: String,
    subtitle: String,
    categoryTag: String,
    secondaryTag: String,
    imageUrl: String,
    daysRemaining: Int,
    isImportant: Boolean
  ) {
    if (title.isBlank()) {
      showToast("Vui lòng nhập tiêu đề kỷ niệm!")
      return
    }
    viewModelScope.launch {
      val ms = MilestoneEntity(
        title = title,
        dateText = dateText,
        subtitle = subtitle,
        categoryTag = if (categoryTag.isNotBlank()) categoryTag else "Kỷ Niệm",
        secondaryTag = if (secondaryTag.isNotBlank()) secondaryTag else "Ý Nghĩa",
        imageUrl = if (imageUrl.isNotBlank()) imageUrl else "https://lh3.googleusercontent.com/aida-public/AB6AXuANA2ChG6LS0d4msPLYL4g-4W2BU_q52b1udp8NDaY4NJSzyw4NZnx6e2qKT1oMKzYrc76_1-nMndDIrMSO7k1QXvz66V8WEt7D3GuZmigotLqTpeJbbAdYrKyOPyUV1W-RxHRZbCo09c24vQC-5ZIS2iG1PM6s7V5_nejLv9V0-tTQujYKsbrgGRbfxlS_JvPgXqa_zXWfABTSL3rCM-VVaw2iIyPJ43vA8jK5bjWCrVYznx4hzUS6_w",
        daysRemaining = daysRemaining,
        isPast = daysRemaining < 0,
        isImportant = isImportant,
        isUserCreated = true,
        notificationEnabled = true
      )
      val newId = repository.addMilestone(ms)
      val context = getApplication<Application>()
      com.example.alarm.AlarmNotificationScheduler.scheduleMilestoneNotification(context, ms.copy(id = newId))
      _showAddMilestoneDialog.value = false
      triggerFloatingHearts()
      showToast("Đã thêm kỷ niệm mới & hẹn giờ thông báo! 🔔")
    }
  }

  fun toggleMilestoneNotification(item: MilestoneEntity) {
    viewModelScope.launch {
      val context = getApplication<Application>()
      val willBeEnabled = !item.notificationEnabled
      val updated = item.copy(notificationEnabled = willBeEnabled)
      repository.toggleMilestoneNotification(item)
      if (willBeEnabled) {
        com.example.alarm.AlarmNotificationScheduler.scheduleMilestoneNotification(context, updated)
        showToast("🔔 Đã bật thông báo cho cột mốc '${item.title}'")
      } else {
        com.example.alarm.AlarmNotificationScheduler.cancelMilestoneNotification(context, item.id)
        showToast("🔕 Đã tắt thông báo cho '${item.title}'")
      }
    }
  }

  fun markAllNotificationsRead() {
    _isAllNotificationsRead.value = true
    showToast("Đã đánh dấu đã đọc tất cả thông báo!")
  }

  fun toggleSweetNoteLike() {
    val newState = !_sweetNoteLiked.value
    _sweetNoteLiked.value = newState
    if (newState) {
      triggerFloatingHearts()
      showToast("Đã gửi phản hồi tim ngọt ngào ❤️!")
    }
  }

  fun saveCoupleProfile(
    boy: String,
    boyBirth: String,
    boyAvatar: String,
    boyA: Int,
    boyZod: String,
    girl: String,
    girlBirth: String,
    girlAvatar: String,
    girlA: Int,
    girlZod: String,
    title: String,
    days: Int,
    anniversary: String = "18/12/2022"
  ) {
    val cleanBoy = boy.trim().ifEmpty { "Mhoang" }
    val cleanBoyBirth = boyBirth.trim().ifEmpty { "15/10/2004" }
    val cleanBoyAvatar = boyAvatar.trim().ifEmpty { _boyAvatarUrl.value }
    val cleanBoyZod = boyZod.trim().ifEmpty { "Thiên Bình" }

    val cleanGirl = girl.trim().ifEmpty { "TLinh" }
    val cleanGirlBirth = girlBirth.trim().ifEmpty { "24/07/2003" }
    val cleanGirlAvatar = girlAvatar.trim().ifEmpty { _girlAvatarUrl.value }
    val cleanGirlZod = girlZod.trim().ifEmpty { "Cự Giải" }

    val cleanTitle = title.trim().ifEmpty { "Bámmmm" }

    viewModelScope.launch {
      val entity = CoupleProfileEntity(
        id = 1,
        partner1Name = cleanBoy,
        partner1Birthday = cleanBoyBirth,
        partner1ProfilePicture = cleanBoyAvatar,
        partner1Age = boyA,
        partner1Zodiac = cleanBoyZod,
        partner2Name = cleanGirl,
        partner2Birthday = cleanGirlBirth,
        partner2ProfilePicture = cleanGirlAvatar,
        partner2Age = girlA,
        partner2Zodiac = cleanGirlZod,
        loveTitle = cleanTitle,
        loveDays = days,
        anniversaryDate = anniversary
      )
      repository.saveCoupleProfile(entity)

      // Also update local StateFlows immediately for reactive smoothness
      _boyName.value = cleanBoy
      _boyBirthDate.value = cleanBoyBirth
      _boyAvatarUrl.value = cleanBoyAvatar
      _boyAge.value = boyA
      _boyZodiac.value = cleanBoyZod
      _girlName.value = cleanGirl
      _girlBirthDate.value = cleanGirlBirth
      _girlAvatarUrl.value = cleanGirlAvatar
      _girlAge.value = girlA
      _girlZodiac.value = cleanGirlZod
      _loveTitle.value = cleanTitle
      _loveDays.value = days
      _anniversaryDate.value = anniversary

      _showEditCoupleDialog.value = false
      triggerFloatingHearts()
      showToast("Đã lưu hồ sơ 2 bạn vào cơ sở dữ liệu Room thành công! ❤️")
    }
  }

  /**
   * Calculates the exact number of days a couple has been together given their
   * anniversary/start date string (e.g. "18/12/2022").
   * Counts the start day as day 1 so today is included in the streak.
   */
  fun calculateLoveDaysFromDate(dateStr: String): Int {
    val formats = listOf("dd/MM/yyyy", "d/M/yyyy", "dd-MM-yyyy", "yyyy-MM-dd")
    for (pattern in formats) {
      try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.isLenient = false
        val date = sdf.parse(dateStr.trim())
        if (date != null) {
          val startCal = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
          }
          val nowCal = Calendar.getInstance()
          val diffMillis = nowCal.timeInMillis - startCal.timeInMillis
          if (diffMillis >= 0) {
            val days = TimeUnit.MILLISECONDS.toDays(diffMillis) + 1
            return days.toInt()
          }
        }
      } catch (_: Exception) {}
    }
    return _loveDays.value
  }

  /**
   * Sets the anniversary date, calculates the total days elapsed, updates the
   * Room Database and UI StateFlows, and gives celebratory romantic feedback.
   */
  fun setAnniversaryAndRecalculateDays(newDate: String) {
    val cleanDate = newDate.trim().ifEmpty { "18/12/2022" }
    val calcDays = calculateLoveDaysFromDate(cleanDate)
    _anniversaryDate.value = cleanDate
    _loveDays.value = calcDays

    viewModelScope.launch {
      val current = repository.coupleProfile.first()
      if (current != null) {
        val updated = current.copy(
          anniversaryDate = cleanDate,
          loveDays = calcDays,
          updatedAt = System.currentTimeMillis()
        )
        repository.saveCoupleProfile(updated)
      }
      triggerFloatingHearts()
      showToast(
        if (_appLanguage.value == AppLanguage.VI) {
          "Đã tính lại: $calcDays ngày yêu nhau từ ngày $cleanDate! 💕"
        } else {
          "Recalculated: $calcDays days together since $cleanDate! 💕"
        }
      )
    }
  }

  /**
   * Direct sync of calculated days to the couple profile and database.
   */
  fun syncCalculatedDaysToHome(days: Int) {
    if (days <= 0) return
    _loveDays.value = days
    viewModelScope.launch {
      val current = repository.coupleProfile.first()
      if (current != null) {
        val updated = current.copy(
          loveDays = days,
          updatedAt = System.currentTimeMillis()
        )
        repository.saveCoupleProfile(updated)
      }
      triggerFloatingHearts()
      showToast(
        if (_appLanguage.value == AppLanguage.VI) {
          "Đã đồng bộ $days ngày yêu lên trang chủ! ❤️"
        } else {
          "Synced $days love days to main screen! ❤️"
        }
      )
    }
  }

  fun updateCoupleInfo(
    boy: String,
    boyBirth: String,
    boyA: Int,
    boyZod: String,
    girl: String,
    girlBirth: String,
    girlA: Int,
    girlZod: String,
    title: String,
    days: Int
  ) {
    saveCoupleProfile(
      boy = boy,
      boyBirth = boyBirth,
      boyAvatar = _boyAvatarUrl.value,
      boyA = boyA,
      boyZod = boyZod,
      girl = girl,
      girlBirth = girlBirth,
      girlAvatar = _girlAvatarUrl.value,
      girlA = girlA,
      girlZod = girlZod,
      title = title,
      days = days
    )
  }

  fun openEditCoupleDialog() {
    _showEditCoupleDialog.value = true
  }

  fun closeEditCoupleDialog() {
    _showEditCoupleDialog.value = false
  }

  fun openAddReminderDialog() {
    _showAddReminderDialog.value = true
  }

  fun closeAddReminderDialog() {
    _showAddReminderDialog.value = false
  }

  fun openAddMilestoneDialog() {
    _showAddMilestoneDialog.value = true
  }

  fun closeAddMilestoneDialog() {
    _showAddMilestoneDialog.value = false
  }

  fun openAddChecklistDialog() {
    _showAddChecklistDialog.value = true
  }

  fun closeAddChecklistDialog() {
    _showAddChecklistDialog.value = false
  }

  fun openAddAnniversaryDialog() {
    _showAddAnniversaryDialog.value = true
  }

  fun closeAddAnniversaryDialog() {
    _showAddAnniversaryDialog.value = false
  }

  fun openAddGiftReminderDialog() {
    _showAddGiftReminderDialog.value = true
  }

  fun closeAddGiftReminderDialog() {
    _showAddGiftReminderDialog.value = false
  }

  fun openGiftDetail(item: GiftIdeaEntity) {
    _selectedGiftDetail.value = item
  }

  fun closeGiftDetail() {
    _selectedGiftDetail.value = null
  }

  fun openVipProposal() {
    _showVipProposalDetail.value = true
  }

  fun closeVipProposal() {
    _showVipProposalDetail.value = false
  }

  fun setLanguage(lang: AppLanguage) {
    _appLanguage.value = lang
    val resId = if (lang == AppLanguage.VI) com.example.R.string.toast_switched_vi else com.example.R.string.toast_switched_en
    val config = android.content.res.Configuration(getApplication<Application>().resources.configuration).apply {
      setLocale(if (lang == AppLanguage.VI) java.util.Locale("vi") else java.util.Locale("en"))
    }
    val localizedContext = getApplication<Application>().createConfigurationContext(config)
    val notice = localizedContext.getString(resId)
    showToast(notice)
  }

  fun openLanguageDialog() {
    _showLanguageDialog.value = true
  }

  fun closeLanguageDialog() {
    _showLanguageDialog.value = false
  }

  fun openGuideDialog() {
    _showGuideDialog.value = true
  }

  fun closeGuideDialog() {
    _showGuideDialog.value = false
  }

  fun openWallpaperDialog() {
    _showWallpaperDialog.value = true
  }

  fun closeWallpaperDialog() {
    _showWallpaperDialog.value = false
  }

  fun setWallpaper(url: String) {
    if (url.isNotBlank()) {
      _selectedWallpaperUrl.value = url.trim()
      triggerFloatingHearts()
      val msg = if (_appLanguage.value == AppLanguage.VI) "Đã đổi hình nền lãng mạn mới! 🌸" else "Romantic wallpaper updated! 🌸"
      showToast(msg)
    }
  }

  fun openMemoryDialog() {
    _showMemoryDialog.value = true
  }

  fun closeMemoryDialog() {
    _showMemoryDialog.value = false
  }

  fun saveMemory(note: String, photoUrl: String) {
    viewModelScope.launch {
      val cleanNote = note.trim().ifEmpty { "Kỷ niệm ngày yêu ngọt ngào cùng nhau" }
      val cleanUrl = photoUrl.trim().ifEmpty {
        "https://images.unsplash.com/photo-1518199266791-5375a83190b7?q=80&w=1080&auto=format&fit=crop"
      }
      repository.addMilestone(
        MilestoneEntity(
          title = "Khoảnh Khắc: ${cleanNote.take(24)}",
          dateText = "Hôm nay",
          subtitle = cleanNote,
          categoryTag = "Kỷ Niệm",
          secondaryTag = "Ảnh Đôi",
          imageUrl = cleanUrl,
          daysRemaining = 0,
          isPast = true,
          isImportant = true
        )
      )
      _showMemoryDialog.value = false
      triggerFloatingHearts()
      val msg = if (_appLanguage.value == AppLanguage.VI) "Đã lưu lại bức ảnh kỷ niệm vào dòng thời gian! 📸❤️" else "Memory photo saved to timeline! 📸❤️"
      showToast(msg)
    }
  }

  fun addSharedMemory(
    title: String,
    dateText: String,
    photoUri: String,
    note: String = "",
    location: String = "",
    anniversaryTitle: String = "18/12 - Ngày Yêu Nhau"
  ) {
    viewModelScope.launch {
      val validTitle = title.trim().ifEmpty { "Khoảnh Khắc Ngọt Ngào" }
      val validDate = dateText.trim().ifEmpty { "Hôm nay" }
      val validUri = photoUri.trim().ifEmpty {
        "https://images.unsplash.com/photo-1518199266791-5375a83190b7?q=80&w=1080&auto=format&fit=crop"
      }
      val validAnniversary = anniversaryTitle.trim().ifEmpty { "18/12 - Ngày Yêu Nhau" }
      repository.addSharedMemory(
        title = validTitle,
        dateText = validDate,
        photoUri = validUri,
        note = note.trim(),
        location = location.trim(),
        anniversaryTitle = validAnniversary
      )
      triggerFloatingHearts()
      val msg = if (_appLanguage.value == AppLanguage.VI) {
        "Đã lưu kỷ niệm \"$validTitle\" vào Album! 📸💕"
      } else {
        "Saved memory \"$validTitle\" to Album! 📸💕"
      }
      showToast(msg)
    }
  }

  fun deleteSharedMemory(id: Long) {
    viewModelScope.launch {
      repository.deleteSharedMemory(id)
      if (_selectedMemoryDetail.value?.id == id) {
        _selectedMemoryDetail.value = null
      }
      val msg = if (_appLanguage.value == AppLanguage.VI) "Đã xóa ảnh kỷ niệm" else "Memory deleted"
      showToast(msg)
    }
  }

  fun toggleMemoryFavorite(memory: SharedMemoryEntity) {
    viewModelScope.launch {
      repository.toggleMemoryFavorite(memory)
      if (_selectedMemoryDetail.value?.id == memory.id) {
        _selectedMemoryDetail.value = memory.copy(isFavorite = !memory.isFavorite)
      }
      triggerFloatingHearts()
    }
  }

  fun openMemoryDetail(memory: SharedMemoryEntity) {
    _selectedMemoryDetail.value = memory
  }

  fun closeMemoryDetail() {
    _selectedMemoryDetail.value = null
  }

  // Visual Milestone Tracker & Badges
  fun selectBadge(badge: LoveBadgeEntity?) {
    _selectedBadge.value = badge
  }

  fun openBadgeShowcase() {
    _showBadgeShowcaseDialog.value = true
  }

  fun closeBadgeShowcase() {
    _showBadgeShowcaseDialog.value = false
  }

  fun claimBadge(badgeId: String, customNote: String = "") {
    viewModelScope.launch {
      repository.claimLoveBadge(badgeId, customNote)
      triggerFloatingHearts(isMilestone = true)
      val msg = if (_appLanguage.value == AppLanguage.VI) {
        "🏆 Đã vinh danh và lưu kỷ niệm vào Huy Hiệu Trái Tim!"
      } else {
        "🏆 Badge honored and memory saved to Heart Trophy!"
      }
      showToast(msg)
    }
  }

  fun celebrateBadge(badge: LoveBadgeEntity) {
    triggerFloatingHearts(isMilestone = true)
    val msg = if (_appLanguage.value == AppLanguage.VI) {
      "🎉 Chúc mừng cột mốc ${badge.targetDays} ngày bên nhau! 💕✨"
    } else {
      "🎉 Celebrating ${badge.targetDays} days together! 💕✨"
    }
    showToast(msg)
  }

  // Room Persistence for Anniversary Dates
  fun addAnniversaryDate(
    title: String,
    dateText: String,
    type: String = "LOVE",
    description: String = "",
    isAnnual: Boolean = true,
    reminderDaysBefore: Int = 3
  ) {
    if (title.isBlank()) {
      showToast("Vui lòng nhập tên ngày kỷ niệm!")
      return
    }
    viewModelScope.launch {
      val newId = repository.addAnniversaryDate(
        title = title.trim(),
        dateText = dateText.trim(),
        type = type,
        description = description.trim(),
        isAnnual = isAnnual,
        reminderDaysBefore = reminderDaysBefore
      )
      val context = getApplication<Application>()
      val ann = AnniversaryDateEntity(
        id = newId,
        title = title.trim(),
        dateText = dateText.trim(),
        type = type,
        description = description.trim(),
        isAnnual = isAnnual,
        notificationEnabled = true,
        reminderDaysBefore = reminderDaysBefore
      )
      com.example.alarm.AlarmNotificationScheduler.scheduleAnniversaryNotification(context, ann)
      _showAddAnniversaryDialog.value = false
      triggerFloatingHearts()
      showToast("Đã lưu ngày kỷ niệm & kích hoạt thông báo tự động! 🔔")
    }
  }

  fun updateAnniversaryDate(item: AnniversaryDateEntity) {
    viewModelScope.launch {
      repository.updateAnniversaryDate(item)
      val context = getApplication<Application>()
      if (item.notificationEnabled) {
        com.example.alarm.AlarmNotificationScheduler.scheduleAnniversaryNotification(context, item)
      } else {
        com.example.alarm.AlarmNotificationScheduler.cancelAnniversaryNotification(context, item.id)
      }
      showToast("Đã cập nhật ngày kỷ niệm!")
    }
  }

  fun deleteAnniversaryDate(id: Long) {
    viewModelScope.launch {
      val context = getApplication<Application>()
      com.example.alarm.AlarmNotificationScheduler.cancelAnniversaryNotification(context, id)
      repository.deleteAnniversaryDate(id)
      showToast("Đã xóa ngày kỷ niệm khỏi thiết bị.")
    }
  }

  fun toggleAnniversaryNotification(item: AnniversaryDateEntity) {
    viewModelScope.launch {
      val context = getApplication<Application>()
      val willBeEnabled = !item.notificationEnabled
      val updated = item.copy(notificationEnabled = willBeEnabled)
      repository.updateAnniversaryDate(updated)
      if (willBeEnabled) {
        val scheduled = com.example.alarm.AlarmNotificationScheduler.scheduleAnniversaryNotification(context, updated)
        showToast(if (scheduled) "🔔 Đã bật thông báo kỷ niệm '${item.title}'" else "Đã bật thông báo '${item.title}'")
      } else {
        com.example.alarm.AlarmNotificationScheduler.cancelAnniversaryNotification(context, item.id)
        showToast("🔕 Đã tắt thông báo kỷ niệm '${item.title}'")
      }
    }
  }

  private val _globalAnniversaryNotifications = MutableStateFlow(true)
  val globalAnniversaryNotifications: StateFlow<Boolean> = _globalAnniversaryNotifications.asStateFlow()

  fun toggleGlobalAnniversaryNotifications(enabled: Boolean) {
    _globalAnniversaryNotifications.value = enabled
    val context = getApplication<Application>()
    viewModelScope.launch {
      if (enabled) {
        val count = com.example.alarm.AlarmNotificationScheduler.scheduleAllAnniversariesFromDb(context)
        showToast("🔔 Đã bật & đồng bộ lại $count thông báo kỷ niệm!")
      } else {
        showToast("🔕 Đã tạm dừng thông báo ngày kỷ niệm.")
      }
    }
  }

  fun triggerTestAnniversaryNotification() {
    val context = getApplication<Application>()
    viewModelScope.launch {
      val anniversaries = repository.getAnniversaryDatesList()
      val firstAnn = anniversaries.firstOrNull { it.notificationEnabled } ?: anniversaries.firstOrNull()
      if (firstAnn != null) {
        com.example.alarm.ReminderAlarmReceiver.showNotification(
          context = context,
          title = "🎉 Thử nghiệm Kỷ Niệm: ${firstAnn.title} ❤️",
          message = "Còn ít ngày nữa là đến '${firstAnn.title}' (${firstAnn.dateText}). Đừng quên chuẩn bị món quà bất ngờ và một buổi tối lãng mạn cho người ấy nhé! 🎁✨",
          notificationId = 8888,
          channelId = com.example.alarm.ReminderAlarmReceiver.CHANNEL_ANNIVERSARIES_ID,
          targetTab = "calendar"
        )
      } else {
        com.example.alarm.AlarmNotificationScheduler.triggerInstantTest(context)
      }
      showToast("🔔 Đã gửi thông báo kỷ niệm thử nghiệm lên thanh trạng thái!")
    }
  }

  fun resyncAllAnniversaryAlarms() {
    val context = getApplication<Application>()
    viewModelScope.launch {
      val count = com.example.alarm.AlarmNotificationScheduler.scheduleAllAnniversariesFromDb(context)
      showToast("⏰ Đã quét & kích hoạt lại $count thông báo kỷ niệm từ cơ sở dữ liệu!")
    }
  }

  // Room Persistence for Gift Reminders
  fun addGiftReminder(
    title: String,
    recipient: String = "Người ấy",
    occasion: String = "Kỷ niệm ngày yêu",
    dueDateText: String = "",
    estimatedBudget: String = "",
    notes: String = ""
  ) {
    if (title.isBlank()) {
      showToast("Vui lòng nhập món quà cần nhắc nhở!")
      return
    }
    viewModelScope.launch {
      repository.addGiftReminder(
        title = title.trim(),
        recipient = recipient.trim().ifEmpty { "Người ấy" },
        occasion = occasion.trim().ifEmpty { "Kỷ niệm ngày yêu" },
        dueDateText = dueDateText.trim().ifEmpty { "Sớm nhất" },
        estimatedBudget = estimatedBudget.trim().ifEmpty { "Tùy chọn" },
        notes = notes.trim()
      )
      _showAddGiftReminderDialog.value = false
      triggerFloatingHearts()
      showToast("Đã lưu lời nhắc quà tặng vào cơ sở dữ liệu Room!")
    }
  }

  fun updateGiftReminder(item: GiftReminderEntity) {
    viewModelScope.launch {
      repository.updateGiftReminder(item)
      showToast("Đã cập nhật lời nhắc quà tặng!")
    }
  }

  fun toggleGiftReminderCompleted(item: GiftReminderEntity) {
    viewModelScope.launch {
      repository.toggleGiftReminderCompleted(item)
      val msg = if (!item.isCompleted) "🎁 Tuyệt vời! Đã hoàn thành chuẩn bị món quà!" else "Đã chuyển về danh sách chuẩn bị quà."
      showToast(msg)
    }
  }

  fun deleteGiftReminder(id: Long) {
    viewModelScope.launch {
      repository.deleteGiftReminder(id)
      showToast("Đã xóa lời nhắc quà tặng.")
    }
  }
}
