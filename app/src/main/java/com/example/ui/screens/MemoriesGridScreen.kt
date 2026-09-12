package com.example.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.SharedMemoryEntity
import com.example.ui.util.AppLanguage
import com.example.ui.util.LocalizedStrings
import com.example.ui.viewmodel.InLoveViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemoriesGridScreen(
  viewModel: InLoveViewModel,
  modifier: Modifier = Modifier
) {
  val currentLanguage by viewModel.appLanguage.collectAsState()
  val strings = LocalizedStrings.get(currentLanguage)
  val memories by viewModel.sharedMemories.collectAsState()
  val selectedDetail by viewModel.selectedMemoryDetail.collectAsState()

  var showAddDialog by remember { mutableStateOf(false) }
  var selectedFilter by remember { mutableStateOf("all") } // "all" or "fav"

  val filteredMemories = remember(memories, selectedFilter) {
    when (selectedFilter) {
      "fav" -> memories.filter { it.isFavorite }
      else -> memories
    }
  }

  Box(modifier = modifier.fillMaxSize()) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 96.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      // Header Section
      item(span = { GridItemSpan(2) }) {
        MemoriesHeader(
          strings = strings,
          totalCount = memories.size,
          selectedFilter = selectedFilter,
          onFilterChanged = { selectedFilter = it },
          onAddClick = { showAddDialog = true }
        )
      }

      // Empty State
      if (filteredMemories.isEmpty()) {
        item(span = { GridItemSpan(2) }) {
          EmptyMemoriesCard(
            strings = strings,
            isFavoriteFilter = selectedFilter == "fav",
            onAddClick = { showAddDialog = true }
          )
        }
      } else {
        // Grid items
        items(
          items = filteredMemories,
          key = { it.id }
        ) { memory ->
          MemoryCardItem(
            memory = memory,
            onClick = { viewModel.openMemoryDetail(memory) },
            onToggleFavorite = { viewModel.toggleMemoryFavorite(memory) }
          )
        }
      }
    }

    // Floating Action Button
    ExtendedFloatingActionButton(
      onClick = { showAddDialog = true },
      containerColor = Color(0xFFFF2D75),
      contentColor = Color.White,
      shape = RoundedCornerShape(28.dp),
      icon = {
        Icon(
          imageVector = Icons.Default.AddAPhoto,
          contentDescription = strings.btnAddMemory
        )
      },
      text = {
        Text(
          text = strings.btnAddMemory,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp
        )
      },
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(end = 20.dp, bottom = 24.dp)
    )

    // Add Memory Dialog
    if (showAddDialog) {
      AddMemoryDialog(
        strings = strings,
        currentLanguage = currentLanguage,
        onDismiss = { showAddDialog = false },
        onSave = { title, dateText, photoUri, note, location ->
          viewModel.addSharedMemory(title, dateText, photoUri, note, location)
          showAddDialog = false
        }
      )
    }

    // Detail Dialog
    selectedDetail?.let { memory ->
      MemoryDetailDialog(
        memory = memory,
        strings = strings,
        currentLanguage = currentLanguage,
        onDismiss = { viewModel.closeMemoryDetail() },
        onToggleFavorite = { viewModel.toggleMemoryFavorite(memory) },
        onDelete = {
          viewModel.deleteSharedMemory(memory.id)
        }
      )
    }
  }
}

@Composable
private fun MemoriesHeader(
  strings: com.example.ui.util.AppStrings,
  totalCount: Int,
  selectedFilter: String,
  onFilterChanged: (String) -> Unit,
  onAddClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 6.dp)
  ) {
    // Glass Banner Header
    Surface(
      shape = RoundedCornerShape(24.dp),
      color = Color.White.copy(alpha = 0.88f),
      shadowElevation = 4.dp,
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color(0xFFFFDDE6), RoundedCornerShape(24.dp))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                Color(0xFFFFF0F5),
                Color(0xFFFFE4EC),
                Color(0xFFFFF5F8)
              )
            )
          )
          .padding(18.dp)
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = strings.memoryAlbumTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF880E4F)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = strings.memoryAlbumSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6A1B4D).copy(alpha = 0.85f),
                lineHeight = 18.sp
              )
            }

            // Photo Count Badge
            Surface(
              shape = RoundedCornerShape(16.dp),
              color = Color(0xFFFF2D75).copy(alpha = 0.12f),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF2D75).copy(alpha = 0.3f))
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.PhotoLibrary,
                  contentDescription = null,
                  tint = Color(0xFFFF2D75),
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "$totalCount",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = Color(0xFFFF2D75)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Filter Chips Row
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            FilterChip(
              selected = selectedFilter == "all",
              onClick = { onFilterChanged("all") },
              label = {
                Text(
                  text = if (strings.navHome == "Trang Chủ") "Tất cả ($totalCount)" else "All ($totalCount)",
                  fontWeight = if (selectedFilter == "all") FontWeight.Bold else FontWeight.Medium
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFFFF2D75),
                selectedLabelColor = Color.White
              ),
              shape = RoundedCornerShape(12.dp)
            )

            FilterChip(
              selected = selectedFilter == "fav",
              onClick = { onFilterChanged("fav") },
              label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = if (strings.navHome == "Trang Chủ") "Yêu thích" else "Favorites",
                    fontWeight = if (selectedFilter == "fav") FontWeight.Bold else FontWeight.Medium
                  )
                }
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFFFF2D75),
                selectedLabelColor = Color.White
              ),
              shape = RoundedCornerShape(12.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun MemoryCardItem(
  memory: SharedMemoryEntity,
  onClick: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(18.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(0.85f)
      .clip(RoundedCornerShape(18.dp))
      .border(1.dp, Color(0xFFFFE0E9), RoundedCornerShape(18.dp))
      .clickable { onClick() }
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      // Photo
      AsyncImage(
        model = memory.photoUri,
        contentDescription = memory.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      // Top Shadow Gradient for legible favorite button
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .align(Alignment.TopCenter)
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.Black.copy(alpha = 0.45f), Color.Transparent)
            )
          )
      )

      // Favorite Heart Button at Top End
      IconButton(
        onClick = onToggleFavorite,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(4.dp)
          .size(34.dp)
          .background(Color.Black.copy(alpha = 0.28f), CircleShape)
      ) {
        Icon(
          imageVector = if (memory.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
          contentDescription = "Favorite",
          tint = if (memory.isFavorite) Color(0xFFFF2D75) else Color.White,
          modifier = Modifier.size(18.dp)
        )
      }

      // Bottom Gradient Scrim for readable title & date
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .align(Alignment.BottomCenter)
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.55f),
                Color.Black.copy(alpha = 0.88f)
              )
            )
          )
          .padding(horizontal = 10.dp, vertical = 10.dp)
      ) {
        Column {
          Text(
            text = memory.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Spacer(modifier = Modifier.height(2.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CalendarMonth,
              contentDescription = null,
              tint = Color(0xFFFFB3C6),
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = memory.dateText,
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFFFFE4EC),
              fontSize = 11.sp,
              maxLines = 1
            )
          }

          if (memory.location.isNotBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color(0xFFFFD54F),
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = memory.location,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun EmptyMemoriesCard(
  strings: com.example.ui.util.AppStrings,
  isFavoriteFilter: Boolean,
  onAddClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(24.dp),
    color = Color.White.copy(alpha = 0.9f),
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 32.dp)
      .border(1.dp, Color(0xFFFFDDE6), RoundedCornerShape(24.dp))
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(28.dp)
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(72.dp)
          .background(Color(0xFFFFF0F5), CircleShape)
          .border(2.dp, Color(0xFFFFB3C6), CircleShape)
      ) {
        Icon(
          imageVector = if (isFavoriteFilter) Icons.Default.Favorite else Icons.Default.AddAPhoto,
          contentDescription = null,
          tint = Color(0xFFFF2D75),
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = if (isFavoriteFilter) {
          if (strings.navHome == "Trang Chủ") "Chưa có ảnh yêu thích nào" else "No favorite memories yet"
        } else {
          strings.memoryEmptyTitle
        },
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF880E4F),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = strings.memoryEmptySubtitle,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF6A1B4D).copy(alpha = 0.8f),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = onAddClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2D75)),
        shape = RoundedCornerShape(20.dp)
      ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = strings.btnAddMemory, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
private fun AddMemoryDialog(
  strings: com.example.ui.util.AppStrings,
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit,
  onSave: (title: String, dateText: String, photoUri: String, note: String, location: String) -> Unit
) {
  val context = LocalContext.current
  val todayFormatted = remember {
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
  }

  var title by remember { mutableStateOf("") }
  var dateText by remember { mutableStateOf(todayFormatted) }
  var location by remember { mutableStateOf("") }
  var note by remember { mutableStateOf("") }
  var selectedPhotoUri by remember {
    mutableStateOf("https://images.unsplash.com/photo-1518199266791-5375a83190b7?q=80&w=1080&auto=format&fit=crop")
  }

  // System Photo Picker launcher
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    uri?.let {
      // Copy picked uri to app cache for persistence
      val persistentPath = copyUriToInternalStorage(context, it)
      selectedPhotoUri = persistentPath ?: it.toString()
    }
  }

  // Camera capture launcher (TakePicturePreview returns Bitmap)
  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicturePreview()
  ) { bitmap: Bitmap? ->
    bitmap?.let {
      val savedPath = saveBitmapToInternalStorage(context, it)
      if (savedPath != null) {
        selectedPhotoUri = savedPath
      }
    }
  }

  // Romantic Photo Presets for quick selection
  val presetPhotos = listOf(
    "https://images.unsplash.com/photo-1516589178581-6cd7833ae3b2?q=80&w=800&auto=format&fit=crop",
    "https://images.unsplash.com/photo-1518199266791-5375a83190b7?q=80&w=800&auto=format&fit=crop",
    "https://images.unsplash.com/photo-1529636798458-92182e662485?q=80&w=800&auto=format&fit=crop",
    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=800&auto=format&fit=crop",
    "https://images.unsplash.com/photo-1511285560929-80b456fea0bc?q=80&w=800&auto=format&fit=crop",
    "https://images.unsplash.com/photo-1464349095431-e9a21285b5f3?q=80&w=800&auto=format&fit=crop"
  )

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(26.dp),
      color = Color.White,
      shadowElevation = 10.dp,
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color(0xFFFFDDE6), RoundedCornerShape(26.dp))
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Title Row
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .background(Color(0xFFFF2D75).copy(alpha = 0.12f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = null,
                tint = Color(0xFFFF2D75),
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = strings.memoryTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color(0xFF880E4F)
              )
              Text(
                text = strings.memorySubtitle,
                fontSize = 11.sp,
                color = Color(0xFF6A1B4D).copy(alpha = 0.75f)
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Photo Preview
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(2.dp, Color(0xFFFFC0D3), RoundedCornerShape(18.dp))
        ) {
          AsyncImage(
            model = selectedPhotoUri,
            contentDescription = "Selected memory photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons: Pick Photo or Capture Photo
        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedButton(
            onClick = {
              photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
              )
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.PhotoLibrary,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = strings.memoryPickPhoto, fontSize = 12.sp)
          }

          OutlinedButton(
            onClick = {
              cameraLauncher.launch(null)
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.CameraAlt,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = strings.memoryCapturePhoto, fontSize = 12.sp)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Preset Thumbnails Row
        Text(
          text = strings.memoryPresetNotice,
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF6A1B4D).copy(alpha = 0.8f)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
        ) {
          presetPhotos.forEach { url ->
            val isSelected = selectedPhotoUri == url
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(
                  width = if (isSelected) 2.5.dp else 1.dp,
                  color = if (isSelected) Color(0xFFFF2D75) else Color(0xFFFFE0E9),
                  shape = RoundedCornerShape(12.dp)
                )
                .clickable { selectedPhotoUri = url }
            ) {
              AsyncImage(
                model = url,
                contentDescription = "Preset photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Title Input
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text(strings.memoryTitleLabel) },
          placeholder = { Text("Ví dụ: Chuyến đi Đà Lạt mùa hoa") },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFF2D75),
            unfocusedBorderColor = Color(0xFFFFDDE6)
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Date & Location in a Row
        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedTextField(
            value = dateText,
            onValueChange = { dateText = it },
            label = { Text(strings.memoryDateLabel) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFFFF2D75),
              unfocusedBorderColor = Color(0xFFFFDDE6)
            )
          )

          OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text(strings.memoryLocationLabel) },
            placeholder = { Text("Đà Lạt") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = Color(0xFFFF2D75),
              unfocusedBorderColor = Color(0xFFFFDDE6)
            )
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Note / Feelings Input
        OutlinedTextField(
          value = note,
          onValueChange = { note = it },
          label = { Text(strings.memoryNoteLabel) },
          placeholder = { Text(strings.memoryNotePlaceholder) },
          maxLines = 3,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFF2D75),
            unfocusedBorderColor = Color(0xFFFFDDE6)
          )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Action Buttons: Cancel and Save
        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.weight(1f)
          ) {
            Text(strings.btnCancel)
          }

          Button(
            onClick = {
              val finalTitle = title.ifBlank {
                if (currentLanguage == AppLanguage.VI) "Khoảnh Khắc Kỷ Niệm" else "Special Moment"
              }
              onSave(finalTitle, dateText, selectedPhotoUri, note, location)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2D75)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = strings.btnSaveMemory, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
private fun MemoryDetailDialog(
  memory: SharedMemoryEntity,
  strings: com.example.ui.util.AppStrings,
  currentLanguage: AppLanguage,
  onDismiss: () -> Unit,
  onToggleFavorite: () -> Unit,
  onDelete: () -> Unit
) {
  var showDeleteConfirm by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(26.dp),
      color = Color.White,
      shadowElevation = 12.dp,
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color(0xFFFFDDE6), RoundedCornerShape(26.dp))
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Large High-Res Photo View
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFFFE0E9), RoundedCornerShape(20.dp))
        ) {
          AsyncImage(
            model = memory.photoUri,
            contentDescription = memory.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Top action buttons (Close & Favorite)
          Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp)
          ) {
            IconButton(
              onClick = onDismiss,
              modifier = Modifier
                .size(36.dp)
                .background(Color.Black.copy(alpha = 0.35f), CircleShape)
            ) {
              Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
            }

            IconButton(
              onClick = onToggleFavorite,
              modifier = Modifier
                .size(36.dp)
                .background(Color.Black.copy(alpha = 0.35f), CircleShape)
            ) {
              Icon(
                imageVector = if (memory.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (memory.isFavorite) Color(0xFFFF2D75) else Color.White
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
          text = memory.title,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF880E4F)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date and Location Badges
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFFFF0F5),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFDDE6))
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = Color(0xFFFF2D75),
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = memory.dateText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF880E4F)
              )
            }
          }

          if (memory.location.isNotBlank()) {
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFFFFF8E1),
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE082))
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.LocationOn,
                  contentDescription = null,
                  tint = Color(0xFFF57C00),
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = memory.location,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFFE65100)
                )
              }
            }
          }
        }

        // Note / Romantic feeling
        if (memory.note.isNotBlank()) {
          Spacer(modifier = Modifier.height(14.dp))
          Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFFFF7FA),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFE4EC)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "“${memory.note}”",
              fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
              style = MaterialTheme.typography.bodyMedium,
              color = Color(0xFF5D1049),
              lineHeight = 20.sp,
              modifier = Modifier.padding(14.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Delete Confirm or Delete Action
        if (showDeleteConfirm) {
          Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedButton(
              onClick = { showDeleteConfirm = false },
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text(strings.btnCancel)
            }

            Button(
              onClick = {
                onDelete()
                onDismiss()
              },
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
              shape = RoundedCornerShape(14.dp),
              modifier = Modifier.weight(1f)
            ) {
              Text(if (currentLanguage == AppLanguage.VI) "Xác nhận xóa" else "Confirm Delete")
            }
          }
        } else {
          Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            OutlinedButton(
              onClick = { showDeleteConfirm = true },
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
            ) {
              Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(if (currentLanguage == AppLanguage.VI) "Xóa Ảnh" else "Delete")
            }

            Button(
              onClick = onDismiss,
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2D75))
            ) {
              Text(strings.btnGotIt)
            }
          }
        }
      }
    }
  }
}

// Helpers for persisting photos safely
private fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): String? {
  return try {
    val filename = "shared_mem_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, filename)
    FileOutputStream(file).use { out ->
      bitmap.compress(Bitmap.CompressFormat.JPEG, 92, out)
    }
    Uri.fromFile(file).toString()
  } catch (e: Exception) {
    null
  }
}

private fun copyUriToInternalStorage(context: Context, uri: Uri): String? {
  return try {
    val filename = "picked_mem_${System.currentTimeMillis()}.jpg"
    val destFile = File(context.filesDir, filename)
    context.contentResolver.openInputStream(uri)?.use { input ->
      FileOutputStream(destFile).use { output ->
        input.copyTo(output)
      }
    }
    Uri.fromFile(destFile).toString()
  } catch (e: Exception) {
    null
  }
}
