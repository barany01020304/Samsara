package com.example.samsara.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.samsara.datamodel.ApartmentDataModel

@Database(entities = [ApartmentDataModel::class], version = 1, exportSchema = false)
@TypeConverters(ConvertersForRoom::class)
abstract class ApartmentDatabase:RoomDatabase() {
    abstract val apartmentDao:ApartmentDao
    companion object{
        @Volatile
       private lateinit var instance:ApartmentDatabase
        fun getInstance(context: Context):ApartmentDatabase{
            synchronized(ApartmentDatabase::class.java){
                if (!::instance.isInitialized){
                    instance=Room.databaseBuilder(context,ApartmentDatabase::class.java,"apartment_db").fallbackToDestructiveMigration().build()
                }
            }
            return instance
        }
    }
}