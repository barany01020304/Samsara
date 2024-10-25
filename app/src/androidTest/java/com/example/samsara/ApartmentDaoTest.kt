package com.example.samsara

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datamodel.PublicService
import com.example.samsara.datamodel.SpecialService
import com.example.samsara.datasource.local.ApartmentDao
import com.example.samsara.datasource.local.ApartmentDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ApartmentDaoTest {

    private lateinit var db: ApartmentDatabase
    private lateinit var dao: ApartmentDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ApartmentDatabase::class.java
        )
            .allowMainThreadQueries() // Only for testing; avoid this in production code
            .build()

        dao = db.apartmentDao
    }
    @Test
    fun insertAndRetrieveApartment() = runBlocking {
        // Create a sample apartment object
        val apartment = ApartmentDataModel(
            id = 1,
            building = "Building A",
            homeArea = 120,
            description = "A nice apartment",
            latitude = 34.0522,
            longitude = -118.2437,
            location = "Los Angeles",
            locationsNeighborhood = "Downtown",
            locationInMap = "https://maps.example.com/apartmentA",
            photos = listOf("photo1.png", "photo2.png"),
            rating = 4.5,
            numberOfRatings = 10,
            type = "Residential",
            price = 3000,
            rooms = 3,
            specialServices = listOf(SpecialService("wifi.png", "WiFi")),
            publicServices = listOf(PublicService("park.png", "Park", "5 km"))
        )

        // Insert the apartment
        dao.insert(apartment)

        // Retrieve all apartments
        val apartments = dao.getAllApartments()

        // Assert that the data retrieved is as expected
        assertEquals(1, apartments.size)
        assertEquals(apartment.building, apartments[0].building)
        assertEquals(apartment.location, apartments[0].location)
    }
    @Test
    fun deleteByPhotos() = runBlocking {
        // Insert apartments with unique photo lists
        val apartment1 = ApartmentDataModel(
            id = 1,
            building = "Building A",
            homeArea = 120,
            description = "A nice apartment",
            latitude = 34.0522,
            longitude = -118.2437,
            location = "Los Angeles",
            locationsNeighborhood = "Downtown",
            locationInMap = "https://maps.example.com/apartmentA",
            photos = listOf("photo1.png", "photo2.png"),
            rating = 4.5,
            numberOfRatings = 10,
            type = "Residential",
            price = 3000,
            rooms = 3,
            specialServices = listOf(SpecialService("wifi.png", "WiFi")),
            publicServices = listOf(PublicService("park.png", "Park", "5 min"))
        )
        dao.insert(apartment1)

        // Delete by photos
        dao.deleteByBuildingName(listOf("photo1.png", "photo2.png"))

        // Verify deletion
        val apartmentsAfterDelete = dao.getAllApartments()
        assertTrue(apartmentsAfterDelete.isEmpty())
    }
    @After
    fun teardown() {
        db.close()
    }
}