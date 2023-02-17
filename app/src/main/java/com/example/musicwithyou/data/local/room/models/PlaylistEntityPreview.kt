package com.example.musicwithyou.data.local.room.models

import androidx.room.Embedded


data class PlaylistEntityPreview(
    @Embedded val playlist: PlaylistEntity,
    val songCount: Int,
)
