package com.example.musicwithyou.data.local.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.utils.UNKNOWN_ID


@Entity("play_list_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = UNKNOWN_ID,
    val title: String,
    val songs: List<Song>,
    val createdTimeStamp: Long,
    val isEditable: Boolean,
)
