package com.example.musicwithyou.domain.models

data class PlayList(
    val id: Int,
    val title: String,
    val songs: List<Song>,
    val createdTimeStamp: Long,
    val isDeletable: Boolean,
    val isEditable: Boolean,
)
