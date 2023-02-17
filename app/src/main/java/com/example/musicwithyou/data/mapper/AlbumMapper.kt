package com.example.musicwithyou.data.mapper

import com.example.musicwithyou.data.local.room.models.AlbumEntity
import com.example.musicwithyou.domain.models.AlbumDetail
import com.example.musicwithyou.domain.models.AlbumPreview
import com.example.musicwithyou.domain.models.Song


fun AlbumEntity.toAlbumDetail(songs: List<Song>) = AlbumDetail(
    id = id,
    title = title,
    artistId = artistId,
    artistName = artistName,
    songs = songs,
    year = year,
    imageUri = imageUri,
    songsCount = songsCount
)

fun AlbumEntity.toAlbumPreview() = AlbumPreview(
    id = id,
    title = title,
    artistId = artistId,
    artistName = artistName,
    year = year,
    imageUri = imageUri,
    songsCount = songsCount
)

fun AlbumDetail.toAlbumEntity() = AlbumEntity(
    id = id,
    title = title,
    artistId = artistId,
    artistName = artistName,
    year = year,
    imageUri = imageUri,
    songsCount = songsCount
)