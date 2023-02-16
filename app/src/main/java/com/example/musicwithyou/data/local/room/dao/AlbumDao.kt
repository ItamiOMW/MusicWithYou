package com.example.musicwithyou.data.local.room.dao

import androidx.room.*
import com.example.musicwithyou.data.local.room.models.AlbumEntity

@Dao
interface AlbumDao {

    @Transaction
    @Query("SELECT * FROM album_table")
    fun getAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM album_table WHERE id = :id LIMIT 1")
    fun getAlbumWithSongsById(id: Long): AlbumEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    @Query("DELETE FROM album_table")
    suspend fun deleteAll()

}