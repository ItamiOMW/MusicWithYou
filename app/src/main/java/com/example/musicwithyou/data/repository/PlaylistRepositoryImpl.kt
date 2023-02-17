package com.example.musicwithyou.data.repository

import com.example.musicwithyou.data.local.room.dao.PlaylistDao
import com.example.musicwithyou.data.local.room.models.SongPlaylistCrossRef
import com.example.musicwithyou.data.mapper.*
import com.example.musicwithyou.domain.models.PlaylistDetail
import com.example.musicwithyou.domain.models.PlaylistPreview
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.repository.PlaylistRepository
import com.example.musicwithyou.utils.FAVORITE_PLAYLIST_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val playListDao: PlaylistDao,
) : PlaylistRepository {

    override suspend fun getPlaylistPreviews(): Flow<List<PlaylistPreview>> {
        return playListDao.getPlaylistsWithSongs().map { list ->
            list.map { entity ->
                entity.playlistEntity.toPlaylistPreview(entity.songs.size)
            }
        }
    }

    override suspend fun getPlaylistDetail(id: Long): Flow<PlaylistDetail?> {
        return playListDao.getPlaylistWithSongsByIdFlow(id).map {
            val songs = it?.songs?.map { it.toSong() } ?: emptyList()
            it?.playlistEntity?.toPlaylist(songs)
        }
    }

    override suspend fun moveSong(playlistId: Long, from: Int, to: Int) {
        playListDao.move(playlistId, from, to)
    }

    override suspend fun createPlaylist(playlist: PlaylistDetail) {
        val playlistEntity = playlist.toPlaylistEntity()
        val songEntities = playlist.songs.map { song ->
            song.toSongEntity()
        }
        playListDao.createPlaylist(playlistEntity, songEntities)
    }

    override suspend fun updatePlaylist(playlistPreview: PlaylistPreview) {
        if (playlistPreview.isDefault) {
            return
        }
        val playlistEntity = playlistPreview.toPlaylistEntity()
        playListDao.updatePlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlist: PlaylistPreview) {
        if (playlist.isDefault) {
            return
        }
        playListDao.deletePlaylist(playlist.id)
    }

    override suspend fun addSongsToPlaylist(songs: List<Song>, playlistId: Long) {
        val playlistWithSongs = playListDao.getPlaylistWithSongsById(playlistId) ?: return
        var lastIndex = playlistWithSongs.songs.lastIndex
        val refs = songs.map {
            SongPlaylistCrossRef(
                songId = it.id,
                playlistId = playlistWithSongs.playlistEntity.id,
                position = ++lastIndex
            )
        }
        playListDao.insertSongPlaylistCrossRefs(refs)
    }

    override suspend fun getFavoritePlaylistSongs(): Flow<List<Song>> {
        return playListDao.getPlaylistWithSongsByIdFlow(FAVORITE_PLAYLIST_ID).map {
            it?.songs?.map { songEntity ->
                songEntity.toSong()
            } ?: emptyList()
        }

    }

    override suspend fun addSongsToFavoritePlaylist(songs: List<Song>) {
        val crossRef = songs.map {
            SongPlaylistCrossRef(
                it.id,
                FAVORITE_PLAYLIST_ID
            )
        }
        playListDao.insertSongPlaylistCrossRefs(crossRef)

    }

    override suspend fun deleteSongsFromFavoritePlaylist(songs: List<Song>) {
        val favoritePlaylistWithSongs = playListDao.getPlaylistWithSongsById(
            FAVORITE_PLAYLIST_ID
        ) ?: return
        val playlistSongs = favoritePlaylistWithSongs.songs.map { it.toSong() }
        val playlist = favoritePlaylistWithSongs.playlistEntity.toPlaylist(playlistSongs)
        deleteSongsFromPlaylist(songs, playlist.id)
    }

    override suspend fun deleteSongsFromPlaylist(songs: List<Song>, playlistId: Long) {
        playListDao.getPlaylistWithSongsById(playlistId) ?: return
        songs.forEach {
            playListDao.deleteSongPlaylistRefById(playlistId, it.id)
        }
    }

}