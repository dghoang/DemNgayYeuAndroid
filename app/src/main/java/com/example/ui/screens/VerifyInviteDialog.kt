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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.OnlineInviteEntity
import com.example.ui.util.ProfileUtils

@Composable
fun VerifyInviteDialog(
  invite: OnlineInviteEntity,
  onDismiss: () -> Unit,
  onAccept: (confirmedDateMillis: Long) -> Unit,
  onReject: () -> Unit
) {
  var isCustomizingDate by remember { mutableStateOf(false) }
  var confirmedDateText by remember {
    mutableStateOf(
      if (invite.proposedStartDateText.isNotBlank()) invite.proposedStartDateText
      else ProfileUtils.formatDate(invite.proposedStartDate)
    )
  }

  val calculatedDays by remember(confirmedDateText) {
    derivedStateOf {
      val parsedMillis = ProfileUtils.parseDateToMillis(confirmedDateText)
      ProfileUtils.calculateLoveDays(parsedMillis)
    }
  }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(26.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)
        .testTag("verify_invite_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.VerifiedUser,
              contentDescription = null,
              tint = Color(0xFFE91E63),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Xác Nhận Đúng Người",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF880E4F)
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Đóng",
              tint = Color.Gray
            )
          }
        }

        Text(
          text = "Vui lòng kiểm tra kỹ thông tin người gửi và ngày kỷ niệm trước khi đồng ý kết đôi 1-1.",
          fontSize = 12.sp,
          color = Color.Gray,
          modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 14.dp)
        )

        // Partner Identity Card
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F5)),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB6C1)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(modifier = Modifier.size(64.dp)) {
                AsyncImage(
                  model = invite.senderAvatar.ifEmpty { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200" },
                  contentDescription = "Sender Avatar",
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFE91E63), CircleShape)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = invite.effectiveSenderName,
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF880E4F)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "Mã kết đôi: ${invite.senderCoupleCode}",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = Color(0xFFC2185B)
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  if (invite.senderAge > 0) {
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = Color.White
                    ) {
                      Text(
                        text = "${invite.senderAge} tuổi",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC2185B),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }

                  if (invite.senderZodiac.isNotBlank()) {
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = Color.White
                    ) {
                      Text(
                        text = "Cung ${invite.senderZodiac}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF880E4F),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }
              }
            }

            if (invite.senderBio.isNotBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = null,
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = invite.senderBio,
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = Color.DarkGray
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Love Date & Synchronization Status
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CalendarMonth,
                  contentDescription = null,
                  tint = Color(0xFFE91E63),
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Tình trạng ngày yêu đề xuất:",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF880E4F)
                )
              }

              Text(
                text = if (isCustomizingDate) "Đóng chỉnh" else "Chỉnh ngày",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63),
                modifier = Modifier.clickable { isCustomizingDate = !isCustomizingDate }
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
              shape = RoundedCornerShape(14.dp),
              color = Color(0xFFFFF3E0),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = confirmedDateText,
                  fontSize = 18.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color(0xFFE65100)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "❤️ Đã bên nhau $calculatedDays ngày yêu",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFD84315)
                )
                Text(
                  text = "Khi đồng ý, cả hai máy sẽ cùng đồng bộ mốc ngày yêu này!",
                  fontSize = 11.sp,
                  color = Color.DarkGray,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.padding(top = 4.dp)
                )
              }
            }

            if (isCustomizingDate) {
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = confirmedDateText,
                onValueChange = { confirmedDateText = it },
                label = { Text("Ngày bắt đầu yêu (dd/MM/yyyy)") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = Color(0xFFE91E63),
                  unfocusedBorderColor = Color(0xFFFFCDD2)
                ),
                modifier = Modifier.fillMaxWidth()
              )
            }

            if (invite.loveNote.isNotBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              Text(
                text = "💌 Lời nhắn từ đối phương:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
              )
              Text(
                text = "\"${invite.loveNote}\"",
                fontSize = 13.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = Color(0xFF880E4F),
                modifier = Modifier.padding(top = 2.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          OutlinedButton(
            onClick = onReject,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
            modifier = Modifier.weight(1f).testTag("btn_verify_reject")
          ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Từ chối")
          }

          Button(
            onClick = {
              val confirmedMillis = ProfileUtils.parseDateToMillis(confirmedDateText)
              onAccept(confirmedMillis)
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
            modifier = Modifier.weight(1.3f).testTag("btn_verify_accept")
          ) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Đồng ý Set Love ❤️", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
