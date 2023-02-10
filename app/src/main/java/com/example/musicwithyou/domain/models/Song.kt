package com.example.musicwithyou.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
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
): Parcelable
