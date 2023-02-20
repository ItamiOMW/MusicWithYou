package com.example.musicwithyou.data.repository

import com.example.musicwithyou.data.local.content_resolver.ContentResolverHelper
import com.example.musicwithyou.data.local.room.dao.SongDao
import com.example.musicwithyou.data.local.room.models.SongEntity
import com.example.musicwithyou.data.mapper.toSong
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SongRepositoryImpl @Inject constructor(
    private val contentResolver: ContentResolverHelper<SongEntity>,
    private val songDao: SongDao,
) : SongRepository {

    override suspend fun getSongs(): List<Song> = withContext(Dispatchers.IO) {
        val cachedSongIds = songDao.getAllSongs().map { it.id }
        val songsFromMedia = contentResolver.getData()
        val songIdsFromMedia = songsFromMedia.map { it.id }
        val songsToDelete = cachedSongIds.filter { !songIdsFromMedia.contains(it) }
        songsToDelete.forEach { songDao.deleteById(it) }
        songDao.insertAll(songsFromMedia)
        songDao.getAllSongs().map { songEntity ->
            songEntity.toSong()
        }
    }

    override suspend fun getSongsByIds(songIds: List<Long>): List<Song> {
        return songDao.getAllSongs().filter { songEntity ->
            songIds.contains(songEntity.id)
        }.map { songEntity ->
            songEntity.toSong()
        }
    }

}