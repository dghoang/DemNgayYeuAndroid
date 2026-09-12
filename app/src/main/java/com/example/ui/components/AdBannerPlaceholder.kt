package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Primary

/**
 * Khoảng trống phía trên ứng dụng dành riêng để người dùng tích hợp Plugin Quảng Cáo (AdMob / Banner Ad).
 * Thay thế phần chữ yêu thích & icon trái tim để làm vị trí cắm quảng cáo tiện lợi.
 */
@Composable
fun AdBannerPlaceholder(
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 12.dp, vertical = 4.dp)
      .height(52.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(Color.White)
      .border(
        width = 1.2.dp,
        color = Primary.copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp)
      )
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
      .testTag("ad_banner_placeholder_slot"),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = Primary.copy(alpha = 0.12f),
        modifier = Modifier.padding(end = 8.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Filled.Campaign,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "ADS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
          )
        }
      }

      Text(
        text = "Vị trí để trống tích hợp Plugin Quảng Cáo (Banner)",
        fontSize = 11.5.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF880E4F),
        letterSpacing = 0.2.sp
      )
    }
  }
}

