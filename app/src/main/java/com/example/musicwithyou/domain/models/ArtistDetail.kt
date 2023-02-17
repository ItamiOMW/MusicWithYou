package com.example.musicwithyou.domain.models

data class ArtistDetail(
    val id: Long,
    val name: String,
    val albumPreviews: List<AlbumPreview>,
    val songs: List<Song>,
    val albumCount: Int,
    val songsCount: Int,
    val imageUri: String?,
)
