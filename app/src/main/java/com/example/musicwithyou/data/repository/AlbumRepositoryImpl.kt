package com.example.musicwithyou.data.repository

import com.example.musicwithyou.data.local.content_resolver.AlbumContentResolver
import com.example.musicwithyou.data.local.room.dao.AlbumDao
import com.example.musicwithyou.data.local.room.dao.SongDao
import com.example.musicwithyou.data.mapper.toAlbumDetail
import com.example.musicwithyou.data.mapper.toAlbumPreview
import com.example.musicwithyou.data.mapper.toSong
import com.example.musicwithyou.domain.models.AlbumDetail
import com.example.musicwithyou.domain.models.AlbumPreview
import com.example.musicwithyou.domain.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao,
    private val songDao: SongDao,
    private val albumContentResolver: AlbumContentResolver,
) : AlbumRepository {

    override suspend fun getAlbumPreviews(): List<AlbumPreview> = withContext(Dispatchers.IO) {
        val albumsToAdd = albumContentResolver.getData()
        albumDao.insertAll(albumsToAdd)
        albumDao.getAlbums().map { albumEntity ->
            albumEntity.toAlbumPreview()
        }
    }

    override suspend fun getAlbumDetailById(id: Long): AlbumDetail? {
        val allSongs = songDao.getAllSongs().map { songEntity ->
            songEntity.toSong()
        }
        val albumEntity = albumDao.getAlbumById(id) ?: return null
        return albumEntity.toAlbumDetail(
            allSongs.filter { it.albumId == albumEntity.id }
        )
    }

}