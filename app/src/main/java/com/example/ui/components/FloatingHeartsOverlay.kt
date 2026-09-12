package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

enum class ParticleType {
  HEART,
  GOLD_SPARKLE,
  GLOW_HEART
}

data class HeartParticle(
  val id: Long,
  val startXFraction: Float,
  val size: Float,
  val color: Color,
  val animProgress: Animatable<Float, *>,
  val swayAmplitude: Float,
  val swayFrequency: Float,
  val rotationSpeed: Float,
  val type: ParticleType = ParticleType.HEART
)

@Composable
fun FloatingHeartsOverlay(
  trigger: Long,
  isMilestoneCelebration: Boolean = false,
  modifier: Modifier = Modifier
) {
  val particles = remember { mutableStateListOf<HeartParticle>() }

  val romanticHeartColors = listOf(
    Color(0xFFFF2D75), // Hot Pink
    Color(0xFFFF4081), // Vivid Pink
    Color(0xFFFF80AB), // Soft Rose
    Color(0xFFE91E63), // Crimson Love
    Color(0xFFDA2873), // Ruby
    Color(0xFFFFB3C6), // Sweet Pastel
    Color(0xFFFF6699)  // Cherry
  )

  val celebrationColors = listOf(
    Color(0xFFFFD700), // Pure Gold
    Color(0xFFFFB300), // Amber Gold
    Color(0xFFFF4081), // Hot Pink
    Color(0xFFFF2D75), // Rose Magenta
    Color(0xFFFF80AB), // Blossom Pink
    Color(0xFFFFFFFF), // White Sparkle
    Color(0xFFFF6D00)  // Golden Peach
  )

  LaunchedEffect(trigger) {
    if (trigger > 0) {
      // Milestone celebration triggers an epic particle explosion!
      val count = if (isMilestoneCelebration) 52 else 24
      val colors = if (isMilestoneCelebration) celebrationColors else romanticHeartColors

      for (i in 0 until count) {
        val id = System.nanoTime() + i
        val startX = 0.05f + Random.nextFloat() * 0.90f
        val size = if (isMilestoneCelebration) {
          18f + Random.nextFloat() * 38f
        } else {
          20f + Random.nextFloat() * 30f
        }
        val color = colors[Random.nextInt(colors.size)]
        val swayAmp = 15f + Random.nextFloat() * 32f
        val swayFreq = 1.2f + Random.nextFloat() * 3.2f
        val rotSpeed = (Random.nextFloat() - 0.5f) * 80f

        val particleType = if (isMilestoneCelebration) {
          when (Random.nextInt(10)) {
            in 0..5 -> ParticleType.HEART
            in 6..8 -> ParticleType.GOLD_SPARKLE
            else -> ParticleType.GLOW_HEART
          }
        } else {
          if (Random.nextInt(10) > 7) ParticleType.GLOW_HEART else ParticleType.HEART
        }

        val anim = Animatable(0f)
        val particle = HeartParticle(
          id = id,
          startXFraction = startX,
          size = size,
          color = color,
          animProgress = anim,
          swayAmplitude = swayAmp,
          swayFrequency = swayFreq,
          rotationSpeed = rotSpeed,
          type = particleType
        )
        particles.add(particle)

        launch {
          val delayTime = if (isMilestoneCelebration) i * 45L else i * 85L
          delay(delayTime)
          val duration = if (isMilestoneCelebration) {
            2600 + Random.nextInt(1600)
          } else {
            2800 + Random.nextInt(1200)
          }
          anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = duration, easing = LinearEasing)
          )
          particles.remove(particle)
        }
      }
    }
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val canvasWidth = size.width
    val canvasHeight = size.height

    particles.forEach { p ->
      val progress = p.animProgress.value
      // Rises from bottom (92% height) to above top (-10%)
      val currentY = canvasHeight * (0.94f - progress * 1.08f)
      val sway = sin(progress * Math.PI * p.swayFrequency).toFloat() * p.swayAmplitude
      val currentX = (canvasWidth * p.startXFraction) + sway

      // Alpha fades in smoothly at spawn, fades out near ceiling
      val alpha = when {
        progress < 0.12f -> progress / 0.12f
        progress > 0.72f -> (1f - progress) / 0.28f
        else -> 1f
      }.coerceIn(0f, 1f)

      // Dynamic scale
      val scale = 0.55f + progress * 0.55f

      rotate(
        degrees = progress * p.rotationSpeed,
        pivot = Offset(currentX, currentY)
      ) {
        when (p.type) {
          ParticleType.HEART -> {
            drawHeart(
              center = Offset(currentX, currentY),
              size = p.size * scale,
              color = p.color.copy(alpha = alpha * 0.90f)
            )
          }
          ParticleType.GLOW_HEART -> {
            // Halo glow heart
            drawHeart(
              center = Offset(currentX, currentY),
              size = p.size * scale * 1.25f,
              color = p.color.copy(alpha = alpha * 0.35f)
            )
            drawHeart(
              center = Offset(currentX, currentY),
              size = p.size * scale,
              color = p.color.copy(alpha = alpha * 0.95f)
            )
          }
          ParticleType.GOLD_SPARKLE -> {
            drawSparkleStar(
              center = Offset(currentX, currentY),
              radius = (p.size * 0.65f) * scale,
              color = p.color.copy(alpha = alpha * 0.95f)
            )
          }
        }
      }
    }
  }
}

private fun DrawScope.drawHeart(
  center: Offset,
  size: Float,
  color: Color
) {
  val path = Path().apply {
    val width = size
    val height = size * 0.9f
    val x = center.x - width / 2
    val y = center.y - height / 2

    moveTo(x + width / 2, y + height / 5)

    // Top left curve
    cubicTo(
      x + width / 2, y,
      x, y,
      x, y + height / 3
    )

    // Bottom left curve to tip
    cubicTo(
      x, y + height * 2 / 3,
      x + width / 3, y + height * 0.8f,
      x + width / 2, y + height
    )

    // Bottom right curve to tip
    cubicTo(
      x + width * 2 / 3, y + height * 0.8f,
      x + width, y + height * 2 / 3,
      x + width, y + height / 3
    )

    // Top right curve
    cubicTo(
      x + width, y,
      x + width / 2, y,
      x + width / 2, y + height / 5
    )
    close()
  }

  drawPath(path = path, color = color)
}

private fun DrawScope.drawSparkleStar(
  center: Offset,
  radius: Float,
  color: Color
) {
  val path = Path().apply {
    val r = radius
    val inner = radius * 0.28f

    moveTo(center.x, center.y - r)
    quadraticTo(center.x, center.y, center.x + inner, center.y - inner)
    lineTo(center.x + r, center.y)
    quadraticTo(center.x, center.y, center.x + inner, center.y + inner)
    lineTo(center.x, center.y + r)
    quadraticTo(center.x, center.y, center.x - inner, center.y + inner)
    lineTo(center.x - r, center.y)
    quadraticTo(center.x, center.y, center.x - inner, center.y - inner)
    close()
  }

  drawPath(path = path, color = color)
}
