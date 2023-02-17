package com.example.musicwithyou.data.local.room.dao

import androidx.room.*
import com.example.musicwithyou.data.local.room.models.AlbumEntity

@Dao
interface AlbumDao {

    @Transaction
    @Query("SELECT * FROM album_table")
    suspend fun getAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM album_table WHERE id = :id LIMIT 1")
    suspend fun getAlbumById(id: Long): AlbumEntity?

    @Query("SELECT * FROM album_table WHERE artistId = :artistId")
    suspend fun getAlbumsByArtistId(artistId: Long): List<AlbumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    @Query("DELETE FROM album_table")
    suspend fun deleteAll()

}