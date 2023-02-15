package com.example.musicwithyou.data.mapper

import com.example.musicwithyou.data.local.room.models.PlaylistEntity
import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.models.Song


fun PlaylistEntity.toPlaylist(songs: List<Song>) = Playlist(
    id = id,
    title = title,
    songs = songs,
    createdTimeStamp = createdTimeStamp,
    iconId = iconId,
    isDefault = isDefault
)

fun Playlist.toPlaylistEntity() = PlaylistEntity(
    id = id,
    title = title,
    createdTimeStamp = createdTimeStamp,
    iconId = iconId,
    isDefault = isDefault
)

