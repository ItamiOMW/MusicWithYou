package com.example.musicwithyou.data.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("artist_table")
data class ArtistEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val albumCount: Int,
    val songsCount: Int,
    val imageUri: String?,
)
