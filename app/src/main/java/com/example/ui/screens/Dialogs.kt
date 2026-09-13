package com.example.ui.screens

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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.GiftIdeaEntity
import com.example.ui.theme.OnPrimaryFixed
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.RoseGradientEnd
import com.example.ui.theme.RoseGradientMid
import com.example.ui.theme.RoseGradientStart
import com.example.ui.theme.Secondary
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.components.DatePickerPresets
import com.example.ui.components.DatePickerUtils
import com.example.ui.components.InLoveDatePickerDialog
import com.example.ui.components.InLoveDatePickerField
import com.example.ui.util.ProfileUtils

@Composable
fun AddReminderDialog(
  onDismiss: () -> Unit,
  onConfirm: (title: String, dateText: String, note: String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var dateText by remember { mutableStateOf("Thứ Bảy, 19:30") }
  var note by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Filled.Stars,
          contentDescription = null,
          tint = Primary
        )
        Text(
          text = "Tạo Lời Nhắc Hẹn",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Tên buổi hẹn / kỷ niệm") },
          placeholder = { Text("Ví dụ: Hẹn ăn tối lãng mạn") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_reminder_title"),
          singleLine = true
        )

        OutlinedTextField(
          value = dateText,
          onValueChange = { dateText = it },
          label = { Text("Thời gian") },
          placeholder = { Text("Ví dụ: Cuối tuần này, 19:00") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_reminder_date"),
          singleLine = true
        )

        OutlinedTextField(
          value = note,
          onValueChange = { note = it },
          label = { Text("Địa điểm / Ghi chú") },
          placeholder = { Text("Ví dụ: Quán cafe quen, nhớ mang hoa") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_reminder_note"),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(title, dateText, note) },
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        modifier = Modifier.testTag("btn_save_reminder")
      ) {
        Text("Lưu lời nhắc", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Hủy", color = OnSurfaceVariant)
      }
    }
  )
}

@Composable
fun AddMilestoneDialog(
  onDismiss: () -> Unit,
  onConfirm: (
    title: String,
    dateText: String,
    subtitle: String,
    categoryTag: String,
    secondaryTag: String,
    imageUrl: String,
    daysRemaining: Int,
    isImportant: Boolean
  ) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var dateText by remember { mutableStateOf("15 Tháng 11, 2026") }
  var subtitle by remember { mutableStateOf("Chủ Nhật") }
  var categoryTag by remember { mutableStateOf("Hẹn Hò") }
  var daysRemainingStr by remember { mutableStateOf("67") }
  var isImportant by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Filled.CalendarMonth,
          contentDescription = null,
          tint = Primary
        )
        Text(
          text = "Thêm Kỷ Niệm Mới",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Tiêu đề sự kiện") },
          placeholder = { Text("Ví dụ: Kỷ niệm chuyến đi Đà Lạt") },
          modifier = Modifier.fillMaxWidth().testTag("input_milestone_title"),
          singleLine = true
        )

        InLoveDatePickerField(
          value = dateText,
          onValueChange = { newDate ->
            dateText = newDate
            val parsedMillis = DatePickerUtils.parseDateToUtcMillis(newDate)
            if (parsedMillis != null) {
              val now = System.currentTimeMillis()
              val diff = parsedMillis - now
              val days = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diff).toInt()
              daysRemainingStr = Math.max(0, days).toString()
            }
          },
          label = "Ngày tháng sự kiện (dd/MM/yyyy) *",
          placeholder = "15/11/2026",
          dialogTitle = "Chọn ngày sự kiện / kỷ niệm",
          quickPresets = DatePickerPresets.upcomingAnniversaryPresets(),
          helperText = DatePickerUtils.getFriendlyDateDescription(dateText),
          modifier = Modifier.fillMaxWidth(),
          testTag = "input_milestone_date"
        )

        OutlinedTextField(
          value = categoryTag,
          onValueChange = { categoryTag = it },
          label = { Text("Thẻ phân loại") },
          placeholder = { Text("Ví dụ: Du Lịch / Sinh Nhật / Tình Yêu") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = daysRemainingStr,
          onValueChange = { daysRemainingStr = it },
          label = { Text("Số ngày còn lại (hoặc âm nếu đã qua)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = "Đánh dấu là mốc Quan Trọng ⭐", fontSize = 13.sp)
          Switch(
            checked = isImportant,
            onCheckedChange = { isImportant = it },
            colors = SwitchDefaults.colors(checkedTrackColor = Primary)
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val days = daysRemainingStr.toIntOrNull() ?: 30
          onConfirm(
            title,
            dateText,
            subtitle,
            categoryTag,
            "Cột mốc mới",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuANA2ChG6LS0d4msPLYL4g-4W2BU_q52b1udp8NDaY4NJSzyw4NZnx6e2qKT1oMKzYrc76_1-nMndDIrMSO7k1QXvz66V8WEt7D3GuZmigotLqTpeJbbAdYrKyOPyUV1W-RxHRZbCo09c24vQC-5ZIS2iG1PM6s7V5_nejLv9V0-tTQujYKsbrgGRbfxlS_JvPgXqa_zXWfABTSL3rCM-VVaw2iIyPJ43vA8jK5bjWCrVYznx4hzUS6_w",
            days,
            isImportant
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        modifier = Modifier.testTag("btn_submit_milestone")
      ) {
        Text("Tạo Kỷ Niệm", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Hủy", color = OnSurfaceVariant)
      }
    }
  )
}

@Composable
fun AddChecklistDialog(
  onDismiss: () -> Unit,
  onConfirm: (text: String) -> Unit
) {
  var text by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Thêm Việc Vào Checklist",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
      )
    },
    text = {
      OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Tên công việc") },
        placeholder = { Text("Ví dụ: Mua bó hoa hồng baby trắng") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("input_checklist_task"),
        singleLine = true
      )
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(text) },
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        modifier = Modifier.testTag("btn_save_checklist_task")
      ) {
        Text("Thêm", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Hủy", color = OnSurfaceVariant)
      }
    }
  )
}

@Composable
fun GiftDetailDialog(
  gift: GiftIdeaEntity,
  onDismiss: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp)
    ) {
      Column(
        modifier = Modifier
          .verticalScroll(rememberScrollState())
          .padding(18.dp)
      ) {
        // Image with close button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(18.dp))
        ) {
          AsyncImage(
            model = gift.imageUrl,
            contentDescription = gift.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(8.dp)
              .size(34.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.5f))
          ) {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Close",
              tint = Color.White,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = PrimaryFixed.copy(alpha = 0.7f)
        ) {
          Text(
            text = gift.category,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = gift.title,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = OnSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = gift.description,
          fontSize = 13.sp,
          color = OnSurfaceVariant,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
          shape = RoundedCornerShape(14.dp),
          color = SurfaceContainerLowest,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "CHI TIẾT THỰC HIỆN",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Primary,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (gift.detailsSnippet.isNotBlank()) gift.detailsSnippet else "Món quà tuyệt vời lưu lại khoảnh khắc gắn kết của hai bạn.",
              fontSize = 12.sp,
              color = OnSurface
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(50.dp),
            color = PrimaryFixed.copy(alpha = 0.5f),
            modifier = Modifier
              .weight(1f)
              .clickable { onToggleFavorite() }
          ) {
            Row(
              modifier = Modifier.padding(vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (gift.isFavorited) "Đã lưu" else "Yêu thích",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Primary
              )
            }
          }

          Button(
            onClick = onDismiss,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            modifier = Modifier.weight(1f)
          ) {
            Text("Đã rõ", fontWeight = FontWeight.Bold, fontSize = 13.sp)
          }
        }
      }
    }
  }
}

@Composable
fun VipProposalDialog(
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(26.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
    ) {
      Column(
        modifier = Modifier
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = PrimaryFixed
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "GÓI ĐỀ XUẤT VIP",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(SurfaceContainerHigh)
          ) {
            Icon(
              imageVector = Icons.Filled.Close,
              contentDescription = "Close",
              tint = OnSurfaceVariant,
              modifier = Modifier.size(18.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Gói Kỷ Niệm 1.000 Ngày Hoàn Hảo",
          fontSize = 19.sp,
          fontWeight = FontWeight.Bold,
          color = OnSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Lịch trình trọn gói được thiết kế riêng giúp bạn tạo nên một đêm kỷ niệm lãng mạn không thể nào quên.",
          fontSize = 13.sp,
          color = OnSurfaceVariant,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Itinerary Timeline Steps
        val steps = listOf(
          Triple("18:30", "Đón Nàng & Bó Hoa Vĩnh Cửu", "Bất ngờ tặng bó hoa hồng đỏ vĩnh cửu lồng kính pha lê kèm thiệp viết tay chân thành."),
          Triple("19:00", "Bữa Tối Rooftop Lung Linh", "Bàn ăn riêng tư tại tầng 35 ngắm trọn vẹn ánh đèn thành phố lung linh."),
          Triple("20:30", "Khoảnh Khắc Bánh Kem & Thổi Nến", "Bật bài hát kỷ niệm của hai bạn, cùng nhau nhìn lại album 1.000 ngày."),
          Triple("21:30", "Dạo Phố & Hộp Quà Dây Chuyền Bạc", "Trao món quà nhỏ xinh khắc ngày đầu tiên gặp gỡ.")
        )

        steps.forEachIndexed { index, (time, title, desc) ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Surface(
                shape = CircleShape,
                color = Primary,
                modifier = Modifier.size(24.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Text(
                    text = "${index + 1}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
              if (index < steps.size - 1) {
                Box(
                  modifier = Modifier
                    .width(2.dp)
                    .height(44.dp)
                    .background(PrimaryFixed)
                )
              }
            }

            Column(modifier = Modifier.padding(bottom = 12.dp)) {
              Text(
                text = "$time • $title",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = desc,
                fontSize = 12.sp,
                color = OnSurfaceVariant,
                lineHeight = 16.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = onDismiss,
          shape = RoundedCornerShape(50.dp),
          colors = ButtonDefaults.buttonColors(containerColor = Primary),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Lưu Kế Hoạch Này", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun EditCoupleDialog(
  currentBoyName: String,
  currentBoyBirth: String,
  currentBoyAvatar: String,
  currentBoyAge: Int,
  currentBoyZodiac: String,
  currentGirlName: String,
  currentGirlBirth: String,
  currentGirlAvatar: String,
  currentGirlAge: Int,
  currentGirlZodiac: String,
  currentTitle: String,
  currentDays: Int,
  currentAnniversary: String = "18/12/2022",
  onDismiss: () -> Unit,
  onSave: (
    boy: String,
    boyBirth: String,
    boyAvatar: String,
    boyAge: Int,
    boyZodiac: String,
    girl: String,
    girlBirth: String,
    girlAvatar: String,
    girlAge: Int,
    girlZodiac: String,
    title: String,
    days: Int,
    anniversary: String
  ) -> Unit
) {
  var boyName by remember { mutableStateOf(currentBoyName) }
  var boyBirth by remember { mutableStateOf(currentBoyBirth) }
  var boyAvatar by remember { mutableStateOf(currentBoyAvatar) }
  var boyAgeStr by remember { mutableStateOf(currentBoyAge.toString()) }
  var boyZodiac by remember { mutableStateOf(currentBoyZodiac) }

  var girlName by remember { mutableStateOf(currentGirlName) }
  var girlBirth by remember { mutableStateOf(currentGirlBirth) }
  var girlAvatar by remember { mutableStateOf(currentGirlAvatar) }
  var girlAgeStr by remember { mutableStateOf(currentGirlAge.toString()) }
  var girlZodiac by remember { mutableStateOf(currentGirlZodiac) }

  var title by remember { mutableStateOf(currentTitle) }
  var daysStr by remember { mutableStateOf(currentDays.toString()) }
  var anniversary by remember { mutableStateOf(currentAnniversary) }

  val boyPresets = remember {
    listOf(
      "https://lh3.googleusercontent.com/aida-public/AB6AXuCg-PmA8kAH3aEsx4nS5akuDkkWQeWMutmW8Lc76ASO-JMvtiwMNbsTfuYqBGpez7bHTAYekQNilJ5X5BHaP78pQf4tATX48UynvtOeQWG8kcCF-v9OqIdcm1OAjLGtZsO1ygTFLVd9qW-yngUnwCmOtlFWn_wzhCPvYfzMcFLVzeEoOX9NuP8fk911cjdlzd0yv0FvSo3h7qg26BWqyaqSpVwTuvKoKaZ20NJLOUWeBfHbRlqMZcIwfI1FL31cw1WGBrQ",
      "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=400&auto=format&fit=crop",
      "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=400&auto=format&fit=crop",
      "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=400&auto=format&fit=crop"
    )
  }

  val girlPresets = remember {
    listOf(
      "https://lh3.googleusercontent.com/aida-public/AB6AXuA7FGhAVlT3aua6tzpkBgh-_XXVsY-hCGooIPxOJ0acYyvZV9FZpASsMTgGO0vBFJLrLojRgwwnQEjfWAniwj2FAiKNpUjSRFNIQgmOhVN8fFSYwpOqLF8hAnWk3UM0dWxSVT1FwaK9ovXMP7Jwi-gjfRbUm2ISX1qyUY6bpBKBZnBYk7YIorizTPWI5c6w9XUdTTsMLFWT3c5ns8lYQooC0eEvt6S5zJNNobW_pn1PTJOb0K2REgdfTQ",
      "https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=400&auto=format&fit=crop",
      "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=400&auto=format&fit=crop",
      "https://images.unsplash.com/photo-1517841905240-472988babdf9?q=80&w=400&auto=format&fit=crop"
    )
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Primary
          )
          Text(
            text = "Hồ Sơ Cặp Đôi (Room DB)",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        }
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Primary.copy(alpha = 0.1f)
        ) {
          Text(
            text = "💾 Lưu trữ cục bộ SQLite an toàn trên thiết bị",
            fontSize = 11.sp,
            color = Primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // --- 1. PARTNER 1 (BẠN NAM) ---
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4).copy(alpha = 0.85f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(68.dp)
                  .clip(CircleShape)
                  .border(2.5.dp, Color(0xFF4DD0E1), CircleShape)
              ) {
                AsyncImage(
                  model = boyAvatar,
                  contentDescription = "Ảnh đại diện bạn nam",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  "Bạn Nam / Đối Tác 1",
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF00838F),
                  fontSize = 14.sp
                )
                Text(
                  "Nhập tên, ngày sinh và ảnh đại diện",
                  fontSize = 11.sp,
                  color = OnSurfaceVariant
                )
              }
            }

            OutlinedTextField(
              value = boyName,
              onValueChange = { boyName = it },
              label = { Text("Tên bạn nam") },
              modifier = Modifier.fillMaxWidth().testTag("partner1_name_input"),
              singleLine = true
            )

            InLoveDatePickerField(
              value = boyBirth,
              onValueChange = { newBirth ->
                boyBirth = newBirth
                val calculatedAge = ProfileUtils.calculateAge(newBirth)
                if (calculatedAge > 0) boyAgeStr = calculatedAge.toString()
                val (zodiacName, _) = ProfileUtils.calculateZodiac(newBirth)
                if (zodiacName != "Chưa rõ") boyZodiac = zodiacName
              },
              label = "Ngày sinh bạn nam (dd/MM/yyyy)",
              placeholder = "15/10/2004",
              dialogTitle = "Chọn ngày sinh bạn nam",
              modifier = Modifier.fillMaxWidth(),
              testTag = "partner1_birthday_input"
            )

            OutlinedTextField(
              value = boyAvatar,
              onValueChange = { boyAvatar = it },
              label = { Text("URL Ảnh đại diện bạn nam") },
              modifier = Modifier.fillMaxWidth().testTag("partner1_avatar_input"),
              singleLine = true
            )

            // Preset Avatars for Partner 1
            Column {
              Text(
                "Hoặc chọn ảnh đại diện mẫu:",
                fontSize = 11.sp,
                color = OnSurfaceVariant,
                fontWeight = FontWeight.Medium
              )
              Spacer(modifier = Modifier.height(4.dp))
              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                boyPresets.forEachIndexed { index, url ->
                  val isSelected = boyAvatar == url
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .border(
                        width = if (isSelected) 2.5.dp else 1.dp,
                        color = if (isSelected) Color(0xFF00838F) else Color.LightGray,
                        shape = CircleShape
                      )
                      .clickable { boyAvatar = url }
                      .testTag("partner1_preset_$index")
                  ) {
                    AsyncImage(
                      model = url,
                      contentDescription = "Preset $index",
                      contentScale = ContentScale.Crop,
                      modifier = Modifier.fillMaxSize()
                    )
                  }
                }
              }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              OutlinedTextField(
                value = boyAgeStr,
                onValueChange = { boyAgeStr = it },
                label = { Text("Tuổi") },
                modifier = Modifier.weight(1f).testTag("partner1_age_input"),
                singleLine = true
              )
              OutlinedTextField(
                value = boyZodiac,
                onValueChange = { boyZodiac = it },
                label = { Text("Cung hoàng đạo") },
                modifier = Modifier.weight(1.5f).testTag("partner1_zodiac_input"),
                singleLine = true
              )
            }
          }
        }

        // --- 2. PARTNER 2 (BẠN NỮ) ---
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2).copy(alpha = 0.85f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(68.dp)
                  .clip(CircleShape)
                  .border(2.5.dp, Color(0xFFFF8A65), CircleShape)
              ) {
                AsyncImage(
                  model = girlAvatar,
                  contentDescription = "Ảnh đại diện bạn nữ",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize()
                )
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  "Bạn Nữ / Đối Tác 2",
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFC2185B),
                  fontSize = 14.sp
                )
                Text(
                  "Nhập tên, ngày sinh và ảnh đại diện",
                  fontSize = 11.sp,
                  color = OnSurfaceVariant
                )
              }
            }

            OutlinedTextField(
              value = girlName,
              onValueChange = { girlName = it },
              label = { Text("Tên bạn nữ") },
              modifier = Modifier.fillMaxWidth().testTag("partner2_name_input"),
              singleLine = true
            )

            InLoveDatePickerField(
              value = girlBirth,
              onValueChange = { newBirth ->
                girlBirth = newBirth
                val calculatedAge = ProfileUtils.calculateAge(newBirth)
                if (calculatedAge > 0) girlAgeStr = calculatedAge.toString()
                val (zodiacName, _) = ProfileUtils.calculateZodiac(newBirth)
                if (zodiacName != "Chưa rõ") girlZodiac = zodiacName
              },
              label = "Ngày sinh bạn nữ (dd/MM/yyyy)",
              placeholder = "24/07/2003",
              dialogTitle = "Chọn ngày sinh bạn nữ",
              modifier = Modifier.fillMaxWidth(),
              testTag = "partner2_birthday_input"
            )

            OutlinedTextField(
              value = girlAvatar,
              onValueChange = { girlAvatar = it },
              label = { Text("URL Ảnh đại diện bạn nữ") },
              modifier = Modifier.fillMaxWidth().testTag("partner2_avatar_input"),
              singleLine = true
            )

            // Preset Avatars for Partner 2
            Column {
              Text(
                "Hoặc chọn ảnh đại diện mẫu:",
                fontSize = 11.sp,
                color = OnSurfaceVariant,
                fontWeight = FontWeight.Medium
              )
              Spacer(modifier = Modifier.height(4.dp))
              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                girlPresets.forEachIndexed { index, url ->
                  val isSelected = girlAvatar == url
                  Box(
                    modifier = Modifier
                      .size(36.dp)
                      .clip(CircleShape)
                      .border(
                        width = if (isSelected) 2.5.dp else 1.dp,
                        color = if (isSelected) Color(0xFFC2185B) else Color.LightGray,
                        shape = CircleShape
                      )
                      .clickable { girlAvatar = url }
                      .testTag("partner2_preset_$index")
                  ) {
                    AsyncImage(
                      model = url,
                      contentDescription = "Preset $index",
                      contentScale = ContentScale.Crop,
                      modifier = Modifier.fillMaxSize()
                    )
                  }
                }
              }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              OutlinedTextField(
                value = girlAgeStr,
                onValueChange = { girlAgeStr = it },
                label = { Text("Tuổi") },
                modifier = Modifier.weight(1f).testTag("partner2_age_input"),
                singleLine = true
              )
              OutlinedTextField(
                value = girlZodiac,
                onValueChange = { girlZodiac = it },
                label = { Text("Cung hoàng đạo") },
                modifier = Modifier.weight(1.5f).testTag("partner2_zodiac_input"),
                singleLine = true
              )
            }
          }
        }

        // --- 3. TIÊU ĐỀ & NGÀY YÊU ---
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Text(
              "Thông Tin Kỷ Niệm Yêu",
              fontWeight = FontWeight.Bold,
              color = Color(0xFF7B1FA2),
              fontSize = 14.sp
            )
            OutlinedTextField(
              value = title,
              onValueChange = { title = it },
              label = { Text("Tiêu đề (VD: Bámmmm / Bên nhau)") },
              modifier = Modifier.fillMaxWidth().testTag("edit_love_title"),
              singleLine = true
            )
            InLoveDatePickerField(
              value = anniversary,
              onValueChange = { newAnniv ->
                anniversary = newAnniv
                val calculatedDays = ProfileUtils.calculateLoveDays(newAnniv)
                daysStr = calculatedDays.toString()
              },
              label = "Ngày bắt đầu yêu (dd/MM/yyyy) *",
              placeholder = "18/12/2022",
              dialogTitle = "Chọn ngày bắt đầu yêu",
              quickPresets = DatePickerPresets.relationshipStartDatePresets(),
              helperText = DatePickerUtils.getFriendlyDateDescription(anniversary),
              modifier = Modifier.fillMaxWidth(),
              testTag = "edit_anniversary_date"
            )
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              TextButton(
                onClick = {
                  val formats = listOf("dd/MM/yyyy", "d/M/yyyy", "dd-MM-yyyy")
                  for (fmt in formats) {
                    try {
                      val sdf = java.text.SimpleDateFormat(fmt, java.util.Locale.getDefault()).apply { isLenient = false }
                      val d = sdf.parse(anniversary.trim())
                      if (d != null) {
                        val startCal = java.util.Calendar.getInstance().apply {
                          time = d
                          set(java.util.Calendar.HOUR_OF_DAY, 0)
                          set(java.util.Calendar.MINUTE, 0)
                          set(java.util.Calendar.SECOND, 0)
                          set(java.util.Calendar.MILLISECOND, 0)
                        }
                        val nowCal = java.util.Calendar.getInstance()
                        val diff = nowCal.timeInMillis - startCal.timeInMillis
                        if (diff >= 0) {
                          val days = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diff) + 1
                          daysStr = days.toString()
                          break
                        }
                      }
                    } catch (_: Exception) {}
                  }
                }
              ) {
                Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tính số ngày từ ngày yêu", fontSize = 12.sp, color = Primary)
              }
            }
            OutlinedTextField(
              value = daysStr,
              onValueChange = { daysStr = it },
              label = { Text("Số ngày yêu (VD: 1349)") },
              modifier = Modifier.fillMaxWidth().testTag("edit_love_days"),
              singleLine = true
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onSave(
            boyName,
            boyBirth,
            boyAvatar,
            boyAgeStr.toIntOrNull() ?: 20,
            boyZodiac,
            girlName,
            girlBirth,
            girlAvatar,
            girlAgeStr.toIntOrNull() ?: 21,
            girlZodiac,
            title,
            daysStr.toIntOrNull() ?: 1349,
            anniversary
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        modifier = Modifier.testTag("btn_save_couple_profile_room")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(Icons.Filled.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
          Text("Lưu Vào Room DB", fontWeight = FontWeight.Bold)
        }
      }
    },
    dismissButton = {
      TextButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("btn_cancel_couple_profile")
      ) {
        Text("Hủy", color = OnSurfaceVariant)
      }
    }
  )
}

@Composable
fun DeleteConfirmationDialog(
  title: String,
  message: String,
  itemName: String? = null,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(Color(0xFFFFEBEE)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Filled.WarningAmber,
          contentDescription = null,
          tint = Color(0xFFE53935),
          modifier = Modifier.size(28.dp)
        )
      }
    },
    title = {
      Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = Color(0xFF26071B)
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = message,
          fontSize = 14.sp,
          lineHeight = 20.sp,
          color = OnSurfaceVariant
        )
        if (!itemName.isNullOrBlank()) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFFF0F5),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = itemName,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF26071B)
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onConfirm,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("btn_confirm_delete")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.DeleteForever,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.White
          )
          Text("Xóa Vĩnh Viễn", fontWeight = FontWeight.Bold, color = Color.White)
        }
      }
    },
    dismissButton = {
      TextButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("btn_cancel_delete")
      ) {
        Text("Hủy bỏ", color = OnSurfaceVariant, fontWeight = FontWeight.Medium)
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(24.dp),
    modifier = Modifier.testTag("dialog_confirm_delete")
  )
}

@Composable
fun SetAlarmReminderDialog(
  initialTitle: String = "",
  initialMessage: String = "",
  reminderId: Long? = null,
  onDismiss: () -> Unit,
  onSchedule: (title: String, message: String, triggerMillis: Long, reminderId: Long) -> Unit,
  onTestNow: (title: String, message: String) -> Unit
) {
  var title by remember {
    mutableStateOf(initialTitle.ifBlank { "Kỷ niệm tình yêu ngọt ngào ❤️" })
  }
  var message by remember {
    mutableStateOf(initialMessage.ifBlank { "Đã đến thời gian kỷ niệm đặc biệt! Hãy gửi lời yêu thương đến người ấy nhé 🌸" })
  }

  // Selected preset index: 0 = 10 sec test, 1 = 9:00 AM, 2 = 19:30 PM, 3 = In 2 Hours, 4 = Custom
  var selectedPreset by remember { mutableIntStateOf(1) }
  var customHour by remember { mutableIntStateOf(9) }
  var customMinute by remember { mutableIntStateOf(0) }

  fun calculateTriggerMillis(preset: Int, hour: Int, minute: Int): Long {
    val now = System.currentTimeMillis()
    return when (preset) {
      0 -> now + 10_000L // 10 seconds (instant test)
      3 -> now + 2 * 3600 * 1000L // 2 hours
      else -> {
        val calendar = java.util.Calendar.getInstance()
        val targetH = if (preset == 1) 9 else if (preset == 2) 19 else hour
        val targetM = if (preset == 1) 0 else if (preset == 2) 30 else minute
        calendar.set(java.util.Calendar.HOUR_OF_DAY, targetH)
        calendar.set(java.util.Calendar.MINUTE, targetM)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        if (calendar.timeInMillis <= now) {
          calendar.add(java.util.Calendar.DAY_OF_YEAR, 1) // next day
        }
        calendar.timeInMillis
      }
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(Color(0xFFFFF0F5)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Filled.NotificationsActive,
          contentDescription = null,
          tint = Primary,
          modifier = Modifier.size(26.dp)
        )
      }
    },
    title = {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = "Cài Đặt Báo Thức Kỷ Niệm",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = Color(0xFF26071B)
        )
        Text(
          text = "Nhận chuông và thông báo vào thời gian chính xác",
          fontSize = 12.sp,
          color = OnSurfaceVariant
        )
      }
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Tiêu đề báo thức") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_alarm_title"),
          singleLine = true
        )

        OutlinedTextField(
          value = message,
          onValueChange = { message = it },
          label = { Text("Lời nhắn kèm thông báo") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_alarm_message"),
          maxLines = 3
        )

        Text(
          text = "Chọn thời gian báo thức:",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = Color(0xFF26071B)
        )

        // Presets Chips
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          val presets = listOf(
            "⏰ 9:00 AM (Sáng mai)" to 1,
            "🌙 19:30 PM (Tối)" to 2,
            "⏳ Sau 2 tiếng" to 3,
            "⚡ Sau 10 giây (Thử chuông)" to 0,
            "⚙️ Tùy chọn giờ cụ thể" to 4
          )
          presets.forEach { (label, index) ->
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (selectedPreset == index) Primary.copy(alpha = 0.12f) else Color(0xFFF7F2FA),
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (selectedPreset == index) Primary else Color(0xFFE7E0EC)
              ),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedPreset = index }
                .testTag("preset_alarm_$index")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = label,
                  fontSize = 13.sp,
                  fontWeight = if (selectedPreset == index) FontWeight.Bold else FontWeight.Normal,
                  color = if (selectedPreset == index) Primary else Color(0xFF49454F)
                )
                if (selectedPreset == index) {
                  Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(14.dp)
                  )
                }
              }
            }
          }
        }

        // Custom time picker row if custom is selected
        if (selectedPreset == 4) {
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFFF0F5),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceAround
            ) {
              // Hours
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Giờ", fontSize = 11.sp, color = OnSurfaceVariant)
                Row(verticalAlignment = Alignment.CenterVertically) {
                  IconButton(
                    onClick = { customHour = (customHour - 1 + 24) % 24 },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Primary)
                  }
                  Text(
                    text = String.format("%02d", customHour),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF26071B),
                    modifier = Modifier.padding(horizontal = 6.dp)
                  )
                  IconButton(
                    onClick = { customHour = (customHour + 1) % 24 },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Primary)
                  }
                }
              }

              Text(":", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Primary)

              // Minutes
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Phút", fontSize = 11.sp, color = OnSurfaceVariant)
                Row(verticalAlignment = Alignment.CenterVertically) {
                  IconButton(
                    onClick = { customMinute = (customMinute - 5 + 60) % 60 },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Primary)
                  }
                  Text(
                    text = String.format("%02d", customMinute),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF26071B),
                    modifier = Modifier.padding(horizontal = 6.dp)
                  )
                  IconButton(
                    onClick = { customMinute = (customMinute + 5) % 60 },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Primary)
                  }
                }
              }
            }
          }
        }

        // Test instant notification button
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFFFF0F5),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onTestNow(title, message) }
            .testTag("btn_test_alarm_instant")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Filled.NotificationsActive,
              contentDescription = null,
              tint = Primary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "🔔 Thử chuông thông báo ngay",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Primary
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val trigger = calculateTriggerMillis(selectedPreset, customHour, customMinute)
          val id = reminderId ?: System.currentTimeMillis()
          onSchedule(title, message, trigger, id)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("btn_confirm_schedule_alarm")
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.AlarmOn,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.White
          )
          Text("Đặt Báo Thức", fontWeight = FontWeight.Bold, color = Color.White)
        }
      }
    },
    dismissButton = {
      TextButton(
        onClick = onDismiss,
        modifier = Modifier.testTag("btn_cancel_schedule_alarm")
      ) {
        Text("Đóng", color = OnSurfaceVariant)
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(24.dp),
    modifier = Modifier.testTag("dialog_set_alarm_reminder")
  )
}

@Composable
fun AddAnniversaryDateDialog(
  onDismiss: () -> Unit,
  onConfirm: (
    title: String,
    dateText: String,
    type: String,
    description: String,
    isAnnual: Boolean,
    reminderDaysBefore: Int
  ) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var dateText by remember { mutableStateOf("18/12/2022") }
  var selectedType by remember { mutableStateOf("LOVE") }
  var description by remember { mutableStateOf("") }
  var isAnnual by remember { mutableStateOf(true) }
  var reminderDaysBefore by remember { mutableIntStateOf(3) }

  val typeOptions = listOf(
    "LOVE" to "Ngày Bắt Đầu Yêu",
    "FIRST_DATE" to "Hẹn Hò Đầu Tiên",
    "FIRST_KISS" to "Nụ Hôn Đầu Tiên",
    "PROPOSAL" to "Cầu Hôn",
    "WEDDING" to "Đám Cưới",
    "CUSTOM" to "Kỷ Niệm Riêng"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Filled.Favorite,
          contentDescription = null,
          tint = Primary
        )
        Text(
          text = "Thêm Ngày Kỷ Niệm Mới",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = OnSurface
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Tên ngày kỷ niệm *") },
          placeholder = { Text("Ví dụ: Ngày chính thức yêu nhau") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_anniversary_title"),
          singleLine = true
        )

        InLoveDatePickerField(
          value = dateText,
          onValueChange = { dateText = it },
          label = "Ngày kỷ niệm (dd/MM/yyyy) *",
          placeholder = "18/12/2022",
          dialogTitle = "Chọn ngày kỷ niệm",
          quickPresets = DatePickerPresets.upcomingAnniversaryPresets(),
          helperText = DatePickerUtils.getFriendlyDateDescription(dateText),
          modifier = Modifier.fillMaxWidth(),
          testTag = "input_anniversary_date"
        )

        Text(
          text = "Loại kỷ niệm:",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = OnSurfaceVariant
        )

        // Type selection chips
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          typeOptions.take(3).forEach { (typeKey, label) ->
            val isSelected = selectedType == typeKey
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = if (isSelected) Primary else SurfaceContainerHigh,
              modifier = Modifier
                .weight(1f)
                .clickable { selectedType = typeKey }
            ) {
              Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else OnSurface,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1
              )
            }
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          typeOptions.drop(3).forEach { (typeKey, label) ->
            val isSelected = selectedType == typeKey
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = if (isSelected) Primary else SurfaceContainerHigh,
              modifier = Modifier
                .weight(1f)
                .clickable { selectedType = typeKey }
            ) {
              Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else OnSurface,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1
              )
            }
          }
        }

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Ghi chú / Cảm xúc") },
          placeholder = { Text("Khoảnh khắc đáng nhớ nhất...") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_anniversary_desc"),
          maxLines = 3
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Lặp lại hàng năm:",
            fontSize = 13.sp,
            color = OnSurface
          )
          Switch(
            checked = isAnnual,
            onCheckedChange = { isAnnual = it },
            colors = SwitchDefaults.colors(
              checkedThumbColor = Color.White,
              checkedTrackColor = Primary
            )
          )
        }

        Text(
          text = "Hẹn giờ thông báo trước:",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = OnSurface
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(0 to "Đúng ngày", 1 to "1 ngày", 3 to "3 ngày", 7 to "7 ngày").forEach { (days, label) ->
            val isSelected = reminderDaysBefore == days
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) Primary else SurfaceContainerHigh,
              modifier = Modifier
                .weight(1f)
                .clickable { reminderDaysBefore = days }
            ) {
              Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else OnSurface,
                modifier = Modifier.padding(vertical = 7.dp, horizontal = 2.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onConfirm(title, dateText, selectedType, description, isAnnual, reminderDaysBefore)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        modifier = Modifier.testTag("btn_save_anniversary_date")
      ) {
        Text("Lưu Kỷ Niệm (Room)", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Hủy", color = OnSurfaceVariant)
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(24.dp),
    modifier = Modifier.testTag("dialog_add_anniversary_date")
  )
}

@Composable
fun AddGiftReminderDialog(
  onDismiss: () -> Unit,
  onConfirm: (
    title: String,
    recipient: String,
    occasion: String,
    dueDateText: String,
    estimatedBudget: String,
    notes: String
  ) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var recipient by remember { mutableStateOf("Người ấy") }
  var occasion by remember { mutableStateOf("Kỷ niệm ngày yêu") }
  var dueDateText by remember { mutableStateOf("11 Tháng 9, 2026") }
  var estimatedBudget by remember { mutableStateOf("500.000đ") }
  var notes by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Filled.CardGiftcard,
          contentDescription = null,
          tint = Primary
        )
        Text(
          text = "Thêm Nhắc Nhở Quà Tặng",
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = OnSurface
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Món quà cần chuẩn bị *") },
          placeholder = { Text("Ví dụ: Bó hoa hồng vĩnh cửu & Thiệp") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_gift_reminder_title"),
          singleLine = true
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = recipient,
            onValueChange = { recipient = it },
            label = { Text("Tặng cho") },
            placeholder = { Text("TLinh") },
            modifier = Modifier
              .weight(1f)
              .testTag("input_gift_reminder_recipient"),
            singleLine = true
          )

          OutlinedTextField(
            value = estimatedBudget,
            onValueChange = { estimatedBudget = it },
            label = { Text("Dự chi") },
            placeholder = { Text("500.000đ") },
            modifier = Modifier
              .weight(1f)
              .testTag("input_gift_reminder_budget"),
            singleLine = true
          )
        }

        OutlinedTextField(
          value = occasion,
          onValueChange = { occasion = it },
          label = { Text("Dịp kỷ niệm / Sự kiện") },
          placeholder = { Text("Kỷ niệm 1.000 ngày") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_gift_reminder_occasion"),
          singleLine = true
        )

        InLoveDatePickerField(
          value = dueDateText,
          onValueChange = { dueDateText = it },
          label = "Hạn hoàn thành chuẩn bị (dd/MM/yyyy)",
          placeholder = "11/09/2026",
          dialogTitle = "Chọn ngày hẹn tặng quà",
          quickPresets = DatePickerPresets.upcomingAnniversaryPresets(),
          helperText = DatePickerUtils.getFriendlyDateDescription(dueDateText),
          modifier = Modifier.fillMaxWidth(),
          testTag = "input_gift_reminder_date"
        )

        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("Ghi chú món quà") },
          placeholder = { Text("Màu sắc, kích cỡ, địa chỉ cửa hàng...") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_gift_reminder_notes"),
          maxLines = 3
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          onConfirm(title, recipient, occasion, dueDateText, estimatedBudget, notes)
        },
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        modifier = Modifier.testTag("btn_save_gift_reminder")
      ) {
        Text("Lưu Nhắc Nhở (Room)", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Hủy", color = OnSurfaceVariant)
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(24.dp),
    modifier = Modifier.testTag("dialog_add_gift_reminder")
  )
}


