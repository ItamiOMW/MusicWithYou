package com.example.musicwithyou.data.local.content_resolver

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import android.util.LongSparseArray
import com.example.musicwithyou.data.local.room.models.ArtistEntity
import javax.inject.Inject

class ArtistsContentResolver @Inject constructor(
    private val application: Application,
) : ContentResolverHelper<ArtistEntity> {

    override suspend fun getData(): List<ArtistEntity> {

        val albumArtPerAristId = getArtistsImages()

        val artistList = mutableListOf<ArtistEntity>()

        val cursor = application.contentResolver.query(
            MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use { crsr ->

            val idColumn = crsr.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)

            val nameColumn = crsr.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST)

            val songsCountColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)

            val albumsCountColumn =
                crsr.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)

            crsr.apply {
                if (count != 0) {
                    while (crsr.moveToNext()) {
                        val id = getLong(idColumn)
                        val name = getString(nameColumn)
                        val songsCount = getInt(songsCountColumn)
                        val albumsCount = getInt(albumsCountColumn)

                        val artist = ArtistEntity(
                            id,
                            name,
                            albumsCount,
                            songsCount,
                            albumArtPerAristId[id]
                        )
                        artistList.add(artist)
                    }
                }
            }
        }
        cursor?.close()
        return artistList
    }

    private fun getArtistsImages() = LongSparseArray<String?>().also { artistAlbumArtId ->
        application.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Albums._ID
            ),
            null,
            null,
            null
        )?.use { crsr ->
            val albumIdColumn = crsr.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)
            val artistIdColumn = crsr.getColumnIndexOrThrow(MediaStore.Audio.Artists._ID)
            val albumInfo = buildList(crsr.count) {
                while (crsr.moveToNext()) {
                    val albumId = crsr.getLong(albumIdColumn)
                    val artistId = crsr.getLong(artistIdColumn)
                    this += AlbumArtInfo(
                        artistId = artistId,
                        albumArtPath = ContentUris.withAppendedId(
                            MediaStoreInternals.CONTENT_URI,
                            albumId
                        ).toString()
                    )
                }
            }
            albumInfo.forEach {
                artistAlbumArtId.put(it.artistId, it.albumArtPath)
            }
        }
    }

    private data class AlbumArtInfo(
        val artistId: Long,
        val albumArtPath: String?,
    )

    companion object {

        private val projection: Array<String> = arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        )


    }

}