package com.example.musicwithyou.data.local.room.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PlaylistWithSongs(
    @Embedded val playlistEntity: PlaylistEntity,
    @Relation(
        entity = SongEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            SortedSongPlaylistCrossRef::class,
            parentColumn = "playlistId",
            entityColumn = "songId"
        )
    )
    val songs: List<SongEntity>,
)
