package com.example.musicwithyou.data.local.room.dao

import androidx.room.*
import com.example.musicwithyou.data.local.room.models.SongEntity

@Dao
interface SongDao {

    @Query("SELECT * FROM song_table")
    suspend fun getAllSongs(): List<SongEntity>

    @Query("SELECT * FROM song_table WHERE id = :id")
    suspend fun getSongById(id: Long): SongEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(songList: List<SongEntity>)

    @Update()
    suspend fun update(song: SongEntity)

    @Query("DELETE FROM song_table")
    suspend fun deleteAll()

}