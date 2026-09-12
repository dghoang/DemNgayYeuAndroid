package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MilestoneEntity
import com.example.ui.theme.OnPrimaryFixed
import com.example.ui.theme.OnPrimaryFixedVariant
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.RoseGradientEnd
import com.example.ui.theme.RoseGradientMid
import com.example.ui.theme.RoseGradientStart
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed
import com.example.ui.viewmodel.InLoveViewModel

@Composable
fun CalendarScreen(
  viewModel: InLoveViewModel,
  onNavigateToGifts: () -> Unit
) {
  val milestones by viewModel.milestones.collectAsState()
  val currentFilter by viewModel.calendarFilter.collectAsState()
  var selectedDay by remember { mutableIntStateOf(11) }
  var milestoneToDelete by remember { mutableStateOf<MilestoneEntity?>(null) }

  milestoneToDelete?.let { milestone ->
    DeleteConfirmationDialog(
      title = "Xóa ngày kỷ niệm?",
      message = "Bạn có chắc chắn muốn xóa ngày kỷ niệm này khỏi lịch không? Lời nhắc và dữ liệu đã lưu sẽ bị xóa hoàn toàn khỏi thiết bị.",
      itemName = milestone.title,
      onConfirm = {
        viewModel.deleteMilestone(milestone.id)
        milestoneToDelete = null
      },
      onDismiss = {
        milestoneToDelete = null
      }
    )
  }

  val filteredMilestones = remember(milestones, currentFilter) {
    when (currentFilter) {
      "Sắp tới (3)" -> milestones.filter { !it.isPast }
      "Đã qua (1)" -> milestones.filter { it.isPast }
      else -> milestones
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Month Bar & Navigation
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Filled.CalendarToday,
              contentDescription = null,
              tint = Primary,
              modifier = Modifier.size(24.dp)
            )
            Text(
              text = "Tháng 9, 2026",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = OnSurface
            )
          }

          Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceContainer.copy(alpha = 0.8f),
            shadowElevation = 1.dp
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
              IconButton(
                onClick = { viewModel.showToast("Xem tháng 8/2026") },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Filled.ChevronLeft,
                  contentDescription = "Tháng trước",
                  tint = OnSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }
              Text(
                text = "T9",
                color = Primary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
              )
              IconButton(
                onClick = { viewModel.showToast("Xem tháng 10/2026") },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Filled.ChevronRight,
                  contentDescription = "Tháng sau",
                  tint = OnSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }

      // 2. Filter Pills Row
      item {
        val filterOptions = listOf(
          Pair("Tất cả (4)", Icons.Filled.AutoAwesome),
          Pair("Sắp tới (3)", Icons.Filled.HourglassTop),
          Pair("Đã qua (1)", Icons.Filled.HistoryEdu),
          Pair("Năm 2026", Icons.Filled.Favorite)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          filterOptions.forEach { (filterName, icon) ->
            val isSelected = currentFilter == filterName
            Surface(
              shape = RoundedCornerShape(24.dp),
              color = if (isSelected) Color.Transparent else SurfaceContainerLowest.copy(alpha = 0.85f),
              shadowElevation = if (isSelected) 4.dp else 1.dp,
              modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .then(
                  if (isSelected) {
                    Modifier.background(
                      Brush.horizontalGradient(
                        listOf(Primary, PrimaryContainer)
                      )
                    )
                  } else Modifier
                )
                .clickable { viewModel.setCalendarFilter(filterName) }
                .testTag("filter_pill_$filterName")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = null,
                  tint = if (isSelected) Color.White else OnSurfaceVariant,
                  modifier = Modifier.size(16.dp)
                )
                Text(
                  text = filterName,
                  fontSize = 13.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  color = if (isSelected) Color.White else OnSurfaceVariant
                )
              }
            }
          }
        }
      }

      // 3. Frosted Mini Calendar Strip
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
          elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            // Day names row
            val dayNames = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              dayNames.forEachIndexed { i, name ->
                val textColor = when (i) {
                  4 -> Primary
                  6 -> Secondary
                  else -> OnSurfaceVariant.copy(alpha = 0.7f)
                }
                Text(
                  text = name,
                  fontSize = 11.sp,
                  fontWeight = if (i == 4 || i == 6) FontWeight.Bold else FontWeight.Medium,
                  color = textColor,
                  modifier = Modifier.width(36.dp),
                  textAlign = TextAlign.Center
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Day numbers row (07 to 13)
            val days = listOf(7, 8, 9, 10, 11, 12, 13)
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceAround,
              verticalAlignment = Alignment.CenterVertically
            ) {
              days.forEach { dayNum ->
                val isMilestoneDay = dayNum == 11
                val isToday = dayNum == 9
                val isSelected = selectedDay == dayNum

                Box(
                  modifier = Modifier
                    .width(38.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .then(
                      when {
                        isMilestoneDay -> Modifier
                          .shadow(6.dp, RoundedCornerShape(14.dp))
                          .background(
                            Brush.verticalGradient(
                              listOf(RoseGradientStart, RoseGradientMid)
                            )
                          )
                        isSelected && !isMilestoneDay -> Modifier
                          .background(PrimaryFixed.copy(alpha = 0.6f))
                        isToday -> Modifier
                          .background(SurfaceContainerLow)
                          .border(1.dp, Primary.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                        else -> Modifier
                      }
                    )
                    .clickable {
                      selectedDay = dayNum
                      if (dayNum == 11) {
                        viewModel.showToast("11/09: Cột mốc 1.000 ngày bên nhau!")
                      } else if (dayNum == 9) {
                        viewModel.showToast("Hôm nay: Ngày 09/09/2026")
                      }
                    },
                  contentAlignment = Alignment.Center
                ) {
                  Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Text(
                      text = String.format("%02d", dayNum),
                      fontSize = 13.sp,
                      fontWeight = if (isMilestoneDay || isToday || isSelected) FontWeight.Bold else FontWeight.Medium,
                      color = when {
                        isMilestoneDay -> Color.White
                        dayNum == 13 -> Secondary
                        else -> OnSurface
                      }
                    )
                    if (isMilestoneDay) {
                      Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                      )
                    } else if (isToday) {
                      Box(
                        modifier = Modifier
                          .size(5.dp)
                          .clip(CircleShape)
                          .background(Secondary)
                      )
                    } else {
                      Spacer(modifier = Modifier.size(5.dp))
                    }
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Status Note inside Calendar
            Surface(
              shape = RoundedCornerShape(50.dp),
              color = PrimaryFixed.copy(alpha = 0.5f),
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  viewModel.showToast("Sắp đến mốc 1.000 ngày yêu nhau!")
                }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(
                    imageVector = Icons.Filled.Stars,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(16.dp)
                  )
                  Text(
                    text = "Chỉ còn 3 ngày đến Kỷ niệm 1.000 ngày yêu nhau!",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = OnPrimaryFixed
                  )
                }
                Icon(
                  imageVector = Icons.Filled.ArrowForward,
                  contentDescription = null,
                  tint = Primary,
                  modifier = Modifier.size(14.dp)
                )
              }
            }
          }
        }
      }

      // 4. Milestone Cards Stream
      items(filteredMilestones, key = { it.id }) { milestone ->
        MilestoneCard(
          milestone = milestone,
          onGiftClick = onNavigateToGifts,
          onNotificationToggle = { viewModel.toggleMilestoneNotification(milestone) },
          onEditClick = { viewModel.showToast("Chỉnh sửa: ${milestone.title}") },
          onAlbumClick = { viewModel.showToast("Mở album ảnh kỷ niệm...") },
          onDelete = { milestoneToDelete = milestone },
          onSetAlarm = {
            viewModel.openSetAlarmDialog(
              title = "Kỷ niệm: ${milestone.title}",
              message = "Hôm nay là ngày kỷ niệm ${milestone.title}! ${milestone.subtitle}",
              reminderId = milestone.id
            )
          }
        )
      }

      // 5. Sync Card
      item {
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = SurfaceContainerLowest.copy(alpha = 0.7f),
          shadowElevation = 1.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
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
                  .clip(RoundedCornerShape(12.dp))
                  .background(SurfaceContainerHighest)
              ) {
                AsyncImage(
                  model = "https://lh3.googleusercontent.com/aida-public/AB6AXuCg-PmA8kAH3aEsx4nS5akuDkkWQeWMutmW8Lc76ASO-JMvtiwMNbsTfuYqBGpez7bHTAYekQNilJ5X5BHaP78pQf4tATX48UynvtOeQWG8kcCF-v9OqIdcm1OAjLGtZsO1ygTFLVd9qW-yngUnwCmOtlFWn_wzhCPvYfzMcFLVzeEoOX9NuP8fk911cjdlzd0yv0FvSo3h7qg26BWqyaqSpVwTuvKoKaZ20NJLOUWeBfHbRlqMZcIwfI1FL31cw1WGBrQ",
                  contentDescription = "Sync thumbnail",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }
              Column {
                Text(
                  text = "Đồng bộ dữ liệu mốc thời gian",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = Primary
                )
                Text(
                  text = "Đã chuyển 4 sự kiện từ danh sách cũ",
                  fontSize = 11.sp,
                  color = OnSurfaceVariant
                )
              }
            }

            Icon(
              imageVector = Icons.Filled.Verified,
              contentDescription = "Verified",
              tint = Primary,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }

    // 6. Floating Action Button "+ Thêm Kỷ Niệm"
    Box(
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 16.dp, bottom = 85.dp)
    ) {
      Surface(
        shape = RoundedCornerShape(50.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
          .clip(RoundedCornerShape(50.dp))
          .background(
            Brush.horizontalGradient(
              listOf(RoseGradientStart, RoseGradientMid, RoseGradientEnd)
            )
          )
          .clickable { viewModel.openAddMilestoneDialog() }
          .testTag("btn_add_milestone")
      ) {
        Row(
          modifier = Modifier
            .background(
              Brush.horizontalGradient(
                listOf(RoseGradientStart, RoseGradientMid, RoseGradientEnd)
              )
            )
            .padding(horizontal = 18.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
          )
          Text(
            text = "Thêm Kỷ Niệm",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }
      }
    }
  }
}

@Composable
fun MilestoneCard(
  milestone: MilestoneEntity,
  onGiftClick: () -> Unit,
  onNotificationToggle: () -> Unit,
  onEditClick: () -> Unit,
  onAlbumClick: () -> Unit,
  onDelete: () -> Unit,
  onSetAlarm: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("milestone_card_${milestone.id}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top Tag Row + Notification & Alarm & Delete buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Category Tag
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = when {
              milestone.isImportant -> PrimaryFixed.copy(alpha = 0.5f)
              milestone.isPast -> TertiaryFixed.copy(alpha = 0.5f)
              else -> SurfaceContainerHigh
            }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              val tagIcon = when {
                milestone.isImportant -> Icons.Filled.Stars
                milestone.categoryTag.contains("Sinh Nhật", ignoreCase = true) -> Icons.Filled.Cake
                milestone.isPast -> Icons.Filled.FlightTakeoff
                else -> Icons.Filled.VolunteerActivism
              }
              Icon(
                imageVector = tagIcon,
                contentDescription = null,
                tint = if (milestone.isImportant) Primary else if (milestone.isPast) Tertiary else Secondary,
                modifier = Modifier.size(13.dp)
              )
              Text(
                text = milestone.categoryTag,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (milestone.isImportant) Primary else if (milestone.isPast) OnTertiaryFixed else Secondary
              )
            }
          }

          // Secondary Tag
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceContainer
          ) {
            Text(
              text = milestone.secondaryTag,
              fontSize = 11.sp,
              color = OnSurfaceVariant,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          if (milestone.isPast) {
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = TertiaryFixed.copy(alpha = 0.8f)
            ) {
              Text(
                text = "Đã qua 268 ngày",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Tertiary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        // Action Icons Row: Alarm + Notification + Delete
        Row(
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Set Alarm button
          IconButton(
            onClick = onSetAlarm,
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(SurfaceContainer)
              .testTag("btn_alarm_milestone_${milestone.id}")
          ) {
            Icon(
              imageVector = Icons.Filled.Alarm,
              contentDescription = "Cài báo thức kỷ niệm",
              tint = Primary,
              modifier = Modifier.size(17.dp)
            )
          }

          // Notification Toggle
          if (!milestone.isPast) {
            IconButton(
              onClick = onNotificationToggle,
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SurfaceContainer)
                .testTag("btn_notif_milestone_${milestone.id}")
            ) {
              Icon(
                imageVector = if (milestone.notificationEnabled) Icons.Filled.NotificationsActive else Icons.Outlined.Notifications,
                contentDescription = "Notification",
                tint = if (milestone.notificationEnabled) Primary else OnSurfaceVariant,
                modifier = Modifier.size(17.dp)
              )
            }
          }

          // Delete milestone button
          IconButton(
            onClick = onDelete,
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(SurfaceContainer)
              .testTag("btn_delete_milestone_${milestone.id}")
          ) {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Xóa kỷ niệm",
              tint = OnSurfaceVariant,
              modifier = Modifier.size(17.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Middle: Image + Title/Date + Countdown Pill
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Photo
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(2.dp)
        ) {
          AsyncImage(
            model = milestone.imageUrl,
            contentDescription = milestone.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        // Title and Date
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = milestone.title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Spacer(modifier = Modifier.height(3.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = when {
                milestone.categoryTag.contains("Sinh Nhật") -> Icons.Filled.Cake
                milestone.isPast -> Icons.Filled.PhotoLibrary
                else -> Icons.Filled.CalendarToday
              },
              contentDescription = null,
              tint = Primary,
              modifier = Modifier.size(13.dp)
            )
            Text(
              text = "${milestone.dateText} • ${milestone.subtitle}",
              fontSize = 11.sp,
              color = OnSurfaceVariant
            )
          }
        }

        // Right side badge: Countdown Pill or Saved indicator
        if (milestone.isPast) {
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = SurfaceContainer,
            modifier = Modifier.padding(2.dp)
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
              Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(18.dp)
              )
              Text(
                text = "Đã lưu",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = OnSurfaceVariant
              )
            }
          }
        } else {
          // Countdown pill
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(16.dp))
              .then(
                if (milestone.isImportant) {
                  Modifier
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(
                      Brush.verticalGradient(
                        listOf(RoseGradientStart, RoseGradientMid)
                      )
                    )
                } else {
                  Modifier.background(SurfaceContainerHigh)
                }
              )
              .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "CÒN",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (milestone.isImportant) Color.White.copy(alpha = 0.9f) else OnSurfaceVariant
              )
              Text(
                text = "${milestone.daysRemaining}",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (milestone.isImportant) Color.White else Primary,
                lineHeight = 26.sp
              )
              Text(
                text = "ngày",
                fontSize = 9.sp,
                color = if (milestone.isImportant) Color.White.copy(alpha = 0.9f) else OnSurfaceVariant
              )
            }
          }
        }
      }

      // Progress bar if present (e.g. 1000 days)
      if (milestone.progressPercent != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Tiến trình mốc lớn",
            fontSize = 11.sp,
            color = OnSurfaceVariant
          )
          Text(
            text = "${milestone.progressPercent}%",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
          progress = { milestone.progressPercent / 100f },
          modifier = Modifier
            .fillMaxWidth()
            .height(7.dp)
            .clip(RoundedCornerShape(50.dp)),
          color = Primary,
          trackColor = SurfaceContainerHighest,
          strokeCap = StrokeCap.Round
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action Buttons inside Card
      when {
        milestone.id == 1L -> {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(50.dp),
              color = PrimaryFixed.copy(alpha = 0.7f),
              modifier = Modifier
                .weight(1f)
                .clickable { onGiftClick() }
            ) {
              Row(
                modifier = Modifier.padding(vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.CardGiftcard,
                  contentDescription = null,
                  tint = OnPrimaryFixedVariant,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Gợi ý quà tặng",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = OnPrimaryFixedVariant
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(50.dp),
              color = SurfaceContainerHighest.copy(alpha = 0.7f),
              modifier = Modifier.clickable { onEditClick() }
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.EditCalendar,
                  contentDescription = null,
                  tint = OnSurfaceVariant,
                  modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Chỉnh sửa",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = OnSurfaceVariant
                )
              }
            }
          }
        }
        milestone.id == 2L -> {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(50.dp),
              color = SurfaceContainerHighest.copy(alpha = 0.8f),
              modifier = Modifier
                .weight(1f)
                .clickable { onGiftClick() }
            ) {
              Row(
                modifier = Modifier.padding(vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.CardGiftcard,
                  contentDescription = null,
                  tint = Primary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Ý tưởng bất ngờ & Quà",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = OnSurface
                )
              }
            }

            Surface(
              shape = CircleShape,
              color = SurfaceContainerHighest.copy(alpha = 0.8f),
              modifier = Modifier
                .size(36.dp)
                .clickable { onEditClick() }
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Filled.MoreHoriz,
                  contentDescription = "More",
                  tint = OnSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
        milestone.isPast -> {
          Surface(
            shape = RoundedCornerShape(50.dp),
            color = SurfaceContainerLow,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onAlbumClick() }
          ) {
            Row(
              modifier = Modifier.padding(vertical = 9.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Filled.PhotoAlbum,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Xem lại album kỷ niệm (12 ảnh)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
              )
            }
          }
        }
        else -> {
          Surface(
            shape = RoundedCornerShape(50.dp),
            color = PrimaryFixed.copy(alpha = 0.7f),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onEditClick() }
          ) {
            Row(
              modifier = Modifier.padding(vertical = 9.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Filled.EditCalendar,
                contentDescription = null,
                tint = OnPrimaryFixedVariant,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Lên kế hoạch hẹn hò",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = OnPrimaryFixedVariant
              )
            }
          }
        }
      }
    }
  }
}
