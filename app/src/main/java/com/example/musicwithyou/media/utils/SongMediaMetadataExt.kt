package com.example.musicwithyou.media.utils

import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import com.example.musicwithyou.domain.models.Song


fun Song.toMediaMetadata(): MediaMetadataCompat {

    return MediaMetadataCompat.Builder()
        .putString(
            MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
            this.id.toString()
        )
        .putString(
            MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
            this.songUri
        ).putString(
            MediaMetadataCompat.METADATA_KEY_TITLE,
            this.title
        ).putString(
            MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
            this.title
        ).putString(
            MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
            this.imageUri
        ).putLong(
            MediaMetadataCompat.METADATA_KEY_YEAR,
            this.year.toLong()
        ).putLong(
            DURATION_KEY,
            this.duration
        )
        .putLong(
            ALBUM_ID_KEY,
            this.albumId
        )
        .putString(
            MediaMetadataCompat.METADATA_KEY_ALBUM,
            this.albumName
        ).putLong(
            ARTISTS_ID_KEY,
            this.artistId
        ).putString(
            MediaMetadataCompat.METADATA_KEY_ARTIST,
            this.artistName
        )
        .build()

}

fun MediaMetadataCompat.toSong(): Song {
    val mediaId = this.description.mediaId?.toLong() ?: throw Exception("Nullable id")
    val title = this.description.title.toString()
    val songUri = this.description.mediaUri.toString()
    val imageUri =
        this.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
            .toString()
    val year = this.getLong(MediaMetadataCompat.METADATA_KEY_YEAR).toInt()
    val duration = this.getLong(DURATION_KEY)
    val albumId = this.getLong(ALBUM_ID_KEY)
    val albumName = this.getString(MediaMetadataCompat.METADATA_KEY_ALBUM).toString()
    val artistId = this.getLong(ARTISTS_ID_KEY)
    val artistName = this.getString(MediaMetadataCompat.METADATA_KEY_ARTIST).toString()
    return Song(
        mediaId,
        title,
        songUri,
        imageUri,
        duration,
        year,
        albumId,
        albumName,
        artistId,
        artistName
    )
}


fun MediaMetadataCompat.toMediaDescription(): MediaDescriptionCompat {
    val imageUri = this.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI).toString()
    val year = this.getLong(MediaMetadataCompat.METADATA_KEY_YEAR).toInt()
    val duration = this.getLong(DURATION_KEY)
    val albumId = this.getLong(ALBUM_ID_KEY)
    val albumName = this.getString(MediaMetadataCompat.METADATA_KEY_ALBUM).toString()
    val artistId = this.getLong(ARTISTS_ID_KEY)
    val artistName = this.getString(MediaMetadataCompat.METADATA_KEY_ARTIST).toString()

    return MediaDescriptionCompat.Builder()
        .setMediaId(this.description.mediaId)
        .setDescription(this.description.description)
        .setTitle(this.description.title)
        .setSubtitle(this.description.subtitle)
        .setIconUri(this.description.iconUri)
        .setMediaUri(this.description.mediaUri)
        .setIconBitmap(this.description.iconBitmap)
        .setExtras(
            Bundle().apply {
                putString(
                    MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                    imageUri
                )
                putLong(
                    MediaMetadataCompat.METADATA_KEY_YEAR,
                    year.toLong()
                )
                putLong(
                    DURATION_KEY,
                    duration
                )
                putLong(
                    ALBUM_ID_KEY,
                    albumId
                )
                putString(
                    MediaMetadataCompat.METADATA_KEY_ALBUM,
                    albumName
                )
                putLong(
                    ARTISTS_ID_KEY,
                    artistId
                )
                putString(
                    MediaMetadataCompat.METADATA_KEY_ARTIST,
                    artistName
                )
            }
        ).build()
}


fun MediaDescriptionCompat.toMediaMetadata(): MediaMetadataCompat {
    val mediaId = this.mediaId?.toLong() ?: throw Exception("Nullable id")
    val title = this.title.toString()
    val songUri = this.mediaUri.toString()
    val imageUri =
        this.extras?.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
            .toString()
    val year = this.extras?.getLong(MediaMetadataCompat.METADATA_KEY_YEAR)?.toInt() ?: 0
    val duration = this.extras?.getLong(DURATION_KEY) ?: 0
    val albumId = this.extras?.getLong(ALBUM_ID_KEY) ?: 0
    val albumName = this.extras?.getString(MediaMetadataCompat.METADATA_KEY_ALBUM).toString()
    val artistId = this.extras?.getLong(ARTISTS_ID_KEY) ?: 0
    val artistName = this.extras?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST).toString()

    return MediaMetadataCompat.Builder()
        .putString(
            MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
            mediaId.toString()
        )
        .putString(
            MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
            songUri
        ).putString(
            MediaMetadataCompat.METADATA_KEY_TITLE,
            title
        ).putString(
            MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
            title
        ).putString(
            MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
            imageUri
        ).putLong(
            MediaMetadataCompat.METADATA_KEY_YEAR,
            year.toLong()
        ).putLong(
            DURATION_KEY,
            duration
        )
        .putLong(
            ALBUM_ID_KEY,
            albumId
        )
        .putString(
            MediaMetadataCompat.METADATA_KEY_ALBUM,
            albumName
        ).putLong(
            ARTISTS_ID_KEY,
            artistId
        ).putString(
            MediaMetadataCompat.METADATA_KEY_ARTIST,
            artistName
        )
        .build()
}
