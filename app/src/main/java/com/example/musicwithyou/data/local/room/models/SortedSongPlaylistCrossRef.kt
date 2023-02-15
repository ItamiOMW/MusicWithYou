package com.example.musicwithyou.data.local.room.models

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("SELECT * FROM song_playlist_table ORDER BY position")
data class SortedSongPlaylistCrossRef(
    @ColumnInfo(index = true)
    val songId: Long,
    @ColumnInfo(index = true)
    val playlistId: Long,
    val position: Int = 0,
)
