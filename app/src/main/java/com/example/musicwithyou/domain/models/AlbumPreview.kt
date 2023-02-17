package com.example.musicwithyou.domain.models

data class AlbumPreview(
    val id: Long,
    val title: String,
    val artistId: Long,
    val artistName: String,
    val songsCount: Int,
    val year: Int,
    val imageUri: String,
)
