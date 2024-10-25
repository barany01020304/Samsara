package com.example.samsara

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datamodel.PublicService
import com.example.samsara.datamodel.SpecialService
import com.example.samsara.datasource.local.ApartmentDao
import com.example.samsara.datasource.local.ApartmentDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.samsara", appContext.packageName)
    }
}
//@MediumTest
//class UserDatabaseTest {
//
//    private lateinit var apartDatabase: ApartmentDatabase
//    private lateinit var userDao: ApartmentDao
//
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Before
//    fun setup() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        apartDatabase = Room.inMemoryDatabaseBuilder(context, apartDatabase::class.java)
//            .allowMainThreadQueries() // For testing, you can allow main thread queries
//            .build()
//        userDao = apartDatabase.apartmentDao
//    }
//
//    @After
//    fun teardown() {
//        apartDatabase.close()
//    }
//
//    @Test
//    fun insertAndGetUser() = runBlocking {
//        val user = ApartmentDataModel(
//            id = 1,
//            building = "Sunset Apartments",
//            homeArea = 85,
//            description = "A cozy 2-bedroom apartment with a beautiful view of the city.",
//            latitude = 30.0444,
//            longitude = 31.2357,
//            location = "Cairo, Egypt",
//            locationsNeighborhood = "Downtown",
//            locationInMap = "https://maps.google.com/?q=30.0444,31.2357",
//            photos = listOf(
//                "https://example.com/photo1.jpg",
//                "https://example.com/photo2.jpg"
//            ),
//            rating = 4.5,
//            numberOfRatings = 120,
//            type = "Apartment",
//            price = 1500,
//            rooms = 2,
//            specialServices = listOf(
//                SpecialService(
//                    img = "https://example.com/service1.jpg",
//                    service = "Gym"
//                ),
//                SpecialService(
//                    img = "https://example.com/service2.jpg",
//                    service = "Swimming Pool"
//                )
//            ),
//            publicServices = listOf(
//                PublicService(
//                    img = "https://example.com/public1.jpg",
//                    service = "Supermarket",
//                    howFar = "500m"
//                ),
//                PublicService(
//                    img = "https://example.com/public2.jpg",
//                    service = "Bus Stop",
//                    howFar = "300m"
//                )
//            )
//        )
//        userDao.insert(user)
//
//        val retrievedUser = userDao.getAllApartments()
//
//    }
//}