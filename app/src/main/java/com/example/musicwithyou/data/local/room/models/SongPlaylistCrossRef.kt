package com.example.musicwithyou.data.local.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "song_playlist_table",
    primaryKeys = ["songId", "playlistId"],
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SongPlaylistCrossRef(
    @ColumnInfo(index = true)
    val songId: Long,
    @ColumnInfo(index = true)
    val playlistId: Long,
    val position: Int = 0,
)
