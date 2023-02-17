package com.example.musicwithyou.data.mapper

import com.example.musicwithyou.data.local.room.models.PlaylistEntity
import com.example.musicwithyou.domain.models.PlaylistDetail
import com.example.musicwithyou.domain.models.PlaylistPreview
import com.example.musicwithyou.domain.models.Song


fun PlaylistEntity.toPlaylist(songs: List<Song>) = PlaylistDetail(
    id = id,
    title = title,
    songs = songs,
    createdTimeStamp = createdTimeStamp,
    iconId = iconId,
    isDefault = isDefault
)

fun PlaylistEntity.toPlaylistPreview(songCount: Int) = PlaylistPreview(
    id = id,
    title = title,
    createdTimeStamp = createdTimeStamp,
    iconId = iconId,
    isDefault = isDefault,
    songCount = songCount
)

fun PlaylistDetail.toPlaylistEntity() = PlaylistEntity(
    id = id,
    title = title,
    createdTimeStamp = createdTimeStamp,
    iconId = iconId,
    isDefault = isDefault
)

fun PlaylistPreview.toPlaylistEntity() = PlaylistEntity(
    id = id,
    title = title,
    createdTimeStamp = createdTimeStamp,
    iconId = iconId,
    isDefault = isDefault
)

