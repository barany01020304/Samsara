package com.example.samsara.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.samsara.datamodel.ApartmentDataModel

@Dao
interface ApartmentDao {
    @Query("select * from ApartmentDataModel")
    suspend fun getAllApartments(): List<ApartmentDataModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(apartment: ApartmentDataModel)
    @Query("DELETE FROM ApartmentDataModel WHERE id = :id")
    fun deleteByBuildingName(id: String)

}