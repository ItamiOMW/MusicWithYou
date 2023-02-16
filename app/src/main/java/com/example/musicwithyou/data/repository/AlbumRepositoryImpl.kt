package com.example.musicwithyou.data.repository

import com.example.musicwithyou.data.local.content_resolver.AlbumContentResolver
import com.example.musicwithyou.data.local.room.dao.AlbumDao
import com.example.musicwithyou.data.local.room.dao.SongDao
import com.example.musicwithyou.data.mapper.toAlbum
import com.example.musicwithyou.data.mapper.toSong
import com.example.musicwithyou.domain.models.Album
import com.example.musicwithyou.domain.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao,
    private val songDao: SongDao,
    private val albumContentResolver: AlbumContentResolver,
) : AlbumRepository {

    override suspend fun getAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val albumsToAdd = albumContentResolver.getData()
        albumDao.insertAll(albumsToAdd)
        val allSongs = songDao.getAllSongs().map { songEntity ->
            songEntity.toSong()
        }
        val albumsEntities = albumDao.getAlbums()
        albumsEntities.map { albumEntity ->
            val albumSongs = allSongs.filter { it.albumId == albumEntity.id }
            albumEntity.toAlbum(albumSongs)
        }
    }

    override suspend fun getAlbumById(id: Long): Album? {
        val allSongs = songDao.getAllSongs().map { songEntity ->
            songEntity.toSong()
        }
        val albumEntity = albumDao.getAlbumWithSongsById(id) ?: return null
        return albumEntity.toAlbum(
            allSongs.filter { it.albumId == albumEntity.id }
        )
    }

}