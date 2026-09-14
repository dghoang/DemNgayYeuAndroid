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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material.icons.filled.Search
import kotlinx.coroutines.launch
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.data.model.OnlineInviteEntity
import com.example.data.model.OnlineStatus
import com.example.data.model.OnlineUserEntity
import com.example.data.repository.OnlineCoupleRepository
import com.example.ui.viewmodel.InLoveViewModel
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
fun dialogTextFieldColors() = OutlinedTextFieldDefaults.colors(
  focusedTextColor = Color(0xFF1E1E24),
  unfocusedTextColor = Color(0xFF1E1E24),
  focusedContainerColor = Color.White,
  unfocusedContainerColor = Color.White,
  disabledContainerColor = Color(0xFFF5F5F7),
  focusedBorderColor = Primary,
  unfocusedBorderColor = Color(0xFFC7C7CC),
  focusedLabelColor = Primary,
  unfocusedLabelColor = Color(0xFF424242),
  focusedPlaceholderColor = Color(0xFF757575),
  unfocusedPlaceholderColor = Color(0xFF9E9E9E),
  cursorColor = Primary
)

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
  viewModel: InLoveViewModel,
  onDismiss: () -> Unit
) {
  val coroutineScope = rememberCoroutineScope()
  val currentUser by viewModel.currentOnlineUser.collectAsState()
  val partnerUser by viewModel.partnerOnlineUser.collectAsState()
  val relationshipStatus by viewModel.relationshipStatus.collectAsState()
  val incomingInvite by viewModel.incomingInvite.collectAsState()
  val outgoingInvite by viewModel.outgoingInvite.collectAsState()
  val loveDays by viewModel.loveDays.collectAsState()
  val anniversaryDate by viewModel.anniversaryDate.collectAsState()

  var searchQuery by remember { mutableStateOf("") }
  var isSearching by remember { mutableStateOf(false) }
  var searchError by remember { mutableStateOf<String?>(null) }
  var selectedPartner by remember { mutableStateOf<OnlineUserEntity?>(null) }

  var proposedStartDateText by remember {
    mutableStateOf(anniversaryDate.ifEmpty { "18/12/2022" })
  }
  var loveNote by remember { mutableStateOf("") }

  // Auto-calculated days strictly based on selected start date
  val calculatedDays by remember(proposedStartDateText) {
    derivedStateOf { ProfileUtils.calculateLoveDays(proposedStartDateText) }
  }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(26.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
        .testTag("edit_couple_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // --- 1. HEADER (To rõ, không chữ chú thích thừa) ---
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Favorite,
              contentDescription = null,
              tint = Color(0xFFE91E63),
              modifier = Modifier.size(26.dp)
            )
            Text(
              text = "Hồ Sơ Cặp Đôi (Set Love)",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF880E4F)
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Đóng",
              tint = Color.Gray
            )
          }
        }

        // --- 2. LỜI MỜI SET LOVE TỪ NGƯỜI KHÁC (NẾU CÓ) ---
        if (incomingInvite != null) {
          val invite = incomingInvite!!
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F5)),
            border = BorderStroke(1.5.dp, Color(0xFFFF4081)),
            modifier = Modifier.fillMaxWidth().testTag("dialog_incoming_invite_card")
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Favorite,
                  contentDescription = null,
                  tint = Color(0xFFE91E63),
                  modifier = Modifier.size(20.dp)
                )
                Text(
                  text = "Lời Mời Set Love Đang Chờ Duyệt!",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = Color(0xFFD81B60)
                )
              }

              // Thông tin người gửi (chỉ đọc, không thể chỉnh sửa)
              Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                  model = invite.senderAvatar.ifEmpty { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200" },
                  contentDescription = "Sender Avatar",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFFF80AB), CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = invite.effectiveSenderName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF880E4F)
                  )
                  Text(
                    text = "Mã: " + invite.senderCoupleCode,
                    fontSize = 13.sp,
                    color = Color.Gray
                  )
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFFEBEE),
                    modifier = Modifier.padding(top = 2.dp)
                  ) {
                    Text(
                      text = "Hồ sơ đối tác • Chỉ đọc ✓",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.SemiBold,
                      color = Color(0xFFC2185B),
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
              }

              // Thông tin kỷ niệm yêu thống nhất từ người tạo
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(
                  modifier = Modifier.padding(12.dp),
                  verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  val incomingDateText = invite.proposedStartDateText.ifEmpty {
                    ProfileUtils.formatDate(invite.proposedStartDate)
                  }
                  val incomingDays = ProfileUtils.calculateLoveDays(invite.proposedStartDate)

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(text = "Ngày bắt đầu yêu thống nhất:", fontSize = 14.sp, color = Color.DarkGray)
                    Text(text = incomingDateText, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC2185B))
                  }

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(text = "Số ngày yêu tính tự động:", fontSize = 14.sp, color = Color.DarkGray)
                    Text(text = incomingDays.toString() + " ngày bên nhau 💕", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFD81B60))
                  }

                  if (invite.loveNote.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = """ + invite.loveNote + """,
                      fontSize = 13.sp,
                      fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                      color = Color(0xFF4A148C)
                    )
                  }
                }
              }

              // Hai nút hành động: Đồng ý hoặc Từ chối
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                OutlinedButton(
                  onClick = { viewModel.rejectSetLoveInvite(invite.inviteId) },
                  shape = RoundedCornerShape(14.dp),
                  modifier = Modifier.weight(1f).height(48.dp).testTag("dialog_reject_invite_btn")
                ) {
                  Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Từ chối", fontSize = 14.sp)
                }

                Button(
                  onClick = {
                    viewModel.acceptSetLoveInvite(invite.inviteId)
                    onDismiss()
                  },
                  shape = RoundedCornerShape(14.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                  modifier = Modifier.weight(1.3f).height(48.dp).testTag("dialog_accept_invite_btn")
                ) {
                  Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Đồng ý Set Love ❤️", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
              }
            }
          }
        }

        // --- 3. ĐANG CHỜ ĐỐI PHƯƠNG PHẢN HỒI (NẾU CÓ OUTGOING INVITE) ---
        if (outgoingInvite != null && relationshipStatus != OnlineStatus.COUPLED) {
          val out = outgoingInvite!!
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
            border = BorderStroke(1.2.dp, Color(0xFFFFB74D)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(22.dp))
                Text("Đang Chờ Đối Phương Xác Nhận", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFE65100))
              }
              val outDays = ProfileUtils.calculateLoveDays(out.proposedStartDate)
              Text(
                text = "Đã gửi tới mã: " + out.targetCoupleCode + " • Ngày yêu đề xuất: " + out.proposedStartDateText + " (" + outDays + " ngày)",
                fontSize = 14.sp,
                color = Color.DarkGray
              )
              OutlinedButton(
                onClick = { viewModel.cancelSentInvite() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Text("Hủy lời mời đã gửi", fontSize = 14.sp)
              }
            }
          }
        }

        // --- 4. NẾU ĐÃ KẾT ĐÔI: HIỂN THỊ HỒ SƠ NGƯỜI ĐÓ (CHỈ ĐỌC) & THÔNG TIN KỶ NIỆM THỐNG NHẤT ---
        if (relationshipStatus == OnlineStatus.COUPLED) {
          val partner = partnerUser
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F5)),
            border = BorderStroke(1.2.dp, Color(0xFFFF80AB)),
            modifier = Modifier.fillMaxWidth().testTag("coupled_partner_card")
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "HỒ SƠ ĐỐI TÁC CỦA BẠN",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF880E4F)
                )
                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color(0xFFE8F5E9)
                ) {
                  Text(
                    text = "ĐÃ KẾT ĐÔI 1-1 ✓",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                  )
                }
              }

              // Thông tin người đó: CHỈ ĐỌC, KHÔNG THỂ ĐIỀU CHỈNH
              Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                  model = partner?.avatarUrl?.ifEmpty { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200" }
                    ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200",
                  contentDescription = "Partner Avatar",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFFF4081), CircleShape)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = partner?.effectiveDisplayName ?: "Người ấy",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF880E4F)
                  )
                  Text(
                    text = "Mã: " + (partner?.coupleCode ?: ""),
                    fontSize = 13.sp,
                    color = Color.Gray
                  )
                  Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if ((partner?.age ?: 0) > 0) {
                      Text(text = partner?.age.toString() + " tuổi", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFC2185B))
                    }
                    if (!partner?.zodiac.isNullOrBlank()) {
                      Text(text = "• Cung " + partner?.zodiac, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF880E4F))
                    }
                  }
                }
              }

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
              ) {
                Text(
                  text = "🔒 Thông tin đối tác được đồng bộ từ tài khoản đối phương và không thể điều chỉnh tại đây.",
                  fontSize = 12.sp,
                  color = Color(0xFF757575),
                  modifier = Modifier.padding(10.dp)
                )
              }
            }
          }

          // Kỷ niệm ngày yêu thống nhất
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
            border = BorderStroke(1.dp, Color(0xFFE1BEE7)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Text(
                text = "Kỷ Niệm Tình Yêu Thống Nhất",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF7B1FA2)
              )
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(text = "Ngày bắt đầu yêu:", fontSize = 14.sp, color = Color.DarkGray)
                Text(text = anniversaryDate, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7B1FA2))
              }
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFEBEE),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFE91E63), modifier = Modifier.size(20.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Đã yêu nhau " + loveDays + " ngày bên nhau 💕",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD81B60)
                  )
                }
              }
            }
          }
        }

        // --- 5. NẾU CHƯA KẾT ĐÔI: TÌM KIẾM ĐỐI TÁC & CHỈ HIỆN THÔNG TIN SET LOVE KHI ĐÃ CHỌN NGƯỜI ---
        if (relationshipStatus != OnlineStatus.COUPLED && incomingInvite == null) {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.2.dp, Color(0xFFFFCDD2)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
              Text(
                text = "Tìm Kiếm Người Ấy Để Set Love",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF880E4F)
              )

              // Ô tìm kiếm & Nút tìm kiếm
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                OutlinedTextField(
                  value = searchQuery,
                  onValueChange = {
                    searchQuery = it
                    searchError = null
                  },
                  placeholder = { Text("Mã hoặc link người ấy (vd: LOVE-9966)...", fontSize = 14.sp) },
                  singleLine = true,
                  shape = RoundedCornerShape(14.dp),
                  colors = dialogTextFieldColors(),
                  modifier = Modifier.weight(1f).testTag("dialog_search_input")
                )

                Button(
                  onClick = {
                    val q = searchQuery.trim()
                    if (q.isNotBlank()) {
                      coroutineScope.launch {
                        isSearching = true
                        searchError = null
                        val res = viewModel.searchPartnerForSetLove(q)
                        isSearching = false
                        if (res == null) {
                          searchError = "Không tìm thấy người dùng với mã này"
                        } else if (res.uid == currentUser.uid) {
                          searchError = "Không thể gửi lời mời cho chính mình"
                        } else {
                          selectedPartner = res
                        }
                      }
                    }
                  },
                  shape = RoundedCornerShape(14.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                  contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                  modifier = Modifier.testTag("dialog_btn_search")
                ) {
                  if (isSearching) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                  } else {
                    Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
                  }
                }
              }

              if (searchError != null) {
                Text(text = searchError!!, color = Color(0xFFD32F2F), fontSize = 13.sp, fontWeight = FontWeight.Medium)
              }

              // Gợi ý nhanh tiện lợi
              val quickTargetCode = if (currentUser.uid == OnlineCoupleRepository.USER_A_ID) {
                OnlineCoupleRepository.USER_B_CODE
              } else {
                OnlineCoupleRepository.USER_A_CODE
              }
              Text(
                text = "💡 Thử nghiệm nhanh: Bấm để dán " + quickTargetCode,
                fontSize = 12.sp,
                color = Color(0xFF00897B),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                  searchQuery = quickTargetCode
                  coroutineScope.launch {
                    isSearching = true
                    searchError = null
                    val res = viewModel.searchPartnerForSetLove(quickTargetCode)
                    isSearching = false
                    if (res != null) selectedPartner = res
                  }
                }
              )
            }
          }

          // CHỈ HIỂN THỊ KHI ĐÃ TÌM THẤY & CHỌN ĐỐI PHƯƠNG
          if (selectedPartner != null) {
            val partner = selectedPartner!!

            // Thẻ hồ sơ người đó: THÔNG TIN KÈM THEO KHÔNG THỂ ĐIỀU CHỈNH (READ-ONLY)
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F5)),
              border = BorderStroke(1.5.dp, Color(0xFFFF4081)),
              modifier = Modifier.fillMaxWidth().testTag("dialog_selected_partner_card")
            ) {
              Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE91E63)
                  ) {
                    Text(
                      text = "ĐÃ TÌM THẤY ĐỐI TÁC • CHỈ ĐỌC",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = Color.White,
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                  }

                  IconButton(
                    onClick = { selectedPartner = null },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(Icons.Default.Close, contentDescription = "Bỏ chọn", tint = Color.Gray)
                  }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                  AsyncImage(
                    model = partner.avatarUrl.ifEmpty { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200" },
                    contentDescription = "Partner Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                      .size(68.dp)
                      .clip(CircleShape)
                      .border(2.dp, Color(0xFFFF4081), CircleShape)
                  )

                  Spacer(modifier = Modifier.width(14.dp))

                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = partner.effectiveDisplayName,
                      fontWeight = FontWeight.Bold,
                      fontSize = 18.sp,
                      color = Color(0xFF880E4F)
                    )
                    Text(
                      text = "Mã: " + partner.coupleCode,
                      fontSize = 13.sp,
                      color = Color.Gray
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                      if (partner.age > 0) {
                        Text(text = partner.age.toString() + " tuổi", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFC2185B))
                      }
                      if (partner.zodiac.isNotBlank()) {
                        Text(text = "• Cung " + partner.zodiac, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF880E4F))
                      }
                    }
                  }
                }

                if (partner.bio.isNotBlank()) {
                  Text(
                    text = """ + partner.bio + """,
                    fontSize = 13.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Color.DarkGray
                  )
                }

                Surface(
                  shape = RoundedCornerShape(8.dp),
                  color = Color.White.copy(alpha = 0.8f),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "🔒 Hồ sơ người ấy là chỉ đọc, không thể chỉnh sửa tại đây.",
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(8.dp)
                  )
                }

                Divider(color = Color(0xFFFFCDD2))

                // THIẾT LẬP KỶ NIỆM YÊU (THỐNG NHẤT TỪ NGƯỜI TẠO)
                Text(
                  text = "Thiết Lập Ngày Bắt Đầu Yêu:",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  color = Color(0xFF880E4F)
                )

                // Date Picker chọn ngày bắt đầu yêu
                InLoveDatePickerField(
                  value = proposedStartDateText,
                  onValueChange = { proposedStartDateText = it },
                  label = "Ngày bắt đầu yêu (dd/MM/yyyy) *",
                  placeholder = "18/12/2022",
                  dialogTitle = "Chọn ngày bắt đầu yêu",
                  quickPresets = DatePickerPresets.relationshipStartDatePresets(),
                  modifier = Modifier.fillMaxWidth(),
                  testTag = "dialog_input_proposed_start_date"
                )

                // TỰ ĐỘNG TÍNH SỐ NGÀY YÊU (KHÔNG NHẬP TAY)
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = Color(0xFFFFEBEE),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = Icons.Default.Favorite,
                      contentDescription = null,
                      tint = Color(0xFFE91E63),
                      modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                      text = "✨ Tính đến hôm nay: " + calculatedDays + " ngày yêu nhau 💕",
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFD81B60)
                    )
                  }
                }

                // Lời nhắn gửi đối phương
                OutlinedTextField(
                  value = loveNote,
                  onValueChange = { loveNote = it },
                  label = { Text("Lời nhắn gửi người ấy (tùy chọn)") },
                  maxLines = 2,
                  shape = RoundedCornerShape(14.dp),
                  colors = dialogTextFieldColors(),
                  modifier = Modifier.fillMaxWidth()
                )

                // Nút Gửi Lời Mời Set Love
                Button(
                  onClick = {
                    val parsedMillis = ProfileUtils.parseDateToMillis(proposedStartDateText)
                    viewModel.sendSetLoveInvite(
                      targetCodeOrLink = partner.coupleCode,
                      proposedStartDateMillis = parsedMillis,
                      loveNote = loveNote
                    )
                    onDismiss()
                  },
                  shape = RoundedCornerShape(16.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("dialog_btn_send_set_love")
                ) {
                  Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Gửi Lời Mời Set Love Cho " + partner.effectiveDisplayName + " ❤️",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                  )
                }
              }
            }
          }
        }

        // --- 6. FOOTER: HỒ SƠ CỦA BẠN ĐIỀU CHỈNH TRONG CÀI ĐẶT ---
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = Color(0xFFF5F5F7),
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              viewModel.openEditProfileDialog()
              onDismiss()
            }
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = null,
              tint = Color(0xFF5C6BC0),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Hồ sơ cá nhân của bạn",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = OnSurface
              )
              Text(
                text = "Chỉnh sửa đầy đủ tại mục Cài đặt (Tên, ngày sinh, ảnh)",
                fontSize = 12.sp,
                color = OnSurfaceVariant
              )
            }
            Icon(
              imageVector = Icons.Filled.ChevronRight,
              contentDescription = null,
              tint = Color.Gray
            )
          }
        }

        // Nút Đóng Dialog
        OutlinedButton(
          onClick = onDismiss,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
        ) {
          Text("Đóng", fontSize = 14.sp, color = OnSurfaceVariant)
        }
      }
    }
  }
}

// Backward-compatibility overload for EditCoupleDialog
@Composable
fun EditCoupleDialog(
  currentBoyName: String = "",
  currentBoyBirth: String = "",
  currentBoyAvatar: String = "",
  currentBoyAge: Int = 0,
  currentBoyZodiac: String = "",
  currentGirlName: String = "",
  currentGirlBirth: String = "",
  currentGirlAvatar: String = "",
  currentGirlAge: Int = 0,
  currentGirlZodiac: String = "",
  currentTitle: String = "",
  currentDays: Int = 0,
  currentAnniversary: String = "18/12/2022",
  onSearchPartner: (suspend (String) -> OnlineUserEntity?)? = null,
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
  // Empty fallback - Main entry calls the ViewModel version
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


