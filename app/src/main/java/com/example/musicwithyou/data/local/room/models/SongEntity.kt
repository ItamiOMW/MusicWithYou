package com.example.musicwithyou.data.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("song_table")
data class SongEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val songUri: String,
    val imageUri: String,
    val duration: Long,
    val year: Int,
    val albumId: Long,
    val albumName: String,
    val artistId: Long,
    val artistName: String,
)
