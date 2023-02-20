package com.example.musicwithyou.data.repository

import com.example.musicwithyou.data.local.content_resolver.ContentResolverHelper
import com.example.musicwithyou.data.local.room.dao.AlbumDao
import com.example.musicwithyou.data.local.room.dao.ArtistDao
import com.example.musicwithyou.data.local.room.dao.SongDao
import com.example.musicwithyou.data.local.room.models.ArtistEntity
import com.example.musicwithyou.data.mapper.toAlbumPreview
import com.example.musicwithyou.data.mapper.toArtistDetail
import com.example.musicwithyou.data.mapper.toArtistPreview
import com.example.musicwithyou.data.mapper.toSong
import com.example.musicwithyou.domain.models.ArtistDetail
import com.example.musicwithyou.domain.models.ArtistPreview
import com.example.musicwithyou.domain.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArtistRepositoryImpl @Inject constructor(
    private val songDao: SongDao,
    private val albumDao: AlbumDao,
    private val artistDao: ArtistDao,
    private val artistContentResolver: ContentResolverHelper<ArtistEntity>,
) : ArtistRepository {

    override suspend fun getArtistPreviews(): List<ArtistPreview> = withContext(Dispatchers.IO) {
        val artistsFromMedia = artistContentResolver.getData()
        val artistIdsFromMedia = artistsFromMedia.map { it.id }
        val cachedArtistsIds = artistDao.getArtists().map { it.id }
        val artistsToDelete = cachedArtistsIds.filter { !artistIdsFromMedia.contains(it) }
        artistsToDelete.forEach { artistDao.deleteById(it) }
        artistDao.insertAll(artistsFromMedia)
        artistDao.getArtists().map { artistEntity ->
            artistEntity.toArtistPreview()
        }
    }

    override suspend fun getArtistDetailById(id: Long): ArtistDetail {
        val songs = songDao.getSongsByArtistId(id).map { songEntity ->
            songEntity.toSong()
        }
        val albums = albumDao.getAlbumsByArtistId(id).map {
            it.toAlbumPreview()
        }
        return artistDao.getArtistById(id).toArtistDetail(
            songs = songs, albums = albums
        )
    }

}