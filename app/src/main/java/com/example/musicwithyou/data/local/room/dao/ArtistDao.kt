package com.example.musicwithyou.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.musicwithyou.data.local.room.models.ArtistEntity

@Dao
interface ArtistDao {

    @Query("SELECT * FROM artist_table")
    suspend fun getArtists(): List<ArtistEntity>

    @Query("SELECT * FROM artist_table WHERE id = :id LIMIT 1")
    suspend fun getArtistById(id: Long): ArtistEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<ArtistEntity>)

    @Query("DELETE FROM artist_table WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM artist_table")
    suspend fun deleteAll()

}