package com.example.musicwithyou.data.local.content_resolver

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.example.musicwithyou.data.local.room.models.AlbumEntity
import javax.inject.Inject

class AlbumContentResolver @Inject constructor(
    private val application: Application,
) : ContentResolverHelper<AlbumEntity> {


    @WorkerThread
    override fun getData(): List<AlbumEntity> {

        val albumList = mutableListOf<AlbumEntity>()

        val cursor = application.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use { crsr ->
            val idColumn = crsr.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)
            val titleColumn = crsr.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ALBUM)
            val yearColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.LAST_YEAR)
            val artistIdColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST_ID)
            val artistNameColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST)
            val songsCountColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS)

            crsr.apply {
                if (count != 0) {
                    while (crsr.moveToNext()) {
                        val id = getLong(idColumn)
                        val title = getString(titleColumn)
                        val year = getInt(yearColumn)
                        val artistId = getLong(artistIdColumn)
                        val artistName = getString(artistNameColumn)
                        val songsCount = getInt(songsCountColumn)
                        val imageUri = Uri.withAppendedPath(
                            Uri.parse("content://media/external/audio/albumart"),
                            id.toString()
                        )
                        val album = AlbumEntity(
                            id,
                            title,
                            artistId,
                            artistName,
                            songsCount,
                            year,
                            imageUri.toString()
                        )
                        albumList.add(album)
                    }
                }
            }
        }
        cursor?.close()
        return albumList
    }

    companion object {

        private val projection: Array<String> = arrayOf(
            MediaStore.Audio.AlbumColumns.ALBUM,
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.AlbumColumns.ARTIST,
            MediaStore.Audio.AlbumColumns.ARTIST_ID,
            MediaStore.Audio.AlbumColumns.LAST_YEAR,
            MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS
        )

    }
}