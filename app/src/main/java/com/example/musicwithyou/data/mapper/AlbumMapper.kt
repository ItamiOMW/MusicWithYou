package com.example.musicwithyou.data.mapper

import com.example.musicwithyou.data.local.room.models.AlbumEntity
import com.example.musicwithyou.domain.models.Album
import com.example.musicwithyou.domain.models.Song


fun AlbumEntity.toAlbum(songs: List<Song>) = Album(
    id = id,
    title = title,
    artistId = artistId,
    artistName = artistName,
    songs = songs,
    year = year,
    imageUri = imageUri,
    songsCount = songsCount
)

fun Album.toAlbumEntity() = AlbumEntity(
    id = id,
    title = title,
    artistId = artistId,
    artistName = artistName,
    year = year,
    imageUri = imageUri,
    songsCount = songsCount
)