package com.example.samsara.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.samsara.datamodel.ApartmentDataModel

@Dao
interface ApartmentDao {
    //@Query("select * from ApartmentDataModel")
    @Query("SELECT * FROM ApartmentDataModel ORDER BY id DESC")
    suspend fun getAllApartments(): List<ApartmentDataModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(apartment: ApartmentDataModel)
    @Query("DELETE FROM ApartmentDataModel WHERE ApartmentDataModel.photos = :list")
    fun deleteByBuildingName(list:List<String>)

}