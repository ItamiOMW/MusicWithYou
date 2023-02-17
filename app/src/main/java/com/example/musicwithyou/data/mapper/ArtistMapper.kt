package com.example.musicwithyou.data.mapper

import com.example.musicwithyou.data.local.room.models.ArtistEntity
import com.example.musicwithyou.domain.models.*


fun ArtistEntity.toArtistDetail(songs: List<Song>, albums: List<AlbumPreview>) = ArtistDetail(
    id = id,
    name = name,
    albumPreviews = albums,
    songs = songs,
    albumCount = albumCount,
    songsCount = songsCount,
    imageUri = imageUri
)

fun ArtistEntity.toArtistPreview() = ArtistPreview(
    id = id,
    name = name,
    albumCount = albumCount,
    songsCount = songsCount,
    imageUri = imageUri
)