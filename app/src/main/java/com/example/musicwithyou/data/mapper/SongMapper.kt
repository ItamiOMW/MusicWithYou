package com.example.musicwithyou.data.mapper

import com.example.musicwithyou.data.local.room.models.SongEntity
import com.example.musicwithyou.domain.models.Song


fun Song.toSongEntity() = SongEntity(
    id = id,
    title = title,
    songUri = songUri,
    imageUri = imageUri,
    duration = duration,
    year = year,
    albumId = albumId,
    albumName = albumName,
    artistId = artistId,
    artistName = artistName,
)

fun SongEntity.toSong() = Song(
    id = id,
    title = title,
    songUri = songUri,
    imageUri = imageUri,
    duration = duration,
    year = year,
    albumId = albumId,
    albumName = albumName,
    artistId = artistId,
    artistName = artistName,
)