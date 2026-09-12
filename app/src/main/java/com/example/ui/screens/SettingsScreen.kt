package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.RoseGradientEnd
import com.example.ui.theme.RoseGradientMid
import com.example.ui.theme.RoseGradientStart
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.util.AppLanguage
import com.example.ui.viewmodel.InLoveViewModel

@Composable
fun SettingsScreen(viewModel: InLoveViewModel) {
  var notificationEnabled by remember { mutableStateOf(true) }
  var soundEnabled by remember { mutableStateOf(true) }
  var biometricEnabled by remember { mutableStateOf(false) }

  val boyName by viewModel.boyName.collectAsState()
  val girlName by viewModel.girlName.collectAsState()
  val boyAvatarUrl by viewModel.boyAvatarUrl.collectAsState()
  val girlAvatarUrl by viewModel.girlAvatarUrl.collectAsState()
  val loveDays by viewModel.loveDays.collectAsState()

  val appLanguage by viewModel.appLanguage.collectAsState()
  val context = LocalContext.current

  val notifEnabledToast = stringResource(R.string.settings_notif_enabled_toast)
  val notifDisabledToast = stringResource(R.string.settings_notif_disabled_toast)
  val dbSyncToast = stringResource(R.string.settings_db_sync_toast)
  val aboutToast = stringResource(R.string.settings_about_toast)

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Couple Profile Card
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
          containerColor = SurfaceContainerLowest.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { viewModel.openEditCoupleDialog() }
          .testTag("settings_couple_profile_card")
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Both avatars side by side
          Box(modifier = Modifier.size(68.dp)) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFF4DD0E1), CircleShape)
                .align(Alignment.TopStart)
            ) {
              AsyncImage(
                model = boyAvatarUrl,
                contentDescription = "Partner 1 Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .border(2.dp, Color(0xFFFF8A65), CircleShape)
                .align(Alignment.BottomEnd)
            ) {
              AsyncImage(
                model = girlAvatarUrl,
                contentDescription = "Partner 2 Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
          }

          Column(modifier = Modifier.weight(1f)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Text(
                text = "$boyName & $girlName",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
              Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.settings_profile_card_edit),
                tint = Primary,
                modifier = Modifier.size(16.dp)
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = stringResource(R.string.settings_sweet_days_format, loveDays),
              fontSize = 12.sp,
              color = Primary,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = stringResource(R.string.settings_db_secured),
              fontSize = 11.sp,
              color = OnSurfaceVariant
            )
          }

          Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = OnSurfaceVariant
          )
        }
      }
    }

    // 2. Language Selection Toggle (Primary Feature Highlight)
    item {
      LanguageSelectionToggleCard(
        currentLanguage = appLanguage,
        onLanguageChanged = { newLang -> viewModel.setLanguage(newLang) },
        onOpenDialog = { viewModel.openLanguageDialog() }
      )
    }

    // 3. Settings Group: Quản Lý Hồ Sơ & Kỷ Niệm
    item {
      Text(
        text = stringResource(R.string.settings_section_profile),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Primary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          SettingClickableRow(
            icon = Icons.Filled.Favorite,
            title = stringResource(R.string.settings_edit_profile_title),
            subtitle = stringResource(R.string.settings_edit_profile_sub),
            onClick = { viewModel.openEditCoupleDialog() }
          )

          SettingClickableRow(
            icon = Icons.Filled.CameraAlt,
            title = stringResource(R.string.settings_capture_memory_title),
            subtitle = stringResource(R.string.settings_capture_memory_sub),
            onClick = { viewModel.openMemoryDialog() }
          )

          SettingClickableRow(
            icon = Icons.Filled.CloudSync,
            title = stringResource(R.string.settings_db_sync_title),
            subtitle = stringResource(R.string.settings_db_sync_sub),
            onClick = { viewModel.showToast(dbSyncToast) }
          )
        }
      }
    }

    // 4. Settings Group: Giao Diện & Hình Nền
    item {
      Text(
        text = stringResource(R.string.settings_section_appearance),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Primary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          SettingClickableRow(
            icon = Icons.Filled.Wallpaper,
            title = stringResource(R.string.settings_wallpaper_title),
            subtitle = stringResource(R.string.settings_wallpaper_sub),
            onClick = { viewModel.openWallpaperDialog() }
          )

          SettingClickableRow(
            icon = Icons.Filled.MenuBook,
            title = stringResource(R.string.settings_guide_title),
            subtitle = stringResource(R.string.settings_guide_sub),
            onClick = { viewModel.openGuideDialog() }
          )
        }
      }
    }

    // 5. Settings Group: Thông Báo & Lời Nhắc
    item {
      Text(
        text = stringResource(R.string.settings_section_notifications),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Primary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
          SettingSwitchRow(
            icon = Icons.Filled.Notifications,
            title = stringResource(R.string.settings_notif_anniversary_title),
            subtitle = stringResource(R.string.settings_notif_anniversary_sub),
            checked = notificationEnabled,
            onCheckedChange = {
              notificationEnabled = it
              viewModel.showToast(if (it) notifEnabledToast else notifDisabledToast)
            }
          )

          SettingSwitchRow(
            icon = Icons.Filled.VolumeUp,
            title = stringResource(R.string.settings_notif_sound_title),
            subtitle = stringResource(R.string.settings_notif_sound_sub),
            checked = soundEnabled,
            onCheckedChange = { soundEnabled = it }
          )
        }
      }
    }

    // 6. Settings Group: Dữ Liệu & Bảo Mật
    item {
      Text(
        text = stringResource(R.string.settings_section_security),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Primary
      )
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          SettingSwitchRow(
            icon = Icons.Filled.Lock,
            title = stringResource(R.string.settings_lock_title),
            subtitle = stringResource(R.string.settings_lock_sub),
            checked = biometricEnabled,
            onCheckedChange = { biometricEnabled = it }
          )

          SettingClickableRow(
            icon = Icons.Filled.Info,
            title = stringResource(R.string.settings_about_title),
            subtitle = stringResource(R.string.settings_about_sub),
            onClick = { viewModel.showToast(aboutToast) }
          )
        }
      }
    }
  }
}

/**
 * Dedicated Language Selection Toggle Card in the Settings Tab.
 * Supports both immediate Segmented Button switching and quick Toggle Switch.
 */
@Composable
fun LanguageSelectionToggleCard(
  currentLanguage: AppLanguage,
  onLanguageChanged: (AppLanguage) -> Unit,
  onOpenDialog: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = 1.dp,
        brush = Brush.linearGradient(listOf(Color(0xFFFF80AB).copy(alpha = 0.5f), Color(0xFFFFD1DC).copy(alpha = 0.5f))),
        shape = RoundedCornerShape(22.dp)
      )
      .testTag("language_selection_toggle")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      // Header with Language Icon and Title
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.weight(1f)
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(
                Brush.linearGradient(listOf(RoseGradientStart, RoseGradientMid))
              ),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.Language,
              contentDescription = stringResource(R.string.settings_language_title),
              tint = Color.White,
              modifier = Modifier.size(22.dp)
            )
          }

          Column {
            Text(
              text = stringResource(R.string.settings_language_title),
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = OnSurface
            )
            Text(
              text = stringResource(R.string.settings_language_sub),
              fontSize = 11.5.sp,
              color = OnSurfaceVariant
            )
          }
        }

        // Active language badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Primary.copy(alpha = 0.12f),
          modifier = Modifier.clickable { onOpenDialog() }
        ) {
          Text(
            text = if (currentLanguage == AppLanguage.VI) "🇻🇳 VI" else "🇬🇧 EN",
            color = Primary,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
          )
        }
      }

      // Segmented Language Toggle Selector
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(Color(0xFFF7F1F5))
          .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Vietnamese Button
        LanguageToggleButton(
          title = stringResource(R.string.lang_vietnamese),
          isSelected = currentLanguage == AppLanguage.VI,
          onClick = { onLanguageChanged(AppLanguage.VI) },
          modifier = Modifier
            .weight(1f)
            .testTag("lang_toggle_vi")
        )

        // English Button
        LanguageToggleButton(
          title = stringResource(R.string.lang_english),
          isSelected = currentLanguage == AppLanguage.EN,
          onClick = { onLanguageChanged(AppLanguage.EN) },
          modifier = Modifier
            .weight(1f)
            .testTag("lang_toggle_en")
        )
      }

      // Quick Switch Row for single-tap toggle
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = stringResource(R.string.settings_language_toggle_label),
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium,
            color = OnSurface
          )
          Text(
            text = if (currentLanguage == AppLanguage.EN) {
              stringResource(R.string.settings_language_current_en)
            } else {
              stringResource(R.string.settings_language_current_vi)
            },
            fontSize = 11.sp,
            color = Primary,
            fontWeight = FontWeight.SemiBold
          )
        }

        Switch(
          checked = currentLanguage == AppLanguage.EN,
          onCheckedChange = { isEnglishChecked ->
            onLanguageChanged(if (isEnglishChecked) AppLanguage.EN else AppLanguage.VI)
          },
          colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = Primary,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = Color(0xFFE0C4D3)
          ),
          modifier = Modifier.testTag("language_switch_toggle")
        )
      }
    }
  }
}

@Composable
private fun LanguageToggleButton(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val backgroundColor by animateColorAsState(
    targetValue = if (isSelected) Primary else Color.Transparent,
    animationSpec = tween(durationMillis = 200),
    label = "lang_toggle_bg"
  )
  val textColor by animateColorAsState(
    targetValue = if (isSelected) Color.White else OnSurface,
    animationSpec = tween(durationMillis = 200),
    label = "lang_toggle_text"
  )

  Surface(
    shape = RoundedCornerShape(10.dp),
    color = backgroundColor,
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .clickable { onClick() }
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp, horizontal = 12.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (isSelected) {
        Icon(
          imageVector = Icons.Filled.Check,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier
            .size(16.dp)
            .padding(end = 4.dp)
        )
      }
      Text(
        text = title,
        color = textColor,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        fontSize = 13.5.sp
      )
    }
  }
}

@Composable
fun SettingSwitchRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.weight(1f)
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(PrimaryFixed.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = Primary,
          modifier = Modifier.size(18.dp)
        )
      }
      Column {
        Text(
          text = title,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = OnSurface
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = OnSurfaceVariant
        )
      }
    }

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = Primary
      )
    )
  }
}

@Composable
fun SettingClickableRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      modifier = Modifier.weight(1f)
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(SurfaceContainerHigh),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = Primary,
          modifier = Modifier.size(18.dp)
        )
      }
      Column {
        Text(
          text = title,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = OnSurface
        )
        Text(
          text = subtitle,
          fontSize = 11.sp,
          color = OnSurfaceVariant
        )
      }
    }

    Icon(
      imageVector = Icons.Filled.ChevronRight,
      contentDescription = null,
      tint = OnSurfaceVariant,
      modifier = Modifier.size(18.dp)
    )
  }
}
