package com.example.musicwithyou.domain.models

data class Artist(
    val id: Long,
    val name: String,
    val albums: List<Album>,
)
