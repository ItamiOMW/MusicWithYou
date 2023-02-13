package com.example.musicwithyou.data.mapper

import com.example.musicwithyou.data.local.room.models.PlaylistEntity
import com.example.musicwithyou.domain.models.Playlist


fun PlaylistEntity.toPlaylist() = Playlist(
    id = id,
    title = title,
    songIds = songIds,
    createdTimeStamp = createdTimeStamp,
    iconId = iconId,
    isDefault = isDefault
)

fun Playlist.toPlaylistEntity() = PlaylistEntity(
    id = id,
    title = title,
    songIds = songIds,
    createdTimeStamp = createdTimeStamp,
    iconId = iconId,
    isDefault = isDefault
)

