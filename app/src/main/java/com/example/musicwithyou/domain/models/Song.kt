package com.example.musicwithyou.domain.models

data class Song(
    val id: Long,
    val title: String,
    val uri: String,
    val data: String,
    val duration: Long,
    val year: Int,
    val albumId: Long,
    val albumName: String,
    val artistId: Long,
    val artistName: String,
)
