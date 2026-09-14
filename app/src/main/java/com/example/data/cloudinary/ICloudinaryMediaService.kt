package com.example.data.cloudinary

import android.content.Context
import android.net.Uri

/**
 * Service Layer Interface for handling image and video uploads to Cloudinary.
 * Ensures media attached to love anniversary memories are stored securely
 * and retrieved for display within the app.
 */
interface ICloudinaryMediaService {
  /**
   * Validates media file size and video duration according to policy limits:
   * Photo <= 10MB, Video <= 50MB & <= 60 seconds.
   */
  fun validateMedia(context: Context, uri: Uri, isVideo: Boolean): MediaValidationResult

  /**
   * Securely uploads media (image or video) attached to a love anniversary memory to Cloudinary.
   */
  suspend fun uploadMemoryMedia(
    context: Context,
    mediaUri: Uri,
    isVideo: Boolean,
    memoryTitle: String,
    relationshipId: String?,
    authorUid: String?
  ): CloudinaryUploadResult

  /**
   * Returns an optimized display URL for Cloudinary images with auto-format and compression.
   */
  fun getOptimizedImageUrl(publicIdOrUrl: String, maxWidth: Int = 1080): String

  /**
   * Returns an optimized streaming URL for Cloudinary videos.
   */
  fun getVideoStreamUrl(publicIdOrUrl: String): String

  /**
   * Returns the secure thumbnail URL for a video memory.
   */
  fun getVideoThumbnailUrl(publicIdOrUrl: String, fallbackUrl: String = ""): String
}
