package com.example.musicwithyou.domain.models

data class Playlist(
    val id: Int,
    val title: String,
    val songIds: List<Long>,
    val createdTimeStamp: Long,
    val iconId: Int? = null,
    val isDefault: Boolean,
)
