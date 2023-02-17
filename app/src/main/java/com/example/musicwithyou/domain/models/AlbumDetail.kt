package com.example.musicwithyou.domain.models

data class AlbumDetail(
    val id: Long,
    val title: String,
    val artistId: Long,
    val artistName: String,
    val songsCount: Int,
    val songs: List<Song>,
    val year: Int,
    val imageUri: String,
)