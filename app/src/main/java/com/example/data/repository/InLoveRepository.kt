package com.example.data.repository

import com.example.data.db.InLoveDao
import com.example.data.model.ChecklistItemEntity
import com.example.data.model.CoupleProfileEntity
import com.example.data.model.CustomReminderEntity
import com.example.data.model.GiftIdeaEntity
import com.example.data.model.MilestoneEntity
import com.example.data.model.ReminderCadenceEntity
import com.example.data.model.SharedMemoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class InLoveRepository(private val dao: InLoveDao) {

  val milestones: Flow<List<MilestoneEntity>> = dao.getAllMilestones()
  val giftIdeas: Flow<List<GiftIdeaEntity>> = dao.getAllGiftIdeas()
  val checklistItems: Flow<List<ChecklistItemEntity>> = dao.getAllChecklistItems()
  val customReminders: Flow<List<CustomReminderEntity>> = dao.getAllCustomReminders()
  val reminderCadences: Flow<List<ReminderCadenceEntity>> = dao.getAllReminderCadences()
  val coupleProfile: Flow<CoupleProfileEntity?> = dao.getCoupleProfile()
  val sharedMemories: Flow<List<SharedMemoryEntity>> = dao.getAllSharedMemories()

  suspend fun initializeDefaultDataIfEmpty() {
    val currentMilestones = dao.getAllMilestones().first()
    if (currentMilestones.isEmpty()) {
      dao.insertMilestones(
        listOf(
          MilestoneEntity(
            id = 1,
            title = "Kỷ niệm 1.000 ngày bên nhau",
            dateText = "11 Tháng 9, 2026",
            subtitle = "Thứ Sáu",
            categoryTag = "Quan Trọng",
            secondaryTag = "1.000 Ngày",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuANA2ChG6LS0d4msPLYL4g-4W2BU_q52b1udp8NDaY4NJSzyw4NZnx6e2qKT1oMKzYrc76_1-nMndDIrMSO7k1QXvz66V8WEt7D3GuZmigotLqTpeJbbAdYrKyOPyUV1W-RxHRZbCo09c24vQC-5ZIS2iG1PM6s7V5_nejLv9V0-tTQujYKsbrgGRbfxlS_JvPgXqa_zXWfABTSL3rCM-VVaw2iIyPJ43vA8jK5bjWCrVYznx4hzUS6_w",
            daysRemaining = 3,
            isPast = false,
            progressPercent = 99.7f,
            isImportant = true,
            notificationEnabled = true
          ),
          MilestoneEntity(
            id = 2,
            title = "Sinh nhật em bé Khánh Linh",
            dateText = "24 Tháng 10, 2026",
            subtitle = "Thứ Bảy",
            categoryTag = "Sinh Nhật Em Bé",
            secondaryTag = "Tròn 23 Tuổi",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD4lUgVwSAdUWsFsi52Rfb9gVlLkWItugcu1E3ZPNu03JcMHYPOLpOx6PQQJBewf11C9EMcYfgVWEv2Y9xlYJdb74JrdBC6LXJJhCz-j_2HlOWzDGliI0zrBqPMFkDD18FyjhoCiA4Z0dPxY3TjGfFYojZAat5iyxhFvpohFryyc2NqLelFCjpec141mHem7Mt4dlPP65lwsYqxXD99_hGjIIth6O15Kttj2sJUKnCfQf7aBnZLg_i5zA",
            daysRemaining = 46,
            isPast = false,
            progressPercent = null,
            isImportant = false,
            notificationEnabled = true
          ),
          MilestoneEntity(
            id = 3,
            title = "Chuyến du lịch Sa Pa đầu tiên",
            dateText = "15 Tháng 12, 2025",
            subtitle = "12 Khoảnh Khắc",
            categoryTag = "Chuyến Đi Đầu Tiên",
            secondaryTag = "Kỷ Niệm Đẹp",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAmf1pfH_ZiuAggP1wth3a6EYHMA9l5Who3A4XNQWzhq7dSbED2mOvatljf5a14bhi2Mf51_bgzzYXGbUcaQ3YaXsU5eDF5uDgtZtC1ilFiXEV0KAihzyhu2Li67tO3-0K6A6ws16FB9HggBFgulri1kwDAccX5uYslgC8LZJttfH1vlN-GObeCk4FkpBUBN5Cw85bh1EW5zxrFUr4Jttan9L0nNfx-nOr5KYnIaf1WP-Vuf0_dcEOoPQ",
            daysRemaining = -268,
            isPast = true,
            progressPercent = null,
            isImportant = false,
            notificationEnabled = false,
            isSaved = true
          ),
          MilestoneEntity(
            id = 4,
            title = "Kỷ niệm 3 năm ngày ngỏ lời yêu",
            dateText = "14 Tháng 02, 2027",
            subtitle = "Valentine",
            categoryTag = "3 Năm Tình Yêu",
            secondaryTag = "Lễ Tình Nhân",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuByv5sl1in8xcVcb5KMOzH0JRlpH-JA-IxBCpv_XkZHLYcdN9FyfgRIH_CmPiynqsTI3Gsfl6at8KfpXpokLjZ-FFiEfQ51M9yotGHpw8yV6aw0tDhSJyljTrN08iN_1Pl_prKyTleVt8t2Zh6_1NexbXOYRXFtz-SfJAxjwv-lLB_3k_b-Y7y0bDqQPCvBs5V1RnjMjzfn0Jm0BrF5s-AC5zQs6vBcVFd0wqmYLnbKzIFBa0jAtGvRNQ",
            daysRemaining = 159,
            isPast = false,
            progressPercent = null,
            isImportant = false,
            notificationEnabled = false
          )
        )
      )
    }

    val currentGifts = dao.getAllGiftIdeas().first()
    if (currentGifts.isEmpty()) {
      dao.insertGiftIdeas(
        listOf(
          GiftIdeaEntity(
            id = 1,
            title = "Hộp Album Ảnh Kỷ Niệm Handmade DIY",
            category = "Kỷ vật Handmade",
            badgeText = "98% Cặp đôi mê mẩn",
            tag = "Dễ làm",
            description = "Lưu giữ trọn vẹn 1.000 ngày bằng những trang ảnh kỷ niệm tự tay dán và trang trí.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuAGsR-R8haSfzCr9i2jWVyf33DkMnNdQLsxyKlu-ZqMRMs6BfWsg33hflHKhbXJyPu8SSPIc99etS-THI47QyHOi_lvNPiNkxwLJaxSS-hO1W1lGOyVgreZcWLIFf28yRDUSD7UJAUQ5dYEc92klFN9yfmWJRC-Pidx3guEWuU_brjx4V5cCIAHPwXWemM5Oz-1rp_0wIc6NuSvFmix92SMvBTWswibb9bs7l7AtkuNqAd8_IxUB9f9sQ",
            isFavorited = false,
            detailsSnippet = "In 20 tấm ảnh đẹp nhất & viết lời chúc kỷ niệm",
            actionText = "Chọn 20 ảnh từ thư viện kỷ niệm"
          ),
          GiftIdeaEntity(
            id = 2,
            title = "Dây Chuyền Bạc Khắc Ngày Gặp (11.09)",
            category = "Trang sức & Nước hoa",
            badgeText = "Tinh tế & Sang trọng",
            tag = "Ý nghĩa",
            description = "Món trang sức nhỏ nhắn khắc ngày đầu tiên rung động, đồng hành cùng người ấy mỗi ngày.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBAUhTdr8AeIajgHNMBBWEPZ_uJ8bgZp_3I65hiteDkMxkg6Ka2A_ukpEAGxoNxgoeqhlwe3TLmsjQ9oOo4u6AQC2L9WRKybZrMZN4eVTUMk9D1JWQ91rnRgewZqZV8-a4RRRQuuy_RzZXnAcm0FNcSpI4SVVCJa1XRmuO5FBCxqN8d5hGOxGWrqZ7we7oAZ8r-1Gy1oCeIFyYOf3efIXwsDUB40jxhAJS-qswCsoAdR23didXgqeX-XQ",
            isFavorited = false,
            detailsSnippet = "Thời gian khắc & giao: Khoảng 1 - 2 ngày (Nên đặt ngay)",
            actionText = "Xem mẫu font chữ & bản thảo khắc"
          ),
          GiftIdeaEntity(
            id = 3,
            title = "Bữa Tối Nến Tự Nấu Tại Nhà",
            category = "Quà lãng mạn",
            badgeText = "Riêng tư & Ấm cúng",
            tag = "Lãng mạn",
            description = "Tự tay chuẩn bị thực đơn đặc biệt riêng tư, chỉ có hai người và bản tình ca du dương.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBlK_9my8DoTkktJjvS5dkcQOk7Tey_g8t418cr6YolIQKcs74335ilX6piWfMvcsdDJeQdRVSSpuwXHwvZSUsA6LHRwWcPPnMsuwX7EjPP72JB0nnMuadOlFJNhfFrMyLGgXtUWWaggEu-1FO3IMNUn7O9debYyXBFyKUuyRm0BQdQoCPu1BMBu1aVu4Q0GIR-ZxDnwiji3jfJwAAdhDXasQ8HUTgFyvk7Bj_xpgJjR-jkuQtFg6e1Kg",
            isFavorited = false,
            detailsSnippet = "🥩 Bò Steak Thăn Nội • 🍷 Rượu Vang Hồng • 🎂 Bánh Kem Trái Tim",
            actionText = "Xem công thức nấu & Danh sách nguyên liệu"
          ),
          GiftIdeaEntity(
            id = 4,
            title = "Vé Cắm Trại Ngắm Hoàng Hôn Cuối Tuần",
            category = "Địa điểm hẹn hò",
            badgeText = "Trải nghiệm mới lạ",
            tag = "Dã ngoại",
            description = "Rời xa phố thị ồn ào để cùng nhau tận hưởng bầu trời hoàng hôn rực rỡ và đếm sao đêm.",
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuD3K3tw6P5ntj7ear4yLPsbBrwsLEpDjRuvAvqh_z6W-AktXn0P_aklMHQ2HJ1b_DnK5eULjwYPf1Vrx887WXNNGLYBYrwuS4YuH6K6lz-6qfFly2DqdttW3iCKBlW2tmmVcIlRO5fIVDICCCKgCBAlJGZvh5SjrflTHwEYgLJdnLZWl6c2zJ2rpqRRll360TPboHuzep0Jpi4bkppkg9NGhicbopTUFkA_kN6q152F-D8VexLIF6pwbg",
            isFavorited = false,
            detailsSnippet = "Đồi thông ngoại ô (cách trung tâm 45 phút lái xe)",
            actionText = "Xem địa điểm Glamping gợi ý"
          )
        )
      )
    }

    val currentChecklist = dao.getAllChecklistItems().first()
    if (currentChecklist.isEmpty()) {
      dao.insertChecklistItems(
        listOf(
          ChecklistItemEntity(id = 1, text = "Đặt bánh kem mini vị dâu bento", iconName = "cake", isCompleted = true),
          ChecklistItemEntity(id = 2, text = "Viết thư tay thổ lộ tình cảm", iconName = "history_edu", isCompleted = false),
          ChecklistItemEntity(id = 3, text = "Đặt trước bàn tại nhà hàng hoa hồng", iconName = "table_restaurant", isCompleted = false)
        )
      )
    }

    val currentCadences = dao.getAllReminderCadences().first()
    if (currentCadences.isEmpty()) {
      dao.insertReminderCadences(
        listOf(
          ReminderCadenceEntity(key = "7_days", label = "Trước 7 ngày", isEnabled = true),
          ReminderCadenceEntity(key = "3_days", label = "Trước 3 ngày", isEnabled = true),
          ReminderCadenceEntity(key = "1_day", label = "Trước 1 ngày", isEnabled = true),
          ReminderCadenceEntity(key = "exact_day", label = "00:00 Ngày lễ", isEnabled = true)
        )
      )
    }

    val currentReminders = dao.getAllCustomReminders().first()
    if (currentReminders.isEmpty()) {
      dao.insertCustomReminders(
        listOf(
          CustomReminderEntity(
            id = 1,
            title = "Hẹn xem phim cuối tuần",
            dateText = "Thứ Bảy, 19:30",
            details = "CGV Landmark 81",
            daysRemainingText = "Sau 2 ngày",
            iconType = "movie"
          ),
          CustomReminderEntity(
            id = 2,
            title = "Kỷ niệm cái nắm tay đầu tiên",
            dateText = "05/10",
            details = "Hồ Tây mùa hoa sữa",
            daysRemainingText = "Sau 27 ngày",
            iconType = "hand"
          )
        )
      )
    }

    val currentProfile = dao.getCoupleProfile().first()
    if (currentProfile == null) {
      dao.insertCoupleProfile(
        CoupleProfileEntity(
          id = 1,
          partner1Name = "Mhoang",
          partner1Birthday = "15/10/2004",
          partner1ProfilePicture = "https://lh3.googleusercontent.com/aida-public/AB6AXuCg-PmA8kAH3aEsx4nS5akuDkkWQeWMutmW8Lc76ASO-JMvtiwMNbsTfuYqBGpez7bHTAYekQNilJ5X5BHaP78pQf4tATX48UynvtOeQWG8kcCF-v9OqIdcm1OAjLGtZsO1ygTFLVd9qW-yngUnwCmOtlFWn_wzhCPvYfzMcFLVzeEoOX9NuP8fk911cjdlzd0yv0FvSo3h7qg26BWqyaqSpVwTuvKoKaZ20NJLOUWeBfHbRlqMZcIwfI1FL31cw1WGBrQ",
          partner1Age = 20,
          partner1Zodiac = "Thiên Bình",
          partner2Name = "TLinh",
          partner2Birthday = "24/07/2003",
          partner2ProfilePicture = "https://lh3.googleusercontent.com/aida-public/AB6AXuA7FGhAVlT3aua6tzpkBgh-_XXVsY-hCGooIPxOJ0acYyvZV9FZpASsMTgGO0vBFJLrLojRgwwnQEjfWAniwj2FAiKNpUjSRFNIQgmOhVN8fFSYwpOqLF8hAnWk3UM0dWxSVT1FwaK9ovXMP7Jwi-gjfRbUm2ISX1qyUY6bpBKBZnBYk7YIorizTPWI5c6w9XUdTTsMLFWT3c5ns8lYQooC0eEvt6S5zJNNobW_pn1PTJOb0K2REgdfTQ",
          partner2Age = 21,
          partner2Zodiac = "Cự Giải",
          loveTitle = "Bámmmm",
          loveDays = 1349,
          anniversaryDate = "18/12/2022"
        )
      )
    }

    val currentMemories = dao.getAllSharedMemories().first()
    if (currentMemories.isEmpty()) {
      dao.insertSharedMemories(
        listOf(
          SharedMemoryEntity(
            id = 1,
            title = "Cái Nắm Tay Đầu Tiên Dưới Phố",
            dateText = "18 Tháng 12, 2022",
            note = "Buổi tối mùa đông se lạnh đầu tiên hai đứa cùng dạo phố đi bộ, tay em bé lạnh ngắt được anh ủ ấm trong túi áo.",
            photoUri = "https://images.unsplash.com/photo-1516589178581-6cd7833ae3b2?q=80&w=1080&auto=format&fit=crop",
            location = "Hồ Gươm, Hà Nội",
            isFavorite = true
          ),
          SharedMemoryEntity(
            id = 2,
            title = "Chuyến Du Lịch Sa Pa Mùa Sương Mù",
            dateText = "15 Tháng 12, 2025",
            note = "Đỉnh Fansipan lộng gió và biển mây bồng bềnh, cùng nhau thưởng thức nồi lẩu cá hồi nóng hổi.",
            photoUri = "https://images.unsplash.com/photo-1518199266791-5375a83190b7?q=80&w=1080&auto=format&fit=crop",
            location = "Sa Pa, Lào Cai",
            isFavorite = true
          ),
          SharedMemoryEntity(
            id = 3,
            title = "Bữa Tối Nến Lãng Mạn Kỷ Niệm 1 Năm",
            dateText = "18 Tháng 12, 2023",
            note = "Anh bí mật tự tay chuẩn bị bò bít tết và bánh kem trái tim, em bất ngờ đến rơi nước mắt.",
            photoUri = "https://images.unsplash.com/photo-1529636798458-92182e662485?q=80&w=1080&auto=format&fit=crop",
            location = "Tổ ấm nhỏ của hai đứa",
            isFavorite = false
          ),
          SharedMemoryEntity(
            id = 4,
            title = "Hoàng Hôn Trên Bờ Biển Nha Trang",
            dateText = "20 Tháng 06, 2024",
            note = "Nắng vàng nhuộm đỏ mặt biển, hai đứa cùng nhặt vỏ ốc và nghe tiếng sóng vỗ rì rào bên tai.",
            photoUri = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=1080&auto=format&fit=crop",
            location = "Bãi Dài, Nha Trang",
            isFavorite = true
          ),
          SharedMemoryEntity(
            id = 5,
            title = "Buổi Xem Phim & Cà Phê Mưa Cuối Tuần",
            dateText = "14 Tháng 02, 2025",
            note = "Trời mưa lất phất bên hiên quán nhỏ quen thuộc, cùng chia nhau một ly matcha latte ấm.",
            photoUri = "https://images.unsplash.com/photo-1511285560929-80b456fea0bc?q=80&w=1080&auto=format&fit=crop",
            location = "Góc Cafe Tầng Thượng",
            isFavorite = false
          ),
          SharedMemoryEntity(
            id = 6,
            title = "Kỷ Niệm Sinh Nhật Em Bé Tròn 22 Tuổi",
            dateText = "24 Tháng 07, 2025",
            note = "Nụ cười rạng rỡ của công chúa nhỏ khi thổi nến và ước nguyện điều ước tình yêu cho cả hai.",
            photoUri = "https://images.unsplash.com/photo-1464349095431-e9a21285b5f3?q=80&w=1080&auto=format&fit=crop",
            location = "Nhà hàng hoa hồng",
            isFavorite = true
          )
        )
      )
    }
  }

  suspend fun saveCoupleProfile(profile: CoupleProfileEntity) {
    dao.insertCoupleProfile(profile.copy(id = 1, updatedAt = System.currentTimeMillis()))
  }

  suspend fun toggleChecklistItem(item: ChecklistItemEntity) {
    dao.updateChecklistItem(item.copy(isCompleted = !item.isCompleted))
  }

  suspend fun addChecklistItem(text: String, icon: String = "check") {
    dao.insertChecklistItem(ChecklistItemEntity(text = text, iconName = icon, isCompleted = false))
  }

  suspend fun toggleGiftFavorite(item: GiftIdeaEntity) {
    dao.updateGiftIdea(item.copy(isFavorited = !item.isFavorited))
  }

  suspend fun toggleCadence(item: ReminderCadenceEntity) {
    dao.updateReminderCadence(item.copy(isEnabled = !item.isEnabled))
  }

  suspend fun addCustomReminder(
    title: String,
    dateText: String,
    note: String,
    alarmMillis: Long? = null,
    alarmFormatted: String = ""
  ): Long {
    return dao.insertCustomReminder(
      CustomReminderEntity(
        title = title,
        dateText = dateText,
        details = if (note.isNotBlank()) note else "Kỷ niệm ngọt ngào",
        daysRemainingText = "Mới tạo",
        iconType = "stars",
        alarmTimeMillis = alarmMillis,
        alarmTimeFormatted = alarmFormatted
      )
    )
  }

  suspend fun deleteCustomReminder(id: Long) {
    dao.deleteCustomReminderById(id)
  }

  suspend fun deleteMilestone(id: Long) {
    dao.deleteMilestoneById(id)
  }

  suspend fun deleteChecklistItem(id: Long) {
    dao.deleteChecklistItemById(id)
  }

  suspend fun updateMilestone(item: MilestoneEntity) {
    dao.updateMilestone(item)
  }

  suspend fun addMilestone(item: MilestoneEntity) {
    dao.insertMilestone(item)
  }

  suspend fun toggleMilestoneNotification(item: MilestoneEntity) {
    dao.updateMilestone(item.copy(notificationEnabled = !item.notificationEnabled))
  }

  suspend fun addSharedMemory(
    title: String,
    dateText: String,
    photoUri: String,
    note: String = "",
    location: String = ""
  ) {
    dao.insertSharedMemory(
      SharedMemoryEntity(
        title = title,
        dateText = dateText,
        photoUri = photoUri,
        note = note,
        location = location,
        isFavorite = false,
        createdAt = System.currentTimeMillis()
      )
    )
  }

  suspend fun deleteSharedMemory(id: Long) {
    dao.deleteSharedMemoryById(id)
  }

  suspend fun toggleMemoryFavorite(memory: SharedMemoryEntity) {
    dao.updateSharedMemory(memory.copy(isFavorite = !memory.isFavorite))
  }
}
