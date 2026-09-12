package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FeaturedPlayList
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.CustomReminderEntity
import com.example.data.model.ReminderCadenceEntity
import com.example.ui.theme.OnPrimaryFixed
import com.example.ui.theme.OnSecondaryFixedVariant
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.RoseGradientEnd
import com.example.ui.theme.RoseGradientMid
import com.example.ui.theme.RoseGradientStart
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.viewmodel.InLoveViewModel

@Composable
fun ReminderScreen(
  viewModel: InLoveViewModel,
  onNavigateToGifts: () -> Unit
) {
  val customReminders by viewModel.customReminders.collectAsState()
  val reminderCadences by viewModel.reminderCadences.collectAsState()
  val isAllRead by viewModel.isAllNotificationsRead.collectAsState()
  val sweetNoteLiked by viewModel.sweetNoteLiked.collectAsState()

  var reminderToDelete by remember { mutableStateOf<CustomReminderEntity?>(null) }

  reminderToDelete?.let { reminder ->
    DeleteConfirmationDialog(
      title = "Xóa lời nhắc hẹn hò?",
      message = "Bạn có chắc chắn muốn xóa lời nhắc này không? Lời nhắc và báo thức liên quan sẽ bị hủy bỏ hoàn toàn.",
      itemName = reminder.title,
      onConfirm = {
        viewModel.deleteCustomReminder(reminder.id)
        reminderToDelete = null
      },
      onDismiss = {
        reminderToDelete = null
      }
    )
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Header Section: "THỜI GIAN THỰC - Trung Tâm Nhắc Hẹn"
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(Primary)
            )
            Text(
              text = "THỜI GIAN THỰC",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Primary,
              letterSpacing = 0.5.sp
            )
          }
          Text(
            text = "Trung Tâm Nhắc Hẹn",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // "Đã đọc" button
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isAllRead) SurfaceContainerHighest.copy(alpha = 0.5f) else SurfaceContainerHigh.copy(alpha = 0.8f),
            modifier = Modifier
              .clickable(enabled = !isAllRead) { viewModel.markAllNotificationsRead() }
              .testTag("btn_mark_all_read")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Filled.DoneAll,
                contentDescription = null,
                tint = if (isAllRead) Primary else OnSurfaceVariant,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = if (isAllRead) "Đã xong" else "Đã đọc",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (isAllRead) Primary else OnSurfaceVariant
              )
            }
          }

          // Notification config button
          IconButton(
            onClick = { viewModel.showToast("Cài đặt thông báo sâu đang sẵn sàng") },
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(PrimaryFixed.copy(alpha = 0.6f))
              .testTag("btn_notification_config")
          ) {
            Icon(
              imageVector = Icons.Filled.NotificationsActive,
              contentDescription = "Cấu hình",
              tint = Primary,
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }
    }

    // 2. Quick Reminder Cadence Widget
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = SurfaceContainerLowest.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("reminder_cadence_widget")
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .clip(CircleShape)
                  .background(PrimaryFixed),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Tune,
                  contentDescription = null,
                  tint = Primary,
                  modifier = Modifier.size(16.dp)
                )
              }
              Text(
                text = "Tần suất nhắc nhở tự động",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
            }

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = PrimaryFixed.copy(alpha = 0.5f)
            ) {
              Text(
                text = "${reminderCadences.size} mốc chọn",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Primary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // 2x2 Grid of Cadence Pills
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val chunked = reminderCadences.chunked(2)
            chunked.forEach { rowItems ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                rowItems.forEach { cadence ->
                  CadencePill(
                    cadence = cadence,
                    onToggle = { viewModel.toggleCadence(cadence) },
                    modifier = Modifier.weight(1f)
                  )
                }
                if (rowItems.size == 1) {
                  Spacer(modifier = Modifier.weight(1f))
                }
              }
            }
          }
        }
      }
    }

    // 3. Section: "Hôm Nay - MỚI NHẤT"
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(Primary)
          )
          Text(
            text = "Hôm Nay",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
        }
        Text(
          text = "MỚI NHẤT",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = Primary,
          letterSpacing = 0.5.sp
        )
      }
    }

    // 4. Radiant Milestone Alert Card
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("radiant_milestone_alert_card")
      ) {
        Column(
          modifier = Modifier
            .background(
              Brush.linearGradient(
                listOf(RoseGradientStart, RoseGradientMid, SecondaryContainer)
              )
            )
            .padding(18.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = Color.White.copy(alpha = 0.25f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Filled.Bolt,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(15.dp)
                )
                Text(
                  text = "CỘT MỐC VĨ ĐẠI",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  letterSpacing = 0.5.sp
                )
              }
            }
            Text(
              text = "09:30 AM",
              fontSize = 11.sp,
              color = Color.White.copy(alpha = 0.9f)
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Sắp đến kỷ niệm 1.000 Ngày Yêu!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "Chỉ còn 3 ngày (11/09/2026). Đừng quên chuẩn bị món quà đặc biệt và đặt bàn hẹn hò lãng mạn nhé!",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Couple Visual Mini Banner
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Black.copy(alpha = 0.18f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .shadow(2.dp)
              ) {
                AsyncImage(
                  model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAOlu4K6QXhT8nLJWfbq0s6GYUud8THiOsAPuE7V6o6zr3ovXkkrLO-pMX0czTUP9JXfh1iTCDakXuWnAkmpqDUDkAV_Y0E1m_J19XKChZL52PHYzs5jwQV8UZcyM0_MhhZjbLWA_UDgAWJW0yWSvGlFzhwS4Jxfb1VrHUB4Y7-Uh0DEm3xv3Na77v_iS8kd9ZcBCyNtgnzliY7Ph7QY1MeT20qvW1g6dY2IsP2thJpQTEGi61qdBE_mQ",
                  contentDescription = "Couple",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }
              Column {
                Text(
                  text = "Đức Minh & Khánh Linh",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
                Text(
                  text = "Khoảnh khắc gắn bó 1.000 ngày ngọt ngào",
                  fontSize = 11.sp,
                  color = Color.White.copy(alpha = 0.8f)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Action buttons: "Gợi ý quà" + "Đặt lịch hẹn"
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(50.dp),
              color = Color.White,
              shadowElevation = 2.dp,
              modifier = Modifier
                .weight(1f)
                .clickable { onNavigateToGifts() }
            ) {
              Row(
                modifier = Modifier.padding(vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.FeaturedPlayList,
                  contentDescription = null,
                  tint = Primary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Gợi ý quà",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Primary
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(50.dp),
              color = Color.White.copy(alpha = 0.25f),
              modifier = Modifier
                .weight(1f)
                .clickable { viewModel.showToast("Chuyển sang đặt bàn & không gian hẹn hò...") }
            ) {
              Row(
                modifier = Modifier.padding(vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Filled.Restaurant,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Đặt lịch hẹn",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color.White
                )
              }
            }
          }
        }
      }
    }

    // 5. Daily Sweet Note Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = SurfaceContainerLowest.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.Top,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(SecondaryContainer.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.MarkEmailRead,
              contentDescription = null,
              tint = Secondary,
              modifier = Modifier.size(22.dp)
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "TIN NHẮN YÊU THƯƠNG",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Secondary,
                letterSpacing = 0.5.sp
              )
              Text(
                text = "07:00 AM",
                fontSize = 10.sp,
                color = Outline
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "“Chào buổi sáng công chúa của anh, chúc em một ngày mới ngập tràn năng lượng và nụ cười rạng rỡ!”",
              fontSize = 13.sp,
              fontStyle = FontStyle.Italic,
              color = OnSurface,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (sweetNoteLiked) PrimaryFixed else SurfaceContainerHigh.copy(alpha = 0.6f),
                modifier = Modifier.clickable { viewModel.toggleSweetNoteLike() }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(13.dp)
                  )
                  Text(
                    text = if (sweetNoteLiked) "Đã thích ❤️ (2)" else "Thả tim (1)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Primary
                  )
                }
              }

              Text(
                text = "Từ Anh yêu",
                fontSize = 11.sp,
                color = OnSurfaceVariant
              )
            }
          }
        }
      }
    }

    // 6. Section: "Tuần Này - 2 thông báo"
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(Secondary)
          )
          Text(
            text = "Tuần Này",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
        }
        Text(
          text = "2 thông báo",
          fontSize = 12.sp,
          color = OnSurfaceVariant
        )
      }
    }

    // 7. Birthday Alert Card
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = SurfaceContainerLowest.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.Top,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(SurfaceContainerHighest),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.Cake,
              contentDescription = null,
              tint = Primary,
              modifier = Modifier.size(22.dp)
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Sinh Nhật Khánh Linh",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
              )
              Text(
                text = "Hôm qua",
                fontSize = 10.sp,
                color = Outline
              )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "Còn 15 ngày nữa là đến ngày đặc biệt của Khánh Linh (23/09). Hãy bắt đầu lên ý tưởng quà tặng và lên kế hoạch bí mật ngay!",
              fontSize = 13.sp,
              color = OnSurface,
              lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
              shape = RoundedCornerShape(20.dp),
              color = PrimaryFixed.copy(alpha = 0.6f),
              modifier = Modifier.clickable {
                viewModel.showToast("Đã lưu vào danh sách chuẩn bị quà sinh nhật")
                onNavigateToGifts()
              }
            ) {
              Text(
                text = "Lên danh sách quà",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = OnPrimaryFixed,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }
        }
      }
    }

    // 8. 1-Year Flashback Memory Card with Photo
    item {
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
          containerColor = SurfaceContainerLowest.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(PrimaryFixed.copy(alpha = 0.4f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Filled.History,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(22.dp)
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "KỶ NIỆM 1 NĂM TRƯỚC",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = Secondary,
                  letterSpacing = 0.5.sp
                )
                Text(
                  text = "3 ngày trước",
                  fontSize = 10.sp,
                  color = Outline
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Hành trình du lịch Hội An",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Đúng ngày này 1 năm trước, hai bạn đã cùng nhau thả hoa đăng tại phố cổ Hội An dưới trăng rằm.",
                fontSize = 12.sp,
                color = OnSurfaceVariant,
                lineHeight = 17.sp
              )
            }
          }

          // Photo Preview
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(150.dp)
              .padding(horizontal = 14.dp, vertical = 4.dp)
              .clip(RoundedCornerShape(16.dp))
              .clickable { viewModel.showToast("Mở trọn vẹn album ảnh kỷ niệm Hội An...") }
          ) {
            AsyncImage(
              model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAbOEba4hclQ4bO__pIIi5dVFW8xCl9aYnZLlmcskH8zXTVdcKEpE_jVWdO6cCgPD1GNdHY7iB1UahD2WS0hbs2UCErBRjPSwLHPItn0kXx1YaLqF1Q4Nx64Jn_P_yyG2-CYfajOTTgQCJyNnbmT3dgTGGeJweviOVRZhHDONxa6mTRXDee_0jdMejAXjjSabpZWwIjKbmcAcihx7srP81878WvyKXGSrTUuLke-s3tmNrNhfSaNUQvXg",
              contentDescription = "Hội An Memory",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )

            // Overlay gradient at bottom
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                  Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                  )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Filled.PhotoLibrary,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                  )
                  Text(
                    text = "12 tấm ảnh",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                  )
                }

                Surface(
                  shape = RoundedCornerShape(20.dp),
                  color = Color.White.copy(alpha = 0.3f)
                ) {
                  Text(
                    text = "Xem lại kỷ niệm →",
                    fontSize = 11.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                  )
                }
              }
            }
          }
          Spacer(modifier = Modifier.height(10.dp))
        }
      }
    }

    // 9. Section: "Nhắc Nhở Tùy Chỉnh"
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(Tertiary)
          )
          Text(
            text = "Nhắc Nhở Tùy Chỉnh",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          modifier = Modifier.clickable { viewModel.openAddReminderDialog() }
        ) {
          Icon(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Thêm lời nhắc",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
          )
        }
      }
    }

    // 10. Custom Reminders List
    items(customReminders, key = { it.id }) { reminder ->
      CustomReminderCard(
        reminder = reminder,
        onDelete = { reminderToDelete = reminder },
        onSetAlarm = {
          viewModel.openSetAlarmDialog(
            title = reminder.title,
            message = reminder.details,
            reminderId = reminder.id
          )
        }
      )
    }

    // 11. Big Dashed Button: "+ Tạo lời nhắc hẹn hò riêng của hai bạn"
    item {
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceContainerHigh.copy(alpha = 0.5f),
        modifier = Modifier
          .fillMaxWidth()
          .border(
            width = 1.dp,
            color = OutlineVariant.copy(alpha = 0.8f),
            shape = RoundedCornerShape(20.dp)
          )
          .clickable { viewModel.openAddReminderDialog() }
          .testTag("btn_open_create_reminder")
      ) {
        Row(
          modifier = Modifier.padding(vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Tạo lời nhắc hẹn hò riêng của hai bạn",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
          )
        }
      }
    }
  }
}

@Composable
fun CadencePill(
  cadence: ReminderCadenceEntity,
  onToggle: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(16.dp),
    color = SurfaceContainerHigh.copy(alpha = 0.65f),
    modifier = modifier.clickable { onToggle() }
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        val icon = when (cadence.key) {
          "7_days" -> Icons.Filled.Schedule
          "3_days" -> Icons.Filled.Notifications
          "1_day" -> Icons.Filled.Alarm
          else -> Icons.Filled.Favorite
        }
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = if (cadence.isEnabled) Primary else Outline,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = cadence.label,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = OnSurface
        )
      }

      if (cadence.isEnabled) {
        Box(
          modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(Primary),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(12.dp)
          )
        }
      } else {
        Box(
          modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(OutlineVariant.copy(alpha = 0.6f))
        )
      }
    }
  }
}

@Composable
fun CustomReminderCard(
  reminder: CustomReminderEntity,
  onDelete: () -> Unit,
  onSetAlarm: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(18.dp),
    color = SurfaceContainerLow.copy(alpha = 0.9f),
    shadowElevation = 1.dp,
    modifier = Modifier
      .fillMaxWidth()
      .testTag("custom_reminder_${reminder.id}")
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
        val icon = when (reminder.iconType) {
          "movie" -> Icons.Filled.Movie
          "hand" -> Icons.Filled.FrontHand
          else -> Icons.Filled.Stars
        }
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(if (reminder.iconType == "movie") SecondaryFixed else PrimaryFixed),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (reminder.iconType == "movie") OnSecondaryFixedVariant else Primary,
            modifier = Modifier.size(18.dp)
          )
        }

        Column {
          Text(
            text = reminder.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = "${reminder.dateText} • ${reminder.details}",
            fontSize = 11.sp,
            color = OnSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = SurfaceContainerHighest
        ) {
          Text(
            text = reminder.daysRemainingText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
          )
        }

        IconButton(
          onClick = onSetAlarm,
          modifier = Modifier
            .size(28.dp)
            .testTag("btn_alarm_reminder_${reminder.id}")
        ) {
          Icon(
            imageVector = Icons.Filled.Alarm,
            contentDescription = "Cài báo thức",
            tint = Primary,
            modifier = Modifier.size(16.dp)
          )
        }

        IconButton(
          onClick = onDelete,
          modifier = Modifier
            .size(28.dp)
            .testTag("btn_delete_reminder_${reminder.id}")
        ) {
          Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Xóa",
            tint = Outline,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    }
  }
}
