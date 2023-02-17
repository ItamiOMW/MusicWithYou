package com.example.musicwithyou.domain.models

data class ArtistPreview(
    val id: Long,
    val name: String,
    val albumCount: Int,
    val songsCount: Int,
    val imageUri: String?,
)
