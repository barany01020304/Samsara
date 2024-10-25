package com.example.samsara

import com.example.samsara.datamodel.ApartmentDataModel
import com.example.samsara.datamodel.PublicService
import com.example.samsara.datamodel.SpecialService
import com.example.samsara.datasource.remote.ApartmentOrder
import com.example.samsara.datasource.remote.RetrofitConfig
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test


class ApartmentOrderTest {

    private val fakeApi: ApartmentOrder = FakeApartmentOrder()
    @Before
    fun setup() {
        ApiConfig.setApi(FakeApartmentOrder()) // Use the fake API for testing
    }

    @Test
    fun getapartmentsreturnssuccessfully() = runBlocking {
        val apartments = fakeApi.getApartments()

        // Verify the mock response
        assertEquals(1, apartments.size)
        assertEquals("Building A", apartments[0].building)
        assertEquals("Los Angeles", apartments[0].location)
    }
}
class FakeApartmentOrder : ApartmentOrder {

    override suspend fun getApartments(): List<ApartmentDataModel> {
        // Return a predefined list of ApartmentDataModel for testing
        return listOf(
            ApartmentDataModel(
                id = 1,
                building = "Building A",
                homeArea = 120,
                description = "A cozy apartment",
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
        )
    }
}
object ApiConfig {
    var apartmentApi: ApartmentOrder = RetrofitConfig.retrofitCreate()

    fun setApi(api: ApartmentOrder) {
        apartmentApi = api
    }
}