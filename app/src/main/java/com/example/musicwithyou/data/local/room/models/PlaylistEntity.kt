package com.example.musicwithyou.data.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musicwithyou.R
import com.example.musicwithyou.utils.UNKNOWN_ID_LONG


@Entity("playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = UNKNOWN_ID_LONG,
    val title: String,
    val createdTimeStamp: Long,
    val iconId: Int? = R.drawable.playlist,
    val isDefault: Boolean = false,
)
