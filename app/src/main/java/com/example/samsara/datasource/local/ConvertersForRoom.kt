package com.example.samsara.datasource.local

import androidx.room.TypeConverter
import com.example.samsara.datamodel.PublicService
import com.example.samsara.datamodel.SpecialService
import com.google.gson.Gson

class ConvertersForRoom{
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun stringListToString(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromPublicServiceList(value: String?): List<PublicService>? {
        return value?.let { Gson().fromJson(it, Array<PublicService>::class.java).toList() }
    }

    @TypeConverter
    fun publicServiceListToString(list: List<PublicService>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromSpecialServiceList(value: String?): List<SpecialService>? {
        return value?.let { Gson().fromJson(it, Array<SpecialService>::class.java).toList() }
    }

    @TypeConverter
    fun specialServiceListToString(list: List<SpecialService>?): String? {
        return Gson().toJson(list)
    }
}