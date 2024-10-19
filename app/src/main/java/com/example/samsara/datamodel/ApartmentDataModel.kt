package com.example.samsara.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey


//data class ApartmentResponse(
//    val apartments: List<Apartment>
//)
@Entity
data class ApartmentDataModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val building: String?,
    val homeArea:Int,
    val description: String?,
    val latitude: Double,
    val longitude: Double,
    val location: String?,
    val locationsNeighborhood :String,
    val locationInMap: String?,
    val photos: List<String>,
    val rating: Double,
    val numberOfRatings: Int,
    val type: String?,
    val price: Int,
    val rooms: Int,
    val specialServices: List<SpecialService>,
    val publicServices: List<PublicService>
)

