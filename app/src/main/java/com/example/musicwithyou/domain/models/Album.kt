package com.example.musicwithyou.domain.models

data class Album(
    val id: Long,
    val title: String,
    val artistId: Long,
    val artistName: String,
    val songs: List<Song>,
    val uriString: String,
)