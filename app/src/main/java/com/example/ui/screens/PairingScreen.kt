package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.OnlineStatus
import com.example.data.repository.OnlineCoupleRepository
import com.example.ui.util.ProfileUtils
import com.example.ui.viewmodel.InLoveViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PairingScreen(
  viewModel: InLoveViewModel,
  onNavigateBack: () -> Unit = {}
) {
  val context = LocalContext.current
  val currentUser by viewModel.currentOnlineUser.collectAsState()
  val partnerUser by viewModel.partnerOnlineUser.collectAsState()
  val relationshipStatus by viewModel.relationshipStatus.collectAsState()
  val incomingInvite by viewModel.incomingInvite.collectAsState()
  val outgoingInvite by viewModel.outgoingInvite.collectAsState()
  val mutualInterests by viewModel.mutualInterests.collectAsState()

  // Search & Inspection State
  val searchQuery by viewModel.searchQuery.collectAsState()
  val searchedUser by viewModel.searchedUser.collectAsState()
  val isSearching by viewModel.isSearching.collectAsState()
  val selectedInviteForVerification by viewModel.selectedInviteForVerification.collectAsState()
  val showEditProfileDialog by viewModel.showEditProfileDialog.collectAsState()

  // Send Invite form fields
  var proposedStartDateText by remember { mutableStateOf("18/12/2022") }
  var loveNoteInput by remember { mutableStateOf("Cùng anh/em xây dựng hạnh phúc Set Love nhé! 💕") }

  val calculatedDaysFromProposed by remember(proposedStartDateText) {
    derivedStateOf {
      val parsed = ProfileUtils.parseDateToMillis(proposedStartDateText)
      ProfileUtils.calculateLoveDays(parsed)
    }
  }

  fun copyToClipboard(text: String, label: String = "Couple Code") {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    viewModel.showToast("Đã sao chép: $text")
  }

  fun shareCoupleLink(code: String) {
    val shareLink = ProfileUtils.createShareLink(code)
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(
        Intent.EXTRA_TEXT,
        "Cùng kết nối Set Love 1-1 với mình trên InLove nhé! Bấm vào link hoặc nhập mã:\n$shareLink\nMã ghép đôi: $code ❤️"
      )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ mã/link ghép đôi qua"))
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Ghép Đôi 1-1 (Set Love)",
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        actions = {
          // Switch Demo User Quick Button for Testing
          OutlinedButton(
            onClick = { viewModel.switchDemoUser() },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier.padding(end = 8.dp).testTag("switch_demo_user_button")
          ) {
            Icon(
              imageVector = Icons.Default.SwapHoriz,
              contentDescription = "Chuyển tài khoản",
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Đổi bên", fontSize = 12.sp)
          }
        }
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(vertical = 16.dp)
    ) {
      // 0. PROFILE SETUP WARNING BANNER (Shows "Vô danh" status)
      if (!currentUser.isProfileSetup) {
        item {
          Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFFB74D)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("profile_setup_prompt_banner")
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color(0xFFE65100),
                modifier = Modifier.size(28.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Bạn đang ở trạng thái \"Vô danh\"",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFFBF360C)
                )
                Text(
                  text = "Hãy thiết lập tên và ngày sinh. Tuổi và cung hoàng đạo sẽ được tự động tính toán chính xác!",
                  fontSize = 11.sp,
                  color = Color.DarkGray
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Button(
                onClick = { viewModel.openEditProfileDialog() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                modifier = Modifier.testTag("btn_setup_profile_now")
              ) {
                Text("Thiết lập", fontSize = 11.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }

      // 1. INCOMING INVITATION CARD (WITH IDENTITY & DATE VERIFICATION)
      if (incomingInvite != null) {
        item {
          Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F5)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFF4081)),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("incoming_invite_card")
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Favorite,
                  contentDescription = null,
                  tint = Color(0xFFE91E63),
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Lời Mời Ghép Đôi Đang Chờ Duyệt!",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = Color(0xFFD81B60)
                )
              }

              Spacer(modifier = Modifier.height(12.dp))

              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
              ) {
                AsyncImage(
                  model = incomingInvite!!.senderAvatar.ifEmpty {
                    "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"
                  },
                  contentDescription = "Sender Avatar",
                  modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFFF80AB), CircleShape),
                  contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = incomingInvite!!.effectiveSenderName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF880E4F)
                  )
                  Text(
                    text = "Mã: ${incomingInvite!!.senderCoupleCode}",
                    fontSize = 12.sp,
                    color = Color.Gray
                  )
                  Text(
                    text = "Đề xuất ngày yêu: ${incomingInvite!!.proposedStartDateText.ifEmpty { ProfileUtils.formatDate(incomingInvite!!.proposedStartDate) }}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFC2185B)
                  )
                }
              }

              Spacer(modifier = Modifier.height(14.dp))

              // Check & Verify Button (User request: "xem tình trạng ngày yêu xác nhận đúng người hay không trước khi từ chối hay chấp nhận ghép inlove")
              Button(
                onClick = { viewModel.inspectInvite(incomingInvite!!) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF880E4F)),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("inspect_verify_invite_button")
              ) {
                Icon(
                  imageVector = Icons.Default.VerifiedUser,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kiểm tra danh tính & Xem tình trạng ngày yêu", fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }

              Spacer(modifier = Modifier.height(8.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
              ) {
                OutlinedButton(
                  onClick = { viewModel.rejectSetLoveInvite(incomingInvite!!.inviteId) },
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.weight(1f).testTag("reject_invite_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Từ chối")
                }

                Button(
                  onClick = { viewModel.acceptSetLoveInvite(incomingInvite!!.inviteId) },
                  shape = RoundedCornerShape(12.dp),
                  colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63)
                  ),
                  modifier = Modifier.weight(1f).testTag("accept_invite_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Đồng ý ❤️", fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }

      // 2. MY PROFILE & COUPLE CODE & SHARE LINK CARD
      item {
        Card(
          shape = RoundedCornerShape(24.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.fillMaxWidth()
            ) {
              AsyncImage(
                model = currentUser.avatarUrl.ifEmpty {
                  "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200"
                },
                contentDescription = "My Avatar",
                modifier = Modifier
                  .size(60.dp)
                  .clip(CircleShape)
                  .border(2.dp, Color(0xFFFF4081), CircleShape),
                contentScale = ContentScale.Crop
              )

              Spacer(modifier = Modifier.width(14.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = currentUser.effectiveDisplayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (relationshipStatus) {
                      OnlineStatus.COUPLED -> Color(0xFFE8F5E9)
                      OnlineStatus.PENDING_INVITE -> Color(0xFFFFF3E0)
                      else -> Color(0xFFF5F5F5)
                    }
                  ) {
                    Text(
                      text = when (relationshipStatus) {
                        OnlineStatus.COUPLED -> "ĐÃ CÓ ĐÔI ❤️"
                        OnlineStatus.PENDING_INVITE -> "CHỜ GHÉP ĐÔI ⏳"
                        else -> "ĐỘC THÂN 🕊️"
                      },
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold,
                      color = when (relationshipStatus) {
                        OnlineStatus.COUPLED -> Color(0xFF2E7D32)
                        OnlineStatus.PENDING_INVITE -> Color(0xFFE65100)
                        else -> Color.Gray
                      },
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }

                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                  modifier = Modifier.padding(top = 2.dp)
                ) {
                  if (currentUser.age > 0) {
                    Text(text = "${currentUser.age} tuổi", fontSize = 12.sp, color = Color.Gray)
                  }
                  if (currentUser.zodiac.isNotBlank()) {
                    Text(text = "• Cung ${currentUser.zodiac}", fontSize = 12.sp, color = Color(0xFF880E4F), fontWeight = FontWeight.SemiBold)
                  }
                }
              }

              // Edit My Profile Button
              IconButton(
                onClick = { viewModel.openEditProfileDialog() },
                modifier = Modifier.testTag("btn_open_edit_profile")
              ) {
                Icon(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Chỉnh sửa hồ sơ",
                  tint = Color(0xFFE91E63)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(12.dp))

            // Couple Code Box
            Text(
              text = "MÃ GHÉP ĐÔI CỦA BẠN",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = Color.Gray
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFFFB6C1), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = currentUser.coupleCode,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = Color(0xFFD81B60),
                modifier = Modifier.testTag("my_couple_code_text")
              )

              Row {
                IconButton(
                  onClick = { copyToClipboard(currentUser.coupleCode, "Couple Code") },
                  modifier = Modifier.size(36.dp).testTag("copy_couple_code_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy code",
                    tint = Color(0xFFD81B60),
                    modifier = Modifier.size(20.dp)
                  )
                }

                IconButton(
                  onClick = {
                    val link = ProfileUtils.createShareLink(currentUser.coupleCode)
                    copyToClipboard(link, "Couple Link")
                  },
                  modifier = Modifier.size(36.dp).testTag("copy_couple_link_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Link,
                    contentDescription = "Copy link",
                    tint = Color(0xFF0288D1),
                    modifier = Modifier.size(20.dp)
                  )
                }

                IconButton(
                  onClick = { shareCoupleLink(currentUser.coupleCode) },
                  modifier = Modifier.size(36.dp).testTag("share_couple_code_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share code/link",
                    tint = Color(0xFFD81B60),
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Gửi mã hoặc link này cho người yêu của bạn để hai bạn đồng bộ và kết nối 1-1.",
              fontSize = 11.sp,
              color = Color.Gray
            )
          }
        }
      }

      // 3. SEARCH BY CODE OR LINK & SEND SET LOVE SECTION
      when (relationshipStatus) {
        OnlineStatus.COUPLED -> {
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  imageVector = Icons.Default.Favorite,
                  contentDescription = null,
                  tint = Color(0xFFE91E63),
                  modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                  text = "Đang trong mối quan hệ 1-1 độc quyền",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = Color(0xFF880E4F)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = "Đối phương: ${partnerUser?.displayName ?: "Người thương"} (${partnerUser?.coupleCode ?: ""})",
                  fontSize = 13.sp,
                  color = Color(0xFF4A148C)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                  onClick = {
                    viewModel.setTab(4) // Go to Settings tab
                    onNavigateBack()
                  },
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text("Quản lý trạng thái Set Love trong Cài đặt")
                }
              }
            }
          }
        }

        OnlineStatus.PENDING_INVITE -> {
          item {
            Card(
              shape = RoundedCornerShape(20.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  imageVector = Icons.Default.HourglassTop,
                  contentDescription = null,
                  tint = Color(0xFFFFA000),
                  modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = "Đang chờ đối phương xác nhận lời mời Set Love...",
                  fontWeight = FontWeight.Bold,
                  fontSize = 15.sp,
                  textAlign = TextAlign.Center,
                  color = Color(0xFFE65100)
                )

                if (outgoingInvite != null) {
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = "Gửi tới: ${outgoingInvite!!.targetCoupleCode} • Ngày yêu đề xuất: ${outgoingInvite!!.proposedStartDateText}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray
                  )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                  text = "Khi người ấy mở ứng dụng và bấm Đồng ý, hai bạn sẽ ngay lập tức được kết đôi 1-1!",
                  fontSize = 12.sp,
                  color = Color.DarkGray,
                  textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedButton(
                  onClick = { viewModel.cancelSentInvite() },
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.testTag("cancel_sent_invite_button")
                ) {
                  Text("Hủy lời mời đã gửi")
                }
              }
            }
          }
        }

        else -> {
          // SINGLE: Search by Code/Link & Send Invite
          item {
            Card(
              shape = RoundedCornerShape(22.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2)),
              modifier = Modifier.fillMaxWidth().testTag("search_and_pair_card")
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "Tìm kiếm & Ghép đôi theo mã hoặc link",
                  fontWeight = FontWeight.Bold,
                  fontSize = 16.sp,
                  color = Color(0xFFB71C1C)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                  text = "Dán mã ghép đôi (ví dụ: LOVE-9966) hoặc link đầy đủ (inlove://pair/...) để kiểm tra danh tính đối phương trước khi gửi lời mời.",
                  fontSize = 12.sp,
                  color = Color.Gray
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Search Input with Action Button
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = { Text("Mã hoặc link đối phương") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                      focusedBorderColor = Color(0xFFE91E63),
                      unfocusedBorderColor = Color(0xFFFFCDD2)
                    ),
                    modifier = Modifier
                      .weight(1f)
                      .testTag("input_search_code_or_link")
                  )

                  Button(
                    onClick = { viewModel.performSearch() },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
                    modifier = Modifier.testTag("btn_perform_search")
                  ) {
                    if (isSearching) {
                      CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    } else {
                      Icon(imageVector = Icons.Default.Search, contentDescription = "Tìm")
                    }
                  }
                }

                // Quick Helper suggestions
                Spacer(modifier = Modifier.height(8.dp))
                val targetSuggestion = if (currentUser.uid == OnlineCoupleRepository.USER_A_ID) {
                  OnlineCoupleRepository.USER_B_CODE
                } else {
                  OnlineCoupleRepository.USER_A_CODE
                }

                Text(
                  text = "💡 Gợi ý nhanh cho bạn thử nghiệm: Bấm để dán $targetSuggestion",
                  fontSize = 11.sp,
                  color = Color(0xFF00897B),
                  modifier = Modifier.clickable {
                    viewModel.updateSearchQuery(targetSuggestion)
                    viewModel.performSearch()
                  }
                )

                // FOUND PARTNER PREVIEW & PROPOSE LOVE DATE CARD
                if (searchedUser != null) {
                  val target = searchedUser!!
                  Spacer(modifier = Modifier.height(16.dp))

                  Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0F5)),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFFF4081)),
                    modifier = Modifier.fillMaxWidth().testTag("found_user_preview_card")
                  ) {
                    Column(modifier = Modifier.padding(14.dp)) {
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
                            text = "ĐÃ TÌM THẤY ĐỐI PHƯƠNG",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                          )
                        }

                        IconButton(
                          onClick = { viewModel.clearSearch() },
                          modifier = Modifier.size(24.dp)
                        ) {
                          Icon(imageVector = Icons.Default.Close, contentDescription = "Xóa", tint = Color.Gray)
                        }
                      }

                      Spacer(modifier = Modifier.height(10.dp))

                      Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                          model = target.avatarUrl.ifEmpty { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200" },
                          contentDescription = "Found user avatar",
                          contentScale = ContentScale.Crop,
                          modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFFFF4081), CircleShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                          Text(
                            text = target.effectiveDisplayName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF880E4F)
                          )
                          Text(
                            text = "Mã: ${target.coupleCode}",
                            fontSize = 12.sp,
                            color = Color.Gray
                          )
                          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (target.age > 0) {
                              Text(text = "${target.age} tuổi", fontSize = 11.sp, color = Color(0xFFC2185B), fontWeight = FontWeight.Bold)
                            }
                            if (target.zodiac.isNotBlank()) {
                              Text(text = "• Cung ${target.zodiac}", fontSize = 11.sp, color = Color(0xFF880E4F), fontWeight = FontWeight.Bold)
                            }
                          }
                        }
                      }

                      if (target.bio.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                          text = "\"${target.bio}\"",
                          fontSize = 12.sp,
                          fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                          color = Color.DarkGray
                        )
                      }

                      Spacer(modifier = Modifier.height(12.dp))
                      Divider(color = Color(0xFFFFCDD2))
                      Spacer(modifier = Modifier.height(10.dp))

                      // Proposed Love Date Field
                      Text(
                        text = "Đề xuất ngày bắt đầu yêu:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF880E4F)
                      )

                      Spacer(modifier = Modifier.height(4.dp))

                      OutlinedTextField(
                        value = proposedStartDateText,
                        onValueChange = { proposedStartDateText = it },
                        label = { Text("Ngày bắt đầu yêu (dd/MM/yyyy)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                          Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFFE91E63))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                          focusedBorderColor = Color(0xFFE91E63),
                          unfocusedBorderColor = Color(0xFFFFCDD2)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_proposed_start_date")
                      )

                      Spacer(modifier = Modifier.height(6.dp))
                      Text(
                        text = "✨ Hai bạn sẽ bắt đầu với khoảng $calculatedDaysFromProposed ngày yêu bên nhau!",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD81B60)
                      )

                      Spacer(modifier = Modifier.height(8.dp))

                      // Love Note Field
                      OutlinedTextField(
                        value = loveNoteInput,
                        onValueChange = { loveNoteInput = it },
                        label = { Text("Lời nhắn gửi người ấy") },
                        maxLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                          focusedBorderColor = Color(0xFFE91E63),
                          unfocusedBorderColor = Color(0xFFFFCDD2)
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_love_note")
                      )

                      Spacer(modifier = Modifier.height(14.dp))

                      // Send Set Love Invite Button
                      Button(
                        onClick = {
                          val parsedDate = ProfileUtils.parseDateToMillis(proposedStartDateText)
                          viewModel.sendSetLoveInvite(
                            targetCodeOrLink = target.coupleCode,
                            proposedStartDateMillis = parsedDate,
                            loveNote = loveNoteInput
                          )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                        modifier = Modifier
                          .fillMaxWidth()
                          .height(48.dp)
                          .testTag("send_set_love_to_target_button")
                      ) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                          text = "Gửi Lời Mời Set Love Cho ${target.effectiveDisplayName} ❤️",
                          fontWeight = FontWeight.Bold,
                          fontSize = 14.sp
                        )
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      // 4. PERSONAL INTERESTS SECTION
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Sở Thích Cá Nhân",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Spacer(modifier = Modifier.weight(1f))
              Text(
                text = "Gợi ý quà tặng",
                fontSize = 11.sp,
                color = Color(0xFFE91E63),
                fontWeight = FontWeight.SemiBold
              )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Hệ thống sẽ lấy giao điểm sở thích của cả hai để gợi ý quà và lịch hẹn phù hợp nhất khi kết đôi.",
              fontSize = 11.sp,
              color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              val myInterests = currentUser.interests
              OnlineCoupleRepository.AVAILABLE_INTERESTS.forEach { (key, label) ->
                val isSelected = myInterests.contains(key)
                FilterChip(
                  selected = isSelected,
                  onClick = { viewModel.toggleInterest(key) },
                  label = { Text(text = label, fontSize = 12.sp) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFFEBEE),
                    selectedLabelColor = Color(0xFFC2185B)
                  )
                )
              }
            }

            if (mutualInterests.isNotEmpty()) {
              Spacer(modifier = Modifier.height(12.dp))
              Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF3E5F5),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(
                    text = "✨ Sở thích chung của hai bạn: ${mutualInterests.joinToString(", ")}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color(0xFF6A1B9A)
                  )
                }
              }
            }
          }
        }
      }
    }
  }

  // DIALOGS:
  // 1. Edit My Profile Dialog
  if (showEditProfileDialog) {
    EditMyProfileDialog(
      currentUser = currentUser,
      onDismiss = { viewModel.closeEditProfileDialog() },
      onSave = { name, birthDate, avatarUrl, gender, bio ->
        viewModel.updateMyProfile(name, birthDate, avatarUrl, gender, bio)
      }
    )
  }

  // 2. Identity & Date Verification Dialog for Incoming Invite
  if (selectedInviteForVerification != null) {
    VerifyInviteDialog(
      invite = selectedInviteForVerification!!,
      onDismiss = { viewModel.dismissInspectInvite() },
      onAccept = { confirmedDateMillis ->
        viewModel.acceptSetLoveInvite(
          inviteId = selectedInviteForVerification!!.inviteId,
          confirmedStartDateMillis = confirmedDateMillis
        )
      },
      onReject = {
        viewModel.rejectSetLoveInvite(selectedInviteForVerification!!.inviteId)
      }
    )
  }
}
