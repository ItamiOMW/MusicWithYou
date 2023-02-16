package com.example.musicwithyou.data.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("album_table")
data class AlbumEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val artistId: Long,
    val artistName: String,
    val songsCount: Int,
    val year: Int,
    val imageUri: String,
)
