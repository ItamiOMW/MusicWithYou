package com.example.musicwithyou.data.local.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.musicwithyou.R
import com.example.musicwithyou.data.local.room.dao.AlbumDao
import com.example.musicwithyou.data.local.room.dao.PlaylistDao
import com.example.musicwithyou.data.local.room.dao.SongDao
import com.example.musicwithyou.data.local.room.models.*
import com.example.musicwithyou.utils.FAVORITE_PLAYLIST_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        PlaylistEntity::class,
        SongEntity::class,
        SongPlaylistCrossRef::class,
        AlbumEntity::class
    ],
    exportSchema = false,
    version = 4,
    views = [
        SortedSongPlaylistCrossRef::class
    ]
)

abstract class MusicWithYouDataBase : RoomDatabase() {

    abstract fun playListDao(): PlaylistDao

    abstract fun songDao(): SongDao

    abstract fun albumDao(): AlbumDao


    companion object {

        private const val DB_NAME = "musicWithYou.db"

        private var instance: MusicWithYouDataBase? = null

        fun getInstance(application: Application): MusicWithYouDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(application).also { instance = it }
            }
        }

        private fun buildDatabase(application: Application): MusicWithYouDataBase {
            return Room.databaseBuilder(
                application,
                MusicWithYouDataBase::class.java,
                DB_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    instance?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            instance?.playListDao()?.createPlaylist(
                                PlaylistEntity(
                                    id = FAVORITE_PLAYLIST_ID,
                                    title = application.getString(R.string.favorites_playlist_title),
                                    createdTimeStamp = System.currentTimeMillis(),
                                    isDefault = true,
                                    iconId = R.drawable.is_favorite,
                                ),
                                emptyList()
                            )
                        }
                    }
                }
            }).build()
        }

    }

}