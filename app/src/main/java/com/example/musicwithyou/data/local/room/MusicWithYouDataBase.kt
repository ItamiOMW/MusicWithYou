package com.example.musicwithyou.data.local.room

import androidx.room.RoomDatabase
import com.example.musicwithyou.data.local.room.dao.PlayListDao

//@Database() TODO implement entities
abstract class MusicWithYouDataBase : RoomDatabase() {

    abstract fun playListDao(): PlayListDao

}