package com.example.musicwithyou.data.local.content_resolver

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.example.musicwithyou.domain.models.Song
import javax.inject.Inject

class SongContentResolver @Inject constructor(
    private val application: Application,
) : ContentResolverHelper<Song> {

    @WorkerThread
    override fun getData(): List<Song> {

        val songList = mutableListOf<Song>()

        val cursor = application.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClause,
            selectionArg,
            null
        )

        cursor?.use { crsr ->
            val idColumn = crsr.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val titleColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)

            val yearColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.YEAR)

            val durationColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)

            val albumIdColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID)

            val albumNameColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM)

            val artistIdColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID)

            val artistNameColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)

            crsr.apply {
                if (count != 0) {
                    while (crsr.moveToNext()) {
                        val id = getLong(idColumn)
                        val title = getString(titleColumn)
                        val duration = getLong(durationColumn)
                        val year = getInt(yearColumn)
                        val albumId = getLong(albumIdColumn)
                        val albumName = getString(albumNameColumn)
                        val artistId = getLong(artistIdColumn)
                        val artistName = getString(artistNameColumn)
                        val songUri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                        val imageUri = Uri.withAppendedPath(
                            Uri.parse("content://media/external/audio/albumart"),
                            albumId.toString()
                        )
                        val song = Song(
                            id,
                            title,
                            songUri.toString(),
                            imageUri.toString(),
                            duration,
                            year,
                            albumId,
                            albumName,
                            artistId,
                            artistName
                        )
                        songList.add(song)
                    }
                }
            }
        }
        cursor?.close()
        return songList
    }

    companion object {

        private val projection: Array<String> = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ARTIST_ID,
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