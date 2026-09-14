package com.example.data.repository

import com.example.data.db.InLoveDao
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
import kotlinx.coroutines.flow.first

class InLoveRepository(private val dao: InLoveDao) {

  val milestones: Flow<List<MilestoneEntity>> = dao.getAllMilestones()
  val giftIdeas: Flow<List<GiftIdeaEntity>> = dao.getAllGiftIdeas()
  val checklistItems: Flow<List<ChecklistItemEntity>> = dao.getAllChecklistItems()
  val customReminders: Flow<List<CustomReminderEntity>> = dao.getAllCustomReminders()
  val reminderCadences: Flow<List<ReminderCadenceEntity>> = dao.getAllReminderCadences()
  val coupleProfile: Flow<CoupleProfileEntity?> = dao.getCoupleProfile()
  val sharedMemories: Flow<List<SharedMemoryEntity>> = dao.getAllSharedMemories()
  val loveBadges: Flow<List<LoveBadgeEntity>> = dao.getAllLoveBadges()
  val anniversaryDates: Flow<List<AnniversaryDateEntity>> = dao.getAllAnniversaryDates()
  val giftReminders: Flow<List<GiftReminderEntity>> = dao.getAllGiftReminders()

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
            isFavorite = true,
            anniversaryTitle = "18/12 - Ngày Bắt Đầu Yêu"
          ),
          SharedMemoryEntity(
            id = 2,
            title = "Chuyến Du Lịch Sa Pa Mùa Sương Mù",
            dateText = "15 Tháng 12, 2025",
            note = "Đỉnh Fansipan lộng gió và biển mây bồng bềnh, cùng nhau thưởng thức nồi lẩu cá hồi nóng hổi.",
            photoUri = "https://images.unsplash.com/photo-1518199266791-5375a83190b7?q=80&w=1080&auto=format&fit=crop",
            location = "Sa Pa, Lào Cai",
            isFavorite = true,
            anniversaryTitle = "Chuyến Du Lịch Đầu Tiên"
          ),
          SharedMemoryEntity(
            id = 3,
            title = "Bữa Tối Nến Lãng Mạn Kỷ Niệm 1 Năm",
            dateText = "18 Tháng 12, 2023",
            note = "Anh bí mật tự tay chuẩn bị bò bít tết và bánh kem trái tim, em bất ngờ đến rơi nước mắt.",
            photoUri = "https://images.unsplash.com/photo-1529636798458-92182e662485?q=80&w=1080&auto=format&fit=crop",
            location = "Tổ ấm nhỏ của hai đứa",
            isFavorite = false,
            anniversaryTitle = "Kỷ Niệm 1 Năm Hoàng Kim"
          ),
          SharedMemoryEntity(
            id = 4,
            title = "Hoàng Hôn Trên Bờ Biển Nha Trang",
            dateText = "20 Tháng 06, 2024",
            note = "Nắng vàng nhuộm đỏ mặt biển, hai đứa cùng nhặt vỏ ốc và nghe tiếng sóng vỗ rì rào bên tai.",
            photoUri = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=1080&auto=format&fit=crop",
            location = "Bãi Dài, Nha Trang",
            isFavorite = true,
            anniversaryTitle = "100 Ngày Bên Nhau"
          ),
          SharedMemoryEntity(
            id = 5,
            title = "Buổi Xem Phim & Cà Phê Mưa Cuối Tuần",
            dateText = "14 Tháng 02, 2025",
            note = "Trời mưa lất phất bên hiên quán nhỏ quen thuộc, cùng chia nhau một ly matcha latte ấm.",
            photoUri = "https://images.unsplash.com/photo-1511285560929-80b456fea0bc?q=80&w=1080&auto=format&fit=crop",
            location = "Góc Cafe Tầng Thượng",
            isFavorite = false,
            anniversaryTitle = "14/02 - Lễ Tình Nhân Valentine"
          ),
          SharedMemoryEntity(
            id = 6,
            title = "Kỷ Niệm Sinh Nhật Em Bé Tròn 22 Tuổi",
            dateText = "24 Tháng 07, 2025",
            note = "Nụ cười rạng rỡ của công chúa nhỏ khi thổi nến và ước nguyện điều ước tình yêu cho cả hai.",
            photoUri = "https://images.unsplash.com/photo-1464349095431-e9a21285b5f3?q=80&w=1080&auto=format&fit=crop",
            location = "Nhà hàng hoa hồng",
            isFavorite = true,
            anniversaryTitle = "24/07 - Sinh Nhật TLinh"
          )
        )
      )
    }

    val currentBadges = dao.getAllLoveBadges().first()
    if (currentBadges.isEmpty()) {
      dao.insertLoveBadges(
        listOf(
          LoveBadgeEntity(
            id = "badge_7_days",
            targetDays = 7,
            titleVi = "Hạt Mầm Tình Yêu",
            titleEn = "First Spark",
            descVi = "1 tuần đầu tiên chính thức cùng nhau bước vào thế giới tình yêu ngọt ngào.",
            descEn = "First 7 days together into the magical journey of romance.",
            tier = "BRONZE",
            iconType = "sprout_heart",
            rewardQuoteVi = "Tình yêu bắt đầu từ một ánh mắt, nảy mầm qua từng ngày bên em.",
            rewardQuoteEn = "Love begins with a glance and sprouts with every sweet day.",
            isClaimed = true,
            claimedTimestamp = 1702944000000L
          ),
          LoveBadgeEntity(
            id = "badge_30_days",
            targetDays = 30,
            titleVi = "Ánh Trăng Tình Đầu",
            titleEn = "Moonlight Lovers",
            descVi = "Tròn 1 tháng đầu tiên với những rung động ngọt ngào và đáng nhớ nhất.",
            descEn = "First month milestone filled with sweetest first memories.",
            tier = "BRONZE",
            iconType = "moon_heart",
            rewardQuoteVi = "Tròn một tháng bên nhau, trăng tròn như tình yêu anh dành cho em.",
            rewardQuoteEn = "A full month together, shining as bright as moonlight.",
            isClaimed = true,
            claimedTimestamp = 1704931200000L
          ),
          LoveBadgeEntity(
            id = "badge_100_days",
            targetDays = 100,
            titleVi = "Bách Nhật Gắn Kết",
            titleEn = "Centurial Romance",
            descVi = "Mốc 100 ngày kỷ niệm ngọt ngào, bền bỉ và tràn ngập niềm vui.",
            descEn = "100 golden days of companionship and growing affection.",
            tier = "SILVER",
            iconType = "rose_heart",
            rewardQuoteVi = "100 ngày trôi qua, nụ cười của em vẫn là điều anh say đắm nhất.",
            rewardQuoteEn = "100 days together and your smile is still my brightest light.",
            isClaimed = true,
            claimedTimestamp = 1711152000000L
          ),
          LoveBadgeEntity(
            id = "badge_200_days",
            targetDays = 200,
            titleVi = "Mùa Hoa Nở Rộ",
            titleEn = "Blossoming Love",
            descVi = "200 ngày thấu hiểu, sẻ chia và luôn có nhau trong mọi thăng trầm.",
            descEn = "200 days of deep understanding, harmony, and joy.",
            tier = "SILVER",
            iconType = "blossom_heart",
            rewardQuoteVi = "Tình yêu của đôi ta như hoa xuân nở rộ, ngát hương qua từng năm tháng.",
            rewardQuoteEn = "Our love blossoms like springtime flowers across the seasons.",
            isClaimed = true,
            claimedTimestamp = 1719792000000L
          ),
          LoveBadgeEntity(
            id = "badge_365_days",
            targetDays = 365,
            titleVi = "Một Năm Vẹn Tròn",
            titleEn = "Golden First Year",
            descVi = "Tròn một năm bốn mùa xuân hạ thu đông trọn vẹn yêu thương.",
            descEn = "One full year of four seasons, unwavering love and joy.",
            tier = "GOLD",
            iconType = "crown_heart",
            rewardQuoteVi = "Một năm bốn mùa trôi qua, tình yêu ta càng thêm đậm sâu và bền chặt.",
            rewardQuoteEn = "Four seasons in one year, every day in love with you.",
            isClaimed = true,
            claimedTimestamp = 1734480000000L
          ),
          LoveBadgeEntity(
            id = "badge_500_days",
            targetDays = 500,
            titleVi = "Trái Tim Pha Lê",
            titleEn = "Crystal Bond",
            descVi = "500 ngày tình yêu trong sáng, kiên định và vững chãi.",
            descEn = "500 days of pure, resilient love and mutual trust.",
            tier = "GOLD",
            iconType = "crystal_heart",
            rewardQuoteVi = "500 ngày qua, tình yêu trong veo như pha lê và bền chặt theo năm tháng.",
            rewardQuoteEn = "500 days of crystal clarity and heartfelt devotion.",
            isClaimed = true,
            claimedTimestamp = 1746144000000L
          ),
          LoveBadgeEntity(
            id = "badge_730_days",
            targetDays = 730,
            titleVi = "Hai Năm Chung Đôi",
            titleEn = "Two Years Harmony",
            descVi = "Tròn 2 năm đồng hành, cùng nắm tay xây dựng tương lai tươi đẹp.",
            descEn = "Two beautiful years hand in hand through every adventure.",
            tier = "RUBY",
            iconType = "ring_heart",
            rewardQuoteVi = "Hai năm đồng hành, hai trái tim cùng chung một nhịp đập son sắt.",
            rewardQuoteEn = "Two years together, two souls beating as one.",
            isClaimed = true,
            claimedTimestamp = 1766016000000L
          ),
          LoveBadgeEntity(
            id = "badge_1000_days",
            targetDays = 1000,
            titleVi = "Thiên Nhật Thủy Chung",
            titleEn = "Millennium of Love",
            descVi = "1.000 ngày son sắt - Cột mốc vĩ đại chứng minh tình yêu không đổi thay.",
            descEn = "1,000 days of steadfast devotion - an epic milestone.",
            tier = "RUBY",
            iconType = "trophy_heart",
            rewardQuoteVi = "Một ngàn ngày bên nhau, ngàn lời yêu thương vẫn luôn mới như ngày đầu.",
            rewardQuoteEn = "A thousand days together, each feeling as magical as day one.",
            isClaimed = true,
            claimedTimestamp = 1789344000000L
          ),
          LoveBadgeEntity(
            id = "badge_1349_days",
            targetDays = 1349,
            titleVi = "Ngọn Lửa Hiện Tại",
            titleEn = "Passionate Flame",
            descVi = "Cột mốc kỳ diệu đánh dấu hành trình rực rỡ và đong đầy đến hôm nay.",
            descEn = "A special milestone marking your radiant journey up to today.",
            tier = "DIAMOND",
            iconType = "flame_heart",
            rewardQuoteVi = "Mỗi ngày trôi qua bên em đều là một kỳ tích ngọt ngào đáng trân trọng.",
            rewardQuoteEn = "Every day with you is a cherished, radiant blessing.",
            isClaimed = true,
            claimedTimestamp = System.currentTimeMillis()
          ),
          LoveBadgeEntity(
            id = "badge_1500_days",
            targetDays = 1500,
            titleVi = "Dải Ngân Hà Yêu Thương",
            titleEn = "Cosmic Aurora",
            descVi = "1.500 ngày tựa bầu trời sao lấp lánh soi sáng mọi nẻo đường hạnh phúc.",
            descEn = "1,500 days like a galaxy of sparkling starlight guiding your joy.",
            tier = "DIAMOND",
            iconType = "galaxy_heart",
            rewardQuoteVi = "Tình yêu chúng ta bao la như ngân hà, lung linh từng ánh sao hạnh phúc.",
            rewardQuoteEn = "Our love shines as vast and luminous as the starlit cosmos.",
            isClaimed = false
          ),
          LoveBadgeEntity(
            id = "badge_1825_days",
            targetDays = 1825,
            titleVi = "Nửa Thập Kỷ Sắt Son",
            titleEn = "Half-Decade Devotion",
            descVi = "Tròn 5 năm - Nửa thập kỷ cùng sẻ chia mái ấm và ước mơ tương lai.",
            descEn = "Five magnificent years building a lifetime of memories together.",
            tier = "COSMIC",
            iconType = "infinity_heart",
            rewardQuoteVi = "Nửa thập kỷ đã qua, nhưng tình yêu anh dành cho em chỉ mới bắt đầu.",
            rewardQuoteEn = "Half a decade together, and yet forever has only just begun.",
            isClaimed = false
          ),
          LoveBadgeEntity(
            id = "badge_3650_days",
            targetDays = 3650,
            titleVi = "Thập Kỷ Kim Cương",
            titleEn = "Decade of Eternity",
            descVi = "10 năm vàng son - Tượng đài tình yêu vĩnh cửu bất diệt cùng thời gian.",
            descEn = "10 years of eternal, unbreakable diamond love lasting forever.",
            tier = "COSMIC",
            iconType = "eternity_diamond",
            rewardQuoteVi = "Mười năm một chặng đường, trăm năm một chữ tình son sắt đến muôn đời.",
            rewardQuoteEn = "A decade of devoted bliss, a lifetime of eternal devotion.",
            isClaimed = false
          )
        )
      )
    }

    val currentAnniversaries = dao.getAllAnniversaryDates().first()
    if (currentAnniversaries.isEmpty()) {
      dao.insertAnniversaryDates(
        listOf(
          AnniversaryDateEntity(
            id = 1,
            title = "Ngày Chính Thức Yêu Nhau",
            dateText = "18/12/2022",
            type = "LOVE",
            description = "Cái nắm tay đầu tiên dưới phố mùa đông se lạnh và lời tỏ tình ngọt ngào",
            isAnnual = true,
            notificationEnabled = true,
            reminderDaysBefore = 7,
            daysRemaining = 97
          ),
          AnniversaryDateEntity(
            id = 2,
            title = "Buổi Hẹn Đầu Tiên",
            dateText = "15/10/2022",
            type = "FIRST_DATE",
            description = "Quán cà phê nhỏ tầng thượng ngắm hoàng hôn đỏ rực cả bầu trời",
            isAnnual = true,
            notificationEnabled = true,
            reminderDaysBefore = 3,
            daysRemaining = 33
          ),
          AnniversaryDateEntity(
            id = 3,
            title = "Nụ Hôn Đầu Tiên",
            dateText = "01/01/2023",
            type = "FIRST_KISS",
            description = "Đêm giao thừa đếm ngược ngắm pháo hoa lung linh bên bờ hồ",
            isAnnual = true,
            notificationEnabled = true,
            reminderDaysBefore = 3,
            daysRemaining = 111
          ),
          AnniversaryDateEntity(
            id = 4,
            title = "Chuyến Du Lịch Sa Pa Đầu Tiên",
            dateText = "15/12/2025",
            type = "CUSTOM",
            description = "Cùng săn mây Fansipan và thưởng thức lẩu cá hồi ấm áp",
            isAnnual = true,
            notificationEnabled = true,
            reminderDaysBefore = 5,
            daysRemaining = 94
          )
        )
      )
    }

    val currentGiftReminders = dao.getAllGiftReminders().first()
    if (currentGiftReminders.isEmpty()) {
      dao.insertGiftReminders(
        listOf(
          GiftReminderEntity(
            id = 1,
            title = "Bó hoa hồng vĩnh cửu & Thiệp viết tay",
            recipient = "TLinh",
            occasion = "Kỷ niệm 1.000 ngày bên nhau",
            dueDateText = "11 Tháng 9, 2026",
            estimatedBudget = "450.000đ",
            notes = "Viết 10 điều anh yêu nhất ở em trong tấm thiệp hồng",
            isCompleted = false
          ),
          GiftReminderEntity(
            id = 2,
            title = "Dây chuyền bạc khắc ngày gặp 11.09",
            recipient = "Em bé Linh",
            occasion = "Cột mốc 1.000 ngày",
            dueDateText = "10 Tháng 9, 2026",
            estimatedBudget = "650.000đ",
            notes = "Nhắc tiệm bạc hoàn thành khắc sớm và đóng gói hộp nơ nhung đỏ",
            isCompleted = true
          ),
          GiftReminderEntity(
            id = 3,
            title = "Bánh kem mini trái tim vị dâu tây",
            recipient = "TLinh",
            occasion = "Kỷ niệm ngày yêu",
            dueDateText = "11 Tháng 9, 2026",
            estimatedBudget = "220.000đ",
            notes = "Tiệm bánh bento gần nhà, dặn làm kem ít ngọt và cắm nến số 1000",
            isCompleted = false
          )
        )
      )
    }
  }

  suspend fun claimLoveBadge(badgeId: String, customNote: String = "") {
    val currentBadges = dao.getAllLoveBadges().first()
    val badge = currentBadges.find { it.id == badgeId }
    if (badge != null) {
      dao.updateLoveBadge(
        badge.copy(
          isClaimed = true,
          claimedTimestamp = System.currentTimeMillis(),
          customNote = customNote.ifEmpty { badge.customNote }
        )
      )
    }
  }

  suspend fun updateLoveBadge(badge: LoveBadgeEntity) {
    dao.updateLoveBadge(badge)
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

  suspend fun addMilestone(item: MilestoneEntity): Long {
    return dao.insertMilestone(item)
  }

  suspend fun toggleMilestoneNotification(item: MilestoneEntity) {
    dao.updateMilestone(item.copy(notificationEnabled = !item.notificationEnabled))
  }

  suspend fun addSharedMemory(
    title: String,
    dateText: String,
    photoUri: String,
    note: String = "",
    location: String = "",
    anniversaryTitle: String = "18/12 - Ngày Yêu Nhau",
    authorId: String = "",
    authorName: String = "Bạn",
    mediaType: String = "IMAGE",
    videoUri: String? = null,
    cloudinaryPublicId: String? = null,
    cloudinaryUrl: String? = null,
    isCloudinaryStored: Boolean = true,
    fileSizeFormatted: String = "",
    durationSeconds: Int = 0,
    privacyLevel: String = "COUPLE_ONLY"
  ) {
    dao.insertSharedMemory(
      SharedMemoryEntity(
        title = title,
        dateText = dateText,
        photoUri = photoUri,
        note = note,
        location = location,
        isFavorite = false,
        anniversaryTitle = anniversaryTitle,
        createdAt = System.currentTimeMillis(),
        authorId = authorId,
        authorName = authorName,
        mediaType = mediaType,
        videoUri = videoUri,
        cloudinaryPublicId = cloudinaryPublicId,
        cloudinaryUrl = cloudinaryUrl,
        isCloudinaryStored = isCloudinaryStored,
        fileSizeFormatted = fileSizeFormatted,
        durationSeconds = durationSeconds,
        privacyLevel = privacyLevel
      )
    )
  }

  suspend fun updateSharedMemory(memory: SharedMemoryEntity) {
    dao.updateSharedMemory(memory)
  }

  suspend fun deleteSharedMemory(id: Long) {
    dao.deleteSharedMemoryById(id)
  }

  suspend fun toggleMemoryFavorite(memory: SharedMemoryEntity) {
    dao.updateSharedMemory(memory.copy(isFavorite = !memory.isFavorite))
  }

  // Anniversary Dates CRUD
  suspend fun addAnniversaryDate(
    title: String,
    dateText: String,
    type: String = "LOVE",
    description: String = "",
    isAnnual: Boolean = true,
    reminderDaysBefore: Int = 3,
    daysRemaining: Int = 0
  ): Long {
    return dao.insertAnniversaryDate(
      AnniversaryDateEntity(
        title = title,
        dateText = dateText,
        type = type,
        description = description,
        isAnnual = isAnnual,
        notificationEnabled = true,
        reminderDaysBefore = reminderDaysBefore,
        daysRemaining = daysRemaining,
        createdAt = System.currentTimeMillis()
      )
    )
  }

  suspend fun updateAnniversaryDate(item: AnniversaryDateEntity) {
    dao.updateAnniversaryDate(item)
  }

  suspend fun deleteAnniversaryDate(id: Long) {
    dao.deleteAnniversaryDateById(id)
  }

  suspend fun toggleAnniversaryNotification(item: AnniversaryDateEntity) {
    dao.updateAnniversaryDate(item.copy(notificationEnabled = !item.notificationEnabled))
  }

  // Gift Reminders CRUD
  suspend fun addGiftReminder(
    title: String,
    recipient: String,
    occasion: String,
    dueDateText: String,
    estimatedBudget: String,
    notes: String,
    alarmTimeMillis: Long? = null,
    alarmTimeFormatted: String = ""
  ): Long {
    return dao.insertGiftReminder(
      GiftReminderEntity(
        title = title,
        recipient = recipient,
        occasion = occasion,
        dueDateText = dueDateText,
        estimatedBudget = estimatedBudget,
        notes = notes,
        isCompleted = false,
        alarmTimeMillis = alarmTimeMillis,
        alarmTimeFormatted = alarmTimeFormatted,
        createdAt = System.currentTimeMillis()
      )
    )
  }

  suspend fun updateGiftReminder(item: GiftReminderEntity) {
    dao.updateGiftReminder(item)
  }

  suspend fun toggleGiftReminderCompleted(item: GiftReminderEntity) {
    dao.updateGiftReminder(item.copy(isCompleted = !item.isCompleted))
  }

  suspend fun deleteGiftReminder(id: Long) {
    dao.deleteGiftReminderById(id)
  }

  suspend fun getAnniversaryDatesList(): List<AnniversaryDateEntity> = dao.getAnniversaryDatesList()
  suspend fun getMilestonesList(): List<MilestoneEntity> = dao.getMilestonesList()
  suspend fun getCoupleProfileSync(): CoupleProfileEntity? = dao.getCoupleProfileSync()
  suspend fun getCustomRemindersList(): List<CustomReminderEntity> = dao.getCustomRemindersList()
  suspend fun getGiftRemindersList(): List<GiftReminderEntity> = dao.getGiftRemindersList()
}
