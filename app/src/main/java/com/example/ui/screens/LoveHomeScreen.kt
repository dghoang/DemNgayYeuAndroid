package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.components.AnniversaryMemoriesWidget
import com.example.ui.components.LoveDaysCalculatorWidget
import com.example.ui.components.MilestoneBadgeDashboardCard
import com.example.ui.util.AppLanguage
import com.example.ui.util.LocalizedStrings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin
import coil.compose.AsyncImage
import com.example.ui.theme.HotPink
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.RoseGradientEnd
import com.example.ui.theme.RoseGradientMid
import com.example.ui.theme.RoseGradientStart
import com.example.ui.theme.Secondary
import com.example.ui.viewmodel.InLoveViewModel

// High-quality romantic cherry blossom floral background
const val CHERRY_BLOSSOM_BG =
  "https://images.unsplash.com/photo-1522383225653-ed111181a951?q=80&w=1080&auto=format&fit=crop"

@Composable
fun LoveHomeScreen(
  viewModel: InLoveViewModel,
  onNavigateToCalendar: () -> Unit,
  onNavigateToGifts: () -> Unit,
  onNavigateToMemories: () -> Unit = {}
) {
  val boyName by viewModel.boyName.collectAsState()
  val boyBirthDate by viewModel.boyBirthDate.collectAsState()
  val boyAge by viewModel.boyAge.collectAsState()
  val boyZodiac by viewModel.boyZodiac.collectAsState()
  val boyAvatarUrl by viewModel.boyAvatarUrl.collectAsState()

  val girlName by viewModel.girlName.collectAsState()
  val girlBirthDate by viewModel.girlBirthDate.collectAsState()
  val girlAge by viewModel.girlAge.collectAsState()
  val girlZodiac by viewModel.girlZodiac.collectAsState()
  val girlAvatarUrl by viewModel.girlAvatarUrl.collectAsState()

  val loveTitle by viewModel.loveTitle.collectAsState()
  val loveDays by viewModel.loveDays.collectAsState()
  val anniversaryDate by viewModel.anniversaryDate.collectAsState()

  // Online 1-1 Set Love States
  val currentOnlineUser by viewModel.currentOnlineUser.collectAsState()
  val partnerUser by viewModel.partnerOnlineUser.collectAsState()
  val relationshipStatus by viewModel.relationshipStatus.collectAsState()
  val incomingInvite by viewModel.incomingInvite.collectAsState()

  val selectedWallpaperUrl by viewModel.selectedWallpaperUrl.collectAsState()
  val appLanguage by viewModel.appLanguage.collectAsState()
  val strings = LocalizedStrings.get(appLanguage)

  val scrollState = rememberScrollState()

  // Heart pulse animation
  val infiniteTransition = rememberInfiniteTransition(label = "heart_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.18f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "heart_scale"
  )

  // Heartbeat expanding ripple wave 1
  val ripple1Scale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.75f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ripple1_scale"
  )
  val ripple1Alpha by infiniteTransition.animateFloat(
    initialValue = 0.55f,
    targetValue = 0.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ripple1_alpha"
  )

  // Heartbeat expanding ripple wave 2
  val ripple2Scale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.50f,
    animationSpec = infiniteRepeatable(
      animation = tween(1800, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ripple2_scale"
  )
  val ripple2Alpha by infiniteTransition.animateFloat(
    initialValue = 0.45f,
    targetValue = 0.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1800, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ripple2_alpha"
  )

  // Rotating soft aura around the Days in Love circular counter
  val counterAuraAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(12000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "counter_aura_angle"
  )

  // Sweet romantic feedback quotes & reflections
  val loveQuotes = remember(loveDays, boyName, girlName, appLanguage) {
    if (appLanguage == AppLanguage.VI) {
      listOf(
        "Dù mai này cuộc sống có bận rộn đến đâu, anh vẫn luôn chọn yêu em như những ngày đầu tiên 💕",
        "Cảm ơn vì đã cùng anh đi qua $loveDays ngày ngọt ngào, đầy ắp tiếng cười và những cái ôm ấm áp ✨",
        "Tình yêu không phải là tìm một người hoàn hảo, mà là cùng nhau hoàn thiện và bao dung mỗi ngày 🌸",
        "Bên cạnh em, mỗi phút giây bình dị nhất cũng trở thành những kỷ niệm vô giá của cuộc đời anh 💖",
        "Cảm ơn em vì đã là mảnh ghép dịu dàng nhất bước vào thanh xuân của anh 🌷",
        "$loveDays ngày bên nhau không chỉ là một con số, mà là minh chứng cho tình yêu bền chặt của đôi ta 💍"
      )
    } else {
      listOf(
        "No matter how busy life gets, I will always choose to love you like day one 💕",
        "Thank you for walking with me through $loveDays sweet days filled with warm smiles and hugs ✨",
        "Love isn't about finding someone perfect, but growing and cherishing each other every day 🌸",
        "Beside you, even the simplest moments turn into priceless treasures 💖",
        "Thank you for being the sweetest part of my youth 🌷",
        "$loveDays days together is not just a number, but a testament to our everlasting love 💍"
      )
    }
  }
  val currentQuoteIndex = remember { mutableIntStateOf(0) }

  // Calculation for upcoming anniversary milestone
  val nextMilestone = ((loveDays / 100) + 1) * 100
  val daysRemaining = (nextMilestone - loveDays).coerceAtLeast(1)
  val milestoneProgress = ((loveDays % 100).toFloat() / 100f).coerceIn(0.05f, 1f)

  // Fluid UI animations for love days counter and milestone progress
  val animatedLoveDays by animateIntAsState(
    targetValue = loveDays,
    animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
    label = "animated_love_days"
  )
  val animatedMilestoneProgress by animateFloatAsState(
    targetValue = milestoneProgress,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "animated_milestone_progress"
  )

  // Trigger floating heart celebration overlay whenever a milestone anniversary is reached
  val isMilestoneReached = (loveDays > 0 && loveDays % 100 == 0)
  LaunchedEffect(loveDays) {
    if (isMilestoneReached) {
      viewModel.celebrateMilestoneAnniversary(loveDays)
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    // 1. Romantic Background (Dynamic based on selected wallpaper)
    AsyncImage(
      model = selectedWallpaperUrl,
      contentDescription = "Romantic Wallpaper Background",
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )

    // Soft pastel romantic dreamy gradient overlay for delicate contrast
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            listOf(
              Color(0x35FFB6C1), // Soft Pastel Rose Pink
              Color(0x22FFE4E1), // Misty Rose Cream
              Color(0x38F8BBD0), // Soft Strawberry Pastel
              Color(0xF2FFF0F5)  // Lavender Blush Base
            )
          )
        )
    )

    // Custom Love Animation: Ambient Floating Pastel Hearts drifting smoothly across background
    AmbientFloatingHeartsCanvas(
      modifier = Modifier.fillMaxSize()
    )

    // 2. Main Scrollable Content
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .padding(horizontal = 14.dp, vertical = 6.dp)
        .padding(bottom = 90.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Quick Action Bar: Symmetrical, cohesive, soft pastel glassmorphic card
      Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.96f),
        shadowElevation = 5.dp,
        modifier = Modifier
          .fillMaxWidth()
          .border(
            width = 1.2.dp,
            color = Color(0xFFFFC6DB),
            shape = RoundedCornerShape(22.dp)
          )
          .padding(vertical = 2.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // 1. Change Wallpaper
          HomeQuickActionButton(
            icon = Icons.Rounded.Favorite,
            label = strings.actionWallpaper,
            onClick = { viewModel.openWallpaperDialog() },
            testTag = "btn_quick_wallpaper"
          )

          // 2. Capture / Save Memory
          HomeQuickActionButton(
            icon = Icons.Filled.CameraAlt,
            label = strings.actionMemory,
            onClick = { viewModel.openMemoryDialog() },
            testTag = "btn_quick_memory"
          )

          // 3. Edit Partner / Companion Profile (Holding Heart Icon from Extended Icons)
          HomeQuickActionButton(
            icon = Icons.Filled.VolunteerActivism,
            label = strings.actionCouple,
            onClick = { viewModel.openEditCoupleDialog() },
            testTag = "btn_quick_couple"
          )

          // 4. User Guide
          HomeQuickActionButton(
            icon = Icons.Filled.MenuBook,
            label = strings.actionGuide,
            onClick = { viewModel.openGuideDialog() },
            testTag = "btn_quick_guide"
          )

          // 5. Language Switcher (VI / EN)
          HomeQuickActionButton(
            icon = Icons.Filled.Language,
            label = if (appLanguage == AppLanguage.VI) "VI 🇻🇳" else "EN 🇬🇧",
            onClick = { viewModel.openLanguageDialog() },
            testTag = "btn_quick_language"
          )
        }
      }



      // Dynamic Banner 1: Profile Setup Warning (Anonymous State)
      if (!currentOnlineUser.isProfileSetup) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFFFFF3E0),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB74D)),
          shadowElevation = 2.dp,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.openEditProfileDialog() }
            .padding(bottom = 8.dp)
            .testTag("home_anonymous_profile_banner")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = Color(0xFFE65100),
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Hồ sơ của bạn đang là \"Vô danh\"",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFFBF360C)
              )
              Text(
                text = "Bấm vào đây để nhập tên & ngày sinh (tuổi & cung hoàng đạo sẽ tự động tính!)",
                fontSize = 11.sp,
                color = Color.DarkGray
              )
            }
          }
        }
      }

      // Dynamic Banner 2: Incoming Set Love Invite Notice
      if (incomingInvite != null) {
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color(0xFFFFF0F5),
          border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFF4081)),
          shadowElevation = 3.dp,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.openPairingScreen() }
            .padding(bottom = 8.dp)
            .testTag("home_incoming_invite_banner")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Favorite,
              contentDescription = null,
              tint = Color(0xFFE91E63),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "💌 Lời mời kết đôi từ ${incomingInvite!!.effectiveSenderName}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF880E4F)
              )
              Text(
                text = "Bấm để kiểm tra danh tính và ngày yêu trước khi đồng ý 💕",
                fontSize = 11.sp,
                color = Color(0xFFC2185B)
              )
            }
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFE91E63)
            ) {
              Text(
                text = "Xem",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }
      } else if (relationshipStatus != com.example.data.model.OnlineStatus.COUPLED) {
        // Dynamic Banner 3: Prompt to Pair 1-1
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = Color.White.copy(alpha = 0.95f),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFC6DB)),
          shadowElevation = 2.dp,
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.openPairingScreen() }
            .padding(bottom = 8.dp)
            .testTag("home_prompt_pair_banner")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.VolunteerActivism,
              contentDescription = null,
              tint = Color(0xFFE91E63),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Ghép đôi 1-1 (Set Love) để đồng bộ ngày yêu cùng người ấy",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color(0xFF880E4F),
              modifier = Modifier.weight(1f)
            )
            Text(
              text = "Bấm để ghép >",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFE91E63)
            )
          }
        }
      }

      // Romantic Pastel Streak Badge
      Surface(
        shape = RoundedCornerShape(50.dp),
        color = Color(0xFFFFF0F5).copy(alpha = 0.95f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFC6DB)),
        shadowElevation = 3.dp,
        modifier = Modifier.padding(bottom = 6.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Color(0xFFFF4081),
            modifier = Modifier.size(13.dp)
          )
          Text(
            text = "GẮN KẾT YÊU THƯƠNG",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFC2185B),
            letterSpacing = 1.sp
          )
          Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Color(0xFFFF4081),
            modifier = Modifier.size(13.dp)
          )
        }
      }

      // 3. Middle Area: Trái tim kèm theo số ngày yêu nổi bật (Large Circular Counter)
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(240.dp)
          .clickable {
            viewModel.triggerFloatingHearts()
            viewModel.showToast("Bên nhau $loveDays ngày hạnh phúc vô bờ! ❤️")
          }
          .testTag("love_circle_days_counter")
      ) {
        // Glowing animated circular progress ring with soft rotating pastel aura
        Canvas(modifier = Modifier.fillMaxSize()) {
          val strokeWidth = 6.dp.toPx()
          // Background soft ring
          drawCircle(
            color = Color(0xFFFFE4EE).copy(alpha = 0.85f),
            radius = size.minDimension / 2 - strokeWidth / 2,
            style = Stroke(width = strokeWidth)
          )
          // Glowing rotating soft pastel romantic sweep arc
          drawArc(
            brush = Brush.sweepGradient(
              listOf(
                Color(0xFFFFB6C1),
                Color(0xFFFF4081),
                Color(0xFFFF80AB),
                Color(0xFFF48FB1),
                Color(0xFFFFB6C1)
              )
            ),
            startAngle = counterAuraAngle,
            sweepAngle = 280f,
            useCenter = false,
            style = Stroke(width = strokeWidth + 2.dp.toPx(), cap = StrokeCap.Round)
          )
        }

        // Main Pristine Circular Card
        Surface(
          shape = CircleShape,
          color = Color.White.copy(alpha = 0.98f),
          shadowElevation = 10.dp,
          modifier = Modifier.size(218.dp)
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
          ) {
            // Love title with romantic heart borders
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = Color(0xFFFF80AB),
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = loveTitle,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                letterSpacing = 0.5.sp
              )
              Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(16.dp)
              )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Big Days Count in High Contrast
            Text(
              text = "$animatedLoveDays",
              fontSize = 64.sp,
              fontWeight = FontWeight.ExtraBold,
              color = Color(0xFF26071B),
              lineHeight = 66.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Filled.VolunteerActivism,
                contentDescription = null,
                tint = Color(0xFFFF4081),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = strings.daysInLove,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFC2185B)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = null,
                tint = HotPink,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 4. Bottom Area: Hồ sơ 2 người với ảnh đại diện, tuổi, cung hoàng đạo & ngày sinh
      Card(
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color.White.copy(alpha = 0.96f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFFC6DB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("couple_bottom_section")
      ) {
        val isCoupled = relationshipStatus == com.example.data.model.OnlineStatus.COUPLED && partnerUser != null

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Left: Current User Profile (User can only edit their own profile)
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
              .weight(1f)
              .clickable { viewModel.openEditProfileDialog() }
              .testTag("current_user_profile_col")
          ) {
            Box(
              modifier = Modifier.size(76.dp),
              contentAlignment = Alignment.Center
            ) {
              Box(
                modifier = Modifier
                  .size(72.dp)
                  .clip(CircleShape)
                  .border(2.5.dp, Color(0xFF81D4FA), CircleShape)
                  .shadow(4.dp, CircleShape)
              ) {
                AsyncImage(
                  model = currentOnlineUser.avatarUrl.ifEmpty { boyAvatarUrl },
                  contentDescription = currentOnlineUser.effectiveDisplayName,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }
              // Mini edit badge
              Box(
                modifier = Modifier
                  .size(22.dp)
                  .align(Alignment.BottomEnd)
                  .clip(CircleShape)
                  .background(Color(0xFFE0F7FA))
                  .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Chỉnh sửa hồ sơ",
                  tint = Color(0xFF00ACC1),
                  modifier = Modifier.size(12.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(5.dp))

            Text(
              text = currentOnlineUser.effectiveDisplayName,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF26071B)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Badges: Soft Pastel Age & Zodiac (Auto calculated)
            Row(
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (!currentOnlineUser.isProfileSetup) {
                Surface(
                  shape = RoundedCornerShape(50.dp),
                  color = Color(0xFFFFEBEE)
                ) {
                  Text(
                    text = "Vô danh",
                    color = Color(0xFFC62828),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                  )
                }
              } else {
                Surface(
                  shape = RoundedCornerShape(50.dp),
                  color = Color(0xFFE0F7FA)
                ) {
                  Text(
                    text = if (currentOnlineUser.age > 0) "${currentOnlineUser.age}t" else "$boyAge t",
                    color = Color(0xFF00838F),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(50.dp),
                  color = Color(0xFFF3E5F5)
                ) {
                  Text(
                    text = currentOnlineUser.zodiac.ifEmpty { boyZodiac },
                    color = Color(0xFF7B1FA2),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
              text = if (currentOnlineUser.birthDate.isNotBlank()) currentOnlineUser.birthDate else "Bấm để cài đặt",
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF6B2B50)
            )
          }

          // Center: Beating Romantic Heart Button with Dual Love Expanding Waves
          Box(
            modifier = Modifier.size(76.dp),
            contentAlignment = Alignment.Center
          ) {
            // Expanding Heartbeat Ripple 1
            Box(
              modifier = Modifier
                .size(52.dp)
                .scale(ripple1Scale)
                .clip(CircleShape)
                .background(Color(0xFFFF80AB).copy(alpha = ripple1Alpha))
            )
            // Expanding Heartbeat Ripple 2
            Box(
              modifier = Modifier
                .size(52.dp)
                .scale(ripple2Scale)
                .clip(CircleShape)
                .background(Color(0xFFFFB6C1).copy(alpha = ripple2Alpha))
            )
            // Center Core Heart
            Box(
              modifier = Modifier
                .scale(pulseScale)
                .size(52.dp)
                .clip(CircleShape)
                .background(
                  Brush.linearGradient(
                    listOf(RoseGradientStart, RoseGradientMid)
                  )
                )
                .shadow(6.dp, CircleShape)
                .clickable {
                  viewModel.triggerFloatingHearts()
                  if (isCoupled) {
                    viewModel.showToast("Trái tim kết nối ${currentOnlineUser.effectiveDisplayName} & ${partnerUser?.displayName} ❤️")
                  } else {
                    viewModel.showToast("Hãy ghép đôi 1-1 để đồng bộ nhịp tim với người ấy 💕")
                    viewModel.openPairingScreen()
                  }
                }
                .testTag("btn_center_heart"),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Trái tim tình yêu",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
              )
            }
          }

          // Right: Partner Profile (if coupled) OR Waiting / Pairing Action (if single)
          if (isCoupled && partnerUser != null) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .weight(1f)
                .clickable { viewModel.openPairingScreen() }
                .testTag("partner_user_profile_col")
            ) {
              Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
              ) {
                Box(
                  modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(2.5.dp, Color(0xFFFF80AB), CircleShape)
                    .shadow(4.dp, CircleShape)
                ) {
                  AsyncImage(
                    model = partnerUser!!.avatarUrl.ifEmpty { girlAvatarUrl },
                    contentDescription = partnerUser!!.displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                  )
                }
                // Mini floating heart badge
                Box(
                  modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(Color(0xFFFCE4EC))
                    .border(1.5.dp, Color.White, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFFF4081),
                    modifier = Modifier.size(12.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(5.dp))

              Text(
                text = partnerUser!!.displayName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF26071B)
              )

              Spacer(modifier = Modifier.height(4.dp))

              // Badges: Soft Pastel Age & Zodiac
              Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  shape = RoundedCornerShape(50.dp),
                  color = Color(0xFFFCE4EC)
                ) {
                  Text(
                    text = if (partnerUser!!.age > 0) "${partnerUser!!.age}t" else "$girlAge t",
                    color = Color(0xFFC2185B),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(50.dp),
                  color = Color(0xFFFFF0F5)
                ) {
                  Text(
                    text = partnerUser!!.zodiac.ifEmpty { girlZodiac },
                    color = Color(0xFFAD1457),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(3.dp))

              Text(
                text = partnerUser!!.birthDate.ifEmpty { girlBirthDate },
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6B2B50)
              )
            }
          } else {
            // Uncoupled / Waiting Partner State
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .weight(1f)
                .clickable { viewModel.openPairingScreen() }
                .testTag("partner_waiting_placeholder_col")
            ) {
              Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
              ) {
                Box(
                  modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF0F5))
                    .border(2.dp, Color(0xFFFF80AB), CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Thêm người ấy",
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(32.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(5.dp))

              Text(
                text = "Chờ người ấy",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF880E4F)
              )

              Spacer(modifier = Modifier.height(4.dp))

              Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFFFEBEE)
              ) {
                Text(
                  text = "+ Ghép Đôi 1-1",
                  color = Color(0xFFE91E63),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }

              Spacer(modifier = Modifier.height(3.dp))

              Text(
                text = "Nhập mã hoặc link",
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 5. BỘ ĐẾM & MÁY TÍNH TOÁN NGÀY YÊU (Love Days Calculator Widget)
      LoveDaysCalculatorWidget(
        anniversaryDate = anniversaryDate,
        currentLoveDays = loveDays,
        boyName = boyName,
        girlName = girlName,
        language = appLanguage,
        onRecalculateAndSync = { calculatedDays ->
          viewModel.syncCalculatedDaysToHome(calculatedDays)
        },
        onUpdateAnniversaryDate = { newDate ->
          viewModel.setAnniversaryAndRecalculateDays(newDate)
        },
        onTriggerHearts = {
          viewModel.triggerFloatingHearts()
          val msg = if (appLanguage == AppLanguage.VI) {
            "Hai bạn $boyName & $girlName đã gắn bó $loveDays ngày yêu thương! ❤️"
          } else {
            "$boyName & $girlName have been in love for $loveDays days! ❤️"
          }
          viewModel.showToast(msg)
        },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Visual Milestone Tracker & Heart Badges Showcase Card
      MilestoneBadgeDashboardCard(
        viewModel = viewModel,
        onOpenFullShowcase = { viewModel.openBadgeShowcase() },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(14.dp))

      // 'Memories' Carousel Widget associated with specific anniversaries (Coil powered)
      AnniversaryMemoriesWidget(
        viewModel = viewModel,
        onNavigateToFullAlbum = onNavigateToMemories,
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(14.dp))

      // 6. GỢI Ý NGÀY KỶ NIỆM TIẾP THEO (Next Anniversary Milestone Suggestion Card)
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color.White.copy(alpha = 0.98f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFFC6DB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
          .fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFCE4EC)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.VolunteerActivism,
                  contentDescription = null,
                  tint = Color(0xFFFF4081),
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = strings.nextMilestoneHeader,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Primary,
                  letterSpacing = 0.8.sp
                )
                Text(
                  text = String.format(strings.nextMilestoneTitle, nextMilestone),
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF26071B)
                )
              }
            }

            // Countdown Pill with soft pastel shadow
            Surface(
              shape = RoundedCornerShape(50.dp),
              color = Color(0xFFFF4081),
              shadowElevation = 2.dp
            ) {
              Text(
                text = String.format(strings.daysRemainingFormat, daysRemaining),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Progress bar towards milestone
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = strings.progressTowards,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF6B2B50)
            )
            Text(
              text = "${(milestoneProgress * 100).toInt()}% ($loveDays / $nextMilestone)",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Primary
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          LinearProgressIndicator(
            progress = { animatedMilestoneProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(50.dp)),
            color = Primary,
            trackColor = Color(0xFFFFD1DF)
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Sweet Advice Box in Soft Pastel Tint
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFFF7FA),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD1DF)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = String.format(strings.romanticAdviceFormat, daysRemaining, nextMilestone, girlName),
              fontSize = 12.sp,
              lineHeight = 17.sp,
              color = Color(0xFF4A0023),
              modifier = Modifier.padding(10.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Action Buttons: Gifts and Calendar
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = onNavigateToGifts,
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Primary
              ),
              modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .testTag("btn_next_anniversary_gifts")
            ) {
              Icon(
                imageVector = Icons.Filled.CardGiftcard,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.White
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = strings.btnGiftIdeas,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }

            Button(
              onClick = onNavigateToCalendar,
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF80AB)
              ),
              modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .testTag("btn_next_anniversary_calendar")
            ) {
              Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.White
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = strings.btnAnniversaryCalendar,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }

          // Floating Hearts Particle Celebration Action
          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = {
              viewModel.celebrateMilestoneAnniversary(if (isMilestoneReached) loveDays else nextMilestone)
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFFFF4081)
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(42.dp)
              .testTag("btn_celebrate_milestone_hearts")
          ) {
            Icon(
              imageVector = Icons.Filled.Favorite,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Filled.Celebration,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isMilestoneReached) {
                strings.milestoneCelebrationTitle
              } else {
                "${strings.milestoneCelebrationBtn} (${String.format(strings.nextMilestoneTitle, nextMilestone)})"
              },
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 6. DÒNG CẢM XÚC & PHẢN HỒI YÊU THƯƠNG (Romantic Quotes & Sweet Feedback Card)
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color.White.copy(alpha = 0.98f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFFC6DB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
          .fillMaxWidth()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFCE4EC)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Favorite,
                  contentDescription = null,
                  tint = Color(0xFFFF4081),
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = strings.quoteSectionTag,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Primary,
                  letterSpacing = 0.8.sp
                )
                Text(
                  text = strings.quoteSectionTitle,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF26071B)
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(50.dp),
              color = Color(0xFFFFF0F5),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFC6DB))
            ) {
              Text(
                text = "${currentQuoteIndex.intValue + 1}/${loveQuotes.size}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Quote Box with elegant styling
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFF5F8),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD1DF)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(
                imageVector = Icons.Filled.FormatQuote,
                contentDescription = null,
                tint = Primary.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              AnimatedContent(
                targetState = loveQuotes[currentQuoteIndex.intValue],
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                label = "quote_transition"
              ) { quoteText ->
                Text(
                  text = quoteText,
                  fontSize = 13.5.sp,
                  fontStyle = FontStyle.Italic,
                  fontWeight = FontWeight.Medium,
                  lineHeight = 20.sp,
                  color = Color(0xFF330922)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Quote Action Buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = {
                currentQuoteIndex.intValue = (currentQuoteIndex.intValue + 1) % loveQuotes.size
                viewModel.showToast(if (appLanguage == AppLanguage.VI) "Đã đổi sang thông điệp yêu thương mới ✨" else "New love reflection loaded ✨")
              },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF80AB)
              ),
              modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .testTag("btn_shuffle_quote")
            ) {
              Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = strings.btnShuffleQuote,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }

            Button(
              onClick = {
                viewModel.triggerFloatingHearts()
                viewModel.showToast(if (appLanguage == AppLanguage.VI) "Đã sao chép lời yêu thương ngọt ngào để gửi cho $girlName! 💌" else "Sweet love quote copied to send to $girlName! 💌")
              },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Primary
              ),
              modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .testTag("btn_send_quote")
            ) {
              Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = strings.btnSendToPartner,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
fun HomeQuickActionButton(
  icon: ImageVector,
  label: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = ""
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .padding(horizontal = 6.dp, vertical = 4.dp)
      .testTag(testTag)
  ) {
    Box(
      modifier = Modifier
        .size(38.dp)
        .clip(CircleShape)
        .background(
          Brush.linearGradient(
            listOf(Color(0xFFFFB6C1), Color(0xFFFF80AB))
          )
        )
        .shadow(2.dp, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = Color.White,
        modifier = Modifier.size(18.dp)
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = Color(0xFF26071B),
      maxLines = 1
    )
  }
}

/**
 * Custom animation: Gentle romantic ambient floating hearts drifting smoothly
 * across the background of the dashboard using soft pastel colors.
 */
@Composable
fun AmbientFloatingHeartsCanvas(modifier: Modifier = Modifier) {
  val infiniteTransition = rememberInfiniteTransition(label = "ambient_hearts")
  val progress by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 7000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "drift_progress"
  )

  Canvas(modifier = modifier) {
    val heartColors = listOf(
      Color(0xFFFFB6C1).copy(alpha = 0.40f), // Soft Pastel Pink
      Color(0xFFFFC0CB).copy(alpha = 0.35f), // Soft Blossom
      Color(0xFFFFD1DC).copy(alpha = 0.42f), // Pastel Rose
      Color(0xFFF8BBD0).copy(alpha = 0.38f), // Soft Strawberry
      Color(0xFFE1BEE7).copy(alpha = 0.30f), // Soft Pastel Lavender
      Color(0xFFFFE4E1).copy(alpha = 0.45f)  // Misty Rose
    )

    val particles = listOf(
      Pair(0.10f, 0.15f),
      Pair(0.24f, 0.65f),
      Pair(0.38f, 0.05f),
      Pair(0.52f, 0.85f),
      Pair(0.68f, 0.40f),
      Pair(0.82f, 0.70f),
      Pair(0.92f, 0.25f),
      Pair(0.30f, 0.45f),
      Pair(0.60f, 0.20f)
    )

    particles.forEachIndexed { index, (xFrac, offsetFrac) ->
      val localProg = (progress + offsetFrac) % 1f
      val y = size.height * (1f - localProg)
      val sway = sin((localProg * 3.5 * Math.PI) + index).toFloat() * 18.dp.toPx()
      val x = size.width * xFrac + sway
      val heartSize = (14 + (index % 4) * 4).dp.toPx()
      val color = heartColors[index % heartColors.size]

      drawHeartPath(
        center = Offset(x, y),
        size = heartSize,
        color = color
      )
    }
  }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawHeartPath(
  center: Offset,
  size: Float,
  color: Color
) {
  val path = Path().apply {
    val half = size / 2f
    val topCurveHeight = size * 0.35f
    moveTo(center.x, center.y + half * 0.8f)
    cubicTo(
      center.x - half, center.y,
      center.x - half, center.y - topCurveHeight,
      center.x, center.y - topCurveHeight * 0.35f
    )
    cubicTo(
      center.x + half, center.y - topCurveHeight,
      center.x + half, center.y,
      center.x, center.y + half * 0.8f
    )
    close()
  }
  drawPath(path = path, color = color)
}

