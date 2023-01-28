package com.example.musicwithyou.data.local.content_resolver

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.example.musicwithyou.domain.models.Song
import javax.inject.Inject

class SongContentResolver @Inject constructor(
    private val application: Application,
) : ContentResolverHelper<Song> {
    override fun getData(): List<Song> {

        val songList = mutableListOf<Song>()

        val cursor = application.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClause,
            selectionArg,
            null
        )

        while (cursor?.moveToNext() == true) {
            val id = cursor.getLong(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            )
            val title = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            )
            val year = cursor.getInt(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)
            )
            val data = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            )
            val duration = cursor.getLong(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            )
            val albumId = cursor.getLong(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)
            )
            val albumName = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)
            )
            val artistId = cursor.getLong(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)
            )
            val artistName = cursor.getString(
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            )
            val uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId)
            val song = Song(
                id,
                title,
                uri.toString(),
                data,
                duration,
                year,
                albumId,
                albumName,
                artistId,
                artistName
            )
            songList.add(song)
        }
        cursor?.close()
        return songList
    }

    companion object {

        private val projection: Array<String> = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ARTIST_ID,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.YEAR,
        )

        private const val selectionClause: String = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"

        private val selectionArg = arrayOf("1")
    }

}