package com.example.musicwithyou.data.local.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson

object DatabaseTypeConverter {

    @TypeConverter
    fun longListToJson(songIds: List<Long>): String = Gson().toJson(songIds)

    @TypeConverter
    fun longListFromJson(json: String): List<Long> {
        return (Gson().fromJson(json, Array<Long>::class.java) ?: emptyArray()).toList()
    }

}