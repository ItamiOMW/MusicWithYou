package com.example.musicwithyou.domain.models

import com.example.musicwithyou.utils.UNKNOWN_ID_LONG


data class PlaylistPreview(
    val id: Long = UNKNOWN_ID_LONG,
    val title: String,
    val songCount: Int,
    val createdTimeStamp: Long,
    val iconId: Int? = null,
    val isDefault: Boolean = false,
)
