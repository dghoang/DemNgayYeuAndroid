package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AdBannerPlaceholder
import com.example.ui.components.FloatingHeartsOverlay
import com.example.ui.components.InLoveBottomNav
import com.example.ui.components.InLoveTopBar
import com.example.ui.screens.AddChecklistDialog
import com.example.ui.screens.AddMilestoneDialog
import com.example.ui.screens.AddReminderDialog
import com.example.ui.screens.CalendarScreen
import com.example.ui.screens.CaptureMemoryDialog
import com.example.ui.screens.EditCoupleDialog
import com.example.ui.screens.GiftDetailDialog
import com.example.ui.screens.GiftScreen
import com.example.ui.screens.LanguageSelectionDialog
import com.example.ui.screens.LoveHomeScreen
import com.example.ui.screens.MemoriesGridScreen
import com.example.ui.screens.ReminderScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.UserGuideDialog
import com.example.ui.screens.VipProposalDialog
import com.example.ui.screens.WallpaperPickerDialog
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.util.AppLanguage
import com.example.ui.util.LocalizedStrings
import com.example.ui.viewmodel.InLoveViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        InLoveApp()
      }
    }
  }
}

@Composable
fun InLoveApp(viewModel: InLoveViewModel = viewModel()) {
  val selectedTab by viewModel.selectedTab.collectAsState()
  val toastMessage by viewModel.toastMessage.collectAsState()
  val floatingHeartsTrigger by viewModel.floatingHeartsTrigger.collectAsState()
  val isMilestoneCelebration by viewModel.isMilestoneCelebration.collectAsState()

  val showAddReminderDialog by viewModel.showAddReminderDialog.collectAsState()
  val showAddMilestoneDialog by viewModel.showAddMilestoneDialog.collectAsState()
  val showAddChecklistDialog by viewModel.showAddChecklistDialog.collectAsState()
  val showEditCoupleDialog by viewModel.showEditCoupleDialog.collectAsState()
  val selectedGiftDetail by viewModel.selectedGiftDetail.collectAsState()
  val showVipProposalDetail by viewModel.showVipProposalDetail.collectAsState()

  val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
  val showWallpaperDialog by viewModel.showWallpaperDialog.collectAsState()
  val showMemoryDialog by viewModel.showMemoryDialog.collectAsState()
  val showGuideDialog by viewModel.showGuideDialog.collectAsState()
  val appLanguage by viewModel.appLanguage.collectAsState()
  val selectedWallpaperUrl by viewModel.selectedWallpaperUrl.collectAsState()

  val locale = remember(appLanguage) {
    when (appLanguage) {
      AppLanguage.VI -> java.util.Locale("vi")
      AppLanguage.EN -> java.util.Locale("en")
    }
  }
  val currentConfig = androidx.compose.ui.platform.LocalConfiguration.current
  val configuration = remember(locale, currentConfig) {
    android.content.res.Configuration(currentConfig).apply {
      setLocale(locale)
    }
  }
  val context = androidx.compose.ui.platform.LocalContext.current.createConfigurationContext(configuration)

  androidx.compose.runtime.CompositionLocalProvider(
    androidx.compose.ui.platform.LocalConfiguration provides configuration,
    androidx.compose.ui.platform.LocalContext provides context
  ) {
    val strings = LocalizedStrings.fromContext(context, appLanguage)

    val boyName by viewModel.boyName.collectAsState()
    val boyBirthDate by viewModel.boyBirthDate.collectAsState()
    val boyAvatarUrl by viewModel.boyAvatarUrl.collectAsState()
    val boyAge by viewModel.boyAge.collectAsState()
    val boyZodiac by viewModel.boyZodiac.collectAsState()

    val girlName by viewModel.girlName.collectAsState()
    val girlBirthDate by viewModel.girlBirthDate.collectAsState()
    val girlAvatarUrl by viewModel.girlAvatarUrl.collectAsState()
    val girlAge by viewModel.girlAge.collectAsState()
    val girlZodiac by viewModel.girlZodiac.collectAsState()

    val loveTitle by viewModel.loveTitle.collectAsState()
    val loveDays by viewModel.loveDays.collectAsState()
    val anniversaryDate by viewModel.anniversaryDate.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
      toastMessage?.let { msg ->
        snackbarHostState.showSnackbar(msg)
        viewModel.clearToast()
      }
    }

    // Trigger floating hearts on first open
    LaunchedEffect(Unit) {
      viewModel.triggerFloatingHearts()
    }

    val screenTitle = when (selectedTab) {
      0 -> strings.navHome
      1 -> strings.navMemories
      2 -> strings.navCalendar
      3 -> strings.navGifts
      else -> strings.navSettings
    }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(
            Surface,
            SurfaceContainerLowest,
            Color(0xFFFFF7F9)
          )
        )
      )
  ) {
    Scaffold(
      containerColor = Color.Transparent,
      topBar = {
        if (selectedTab == 0) {
          // Trang Chủ (Yêu Thích): Phần chữ yêu thích & trái tim được để trống trực tiếp để chèn quảng cáo (Banner Ad)
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .statusBarsPadding()
              .padding(top = 4.dp, bottom = 2.dp)
          ) {
            AdBannerPlaceholder(
              onClick = {
                viewModel.showToast(strings.adPlaceholderNotice)
              }
            )
          }
        } else {
          Column {
            InLoveTopBar(
              title = screenTitle,
              onHeartClick = {
                viewModel.triggerFloatingHearts()
                viewModel.showToast(strings.countingDaysNotice.format(loveDays))
              },
              onProfileClick = {
                viewModel.setTab(4)
              }
            )

            // Khoảng trắng phía trên ứng dụng dành cho Plugin Quảng Cáo
            AdBannerPlaceholder(
              onClick = {
                viewModel.showToast(strings.adPlaceholderNotice)
              }
            )
          }
        }
      },
      bottomBar = {
        InLoveBottomNav(
          selectedTab = selectedTab,
          onTabSelected = { tabIndex ->
            viewModel.setTab(tabIndex)
          },
          labels = listOf(
            strings.navHome,
            strings.navMemories,
            strings.navCalendar,
            strings.navGifts,
            strings.navSettings
          )
        )
      },
      snackbarHost = { SnackbarHost(snackbarHostState) },
      modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        AnimatedContent(
          targetState = selectedTab,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          label = "screen_transition"
        ) { targetIndex ->
          when (targetIndex) {
            0 -> LoveHomeScreen(
              viewModel = viewModel,
              onNavigateToCalendar = { viewModel.setTab(2) },
              onNavigateToGifts = { viewModel.setTab(3) }
            )
            1 -> MemoriesGridScreen(viewModel = viewModel)
            2 -> CalendarScreen(
              viewModel = viewModel,
              onNavigateToGifts = { viewModel.setTab(3) }
            )
            3 -> GiftScreen(viewModel = viewModel)
            4 -> SettingsScreen(viewModel = viewModel)
            else -> LoveHomeScreen(
              viewModel = viewModel,
              onNavigateToCalendar = { viewModel.setTab(2) },
              onNavigateToGifts = { viewModel.setTab(3) }
            )
          }
        }
      }
    }

    // Romantic Floating Hearts Animation Overlay (Particle System with Milestone Celebration mode)
    FloatingHeartsOverlay(
      trigger = floatingHeartsTrigger,
      isMilestoneCelebration = isMilestoneCelebration
    )

    // Dialogs
    if (showEditCoupleDialog) {
      EditCoupleDialog(
        currentBoyName = boyName,
        currentBoyBirth = boyBirthDate,
        currentBoyAvatar = boyAvatarUrl,
        currentBoyAge = boyAge,
        currentBoyZodiac = boyZodiac,
        currentGirlName = girlName,
        currentGirlBirth = girlBirthDate,
        currentGirlAvatar = girlAvatarUrl,
        currentGirlAge = girlAge,
        currentGirlZodiac = girlZodiac,
        currentTitle = loveTitle,
        currentDays = loveDays,
        currentAnniversary = anniversaryDate,
        onDismiss = { viewModel.closeEditCoupleDialog() },
        onSave = { b, bb, ba, bAge, bz, g, gb, ga, gAge, gz, t, d, ann ->
          viewModel.saveCoupleProfile(
            boy = b,
            boyBirth = bb,
            boyAvatar = ba,
            boyA = bAge,
            boyZod = bz,
            girl = g,
            girlBirth = gb,
            girlAvatar = ga,
            girlA = gAge,
            girlZod = gz,
            title = t,
            days = d,
            anniversary = ann
          )
        }
      )
    }

    if (showAddReminderDialog) {
      AddReminderDialog(
        onDismiss = { viewModel.closeAddReminderDialog() },
        onConfirm = { title, dateText, note ->
          viewModel.addCustomReminder(title, dateText, note)
        }
      )
    }

    if (showAddMilestoneDialog) {
      AddMilestoneDialog(
        onDismiss = { viewModel.closeAddMilestoneDialog() },
        onConfirm = { title, dateText, subtitle, catTag, secTag, img, days, important ->
          viewModel.addMilestone(title, dateText, subtitle, catTag, secTag, img, days, important)
        }
      )
    }

    if (showAddChecklistDialog) {
      AddChecklistDialog(
        onDismiss = { viewModel.closeAddChecklistDialog() },
        onConfirm = { text ->
          viewModel.addChecklist(text)
          viewModel.closeAddChecklistDialog()
        }
      )
    }

    selectedGiftDetail?.let { gift ->
      GiftDetailDialog(
        gift = gift,
        onDismiss = { viewModel.closeGiftDetail() },
        onToggleFavorite = { viewModel.toggleGiftFavorite(gift) }
      )
    }

    if (showVipProposalDetail) {
      VipProposalDialog(
        onDismiss = { viewModel.closeVipProposal() }
      )
    }

    if (showLanguageDialog) {
      LanguageSelectionDialog(
        currentLanguage = appLanguage,
        onLanguageSelected = { lang ->
          viewModel.setLanguage(lang)
        },
        onDismiss = { viewModel.closeLanguageDialog() }
      )
    }

    if (showWallpaperDialog) {
      WallpaperPickerDialog(
        language = appLanguage,
        currentWallpaperUrl = selectedWallpaperUrl,
        onApplyWallpaper = { url ->
          viewModel.setWallpaper(url)
        },
        onDismiss = { viewModel.closeWallpaperDialog() }
      )
    }

    if (showMemoryDialog) {
      CaptureMemoryDialog(
        language = appLanguage,
        onSaveMemory = { note, photoUrl ->
          viewModel.saveMemory(note, photoUrl)
        },
        onDismiss = { viewModel.closeMemoryDialog() }
      )
    }

    if (showGuideDialog) {
      UserGuideDialog(
        language = appLanguage,
        onDismiss = { viewModel.closeGuideDialog() }
      )
    }
  }
}

}
