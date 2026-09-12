package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.model.CoupleProfileEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var db: AppDatabase

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
  }

  @After
  fun closeDb() {
    db.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("InLove", appName)
  }

  @Test
  fun `insert and retrieve couple profile in Room`() = runBlocking {
    val profile = CoupleProfileEntity(
      id = 1,
      partner1Name = "Mhoang",
      partner1Birthday = "15/10/2004",
      partner1ProfilePicture = "https://example.com/boy.png",
      partner1Age = 20,
      partner1Zodiac = "Thiên Bình",
      partner2Name = "TLinh",
      partner2Birthday = "24/07/2003",
      partner2ProfilePicture = "https://example.com/girl.png",
      partner2Age = 21,
      partner2Zodiac = "Cự Giải",
      loveTitle = "Bámmmm",
      loveDays = 1349,
      anniversaryDate = "18/12/2022"
    )

    db.inLoveDao().insertCoupleProfile(profile)
    val retrieved = db.inLoveDao().getCoupleProfile().first()

    assertNotNull(retrieved)
    assertEquals("Mhoang", retrieved?.partner1Name)
    assertEquals("TLinh", retrieved?.partner2Name)
    assertEquals("15/10/2004", retrieved?.partner1Birthday)
    assertEquals("24/07/2003", retrieved?.partner2Birthday)
    assertEquals("https://example.com/boy.png", retrieved?.partner1ProfilePicture)
    assertEquals("https://example.com/girl.png", retrieved?.partner2ProfilePicture)
    assertEquals(1349, retrieved?.loveDays)
  }
}
