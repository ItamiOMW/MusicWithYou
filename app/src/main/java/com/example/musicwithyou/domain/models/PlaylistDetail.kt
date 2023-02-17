package com.example.musicwithyou.domain.models

import com.example.musicwithyou.utils.UNKNOWN_ID_LONG

data class PlaylistDetail(
    val id: Long = UNKNOWN_ID_LONG,
    val title: String,
    val songs: List<Song>,
    val createdTimeStamp: Long,
    val iconId: Int? = null,
    val isDefault: Boolean = false,
)
