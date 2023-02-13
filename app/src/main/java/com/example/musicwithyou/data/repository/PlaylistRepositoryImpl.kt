package com.example.musicwithyou.data.repository

import android.app.Application
import com.example.musicwithyou.R
import com.example.musicwithyou.data.local.room.dao.PlaylistDao
import com.example.musicwithyou.data.local.room.models.PlaylistEntity
import com.example.musicwithyou.data.mapper.toPlaylist
import com.example.musicwithyou.data.mapper.toPlaylistEntity
import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.repository.PlaylistRepository
import com.example.musicwithyou.utils.FAVORITE_PLAYLIST_ID
import com.example.musicwithyou.utils.InvalidTitleException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val playListDao: PlaylistDao,
    private val application: Application,
) : PlaylistRepository {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playListDao.getAllPlaylist().map { list ->
            list.map { playlist -> playlist.toPlaylist() }
        }
    }

    override suspend fun getPlaylist(id: Int): Playlist? {
        return playListDao.getPlaylistById(id)?.toPlaylist()
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        val playlistEntity = playlist.toPlaylistEntity()
        playListDao.insert(playlistEntity)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        if (playlist.isDefault) {
            return
        }
        if (playlist.title.isBlank() && playlist.title.isEmpty()) {
            throw InvalidTitleException
        }
        val playlistEntity = playlist.toPlaylistEntity()
        playListDao.update(playlistEntity)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        if (playlist.isDefault) {
            return
        }
        playListDao.delete(playlist.id)
    }

    override suspend fun addSongsToPlaylist(songIds: List<Long>, playlist: Playlist) {
        val newSongIds = playlist.songIds.toMutableList()
        newSongIds.removeAll(songIds)
        newSongIds.addAll(songIds)
        val updatedPlaylistEntity = playlist.copy(songIds = newSongIds).toPlaylistEntity()
        playListDao.update(updatedPlaylistEntity)
    }

    override suspend fun addSongsToFavoritePlaylist(songIds: List<Long>) {
        val favoritePlaylist = playListDao.getPlaylistById(
            FAVORITE_PLAYLIST_ID
        )
        if (favoritePlaylist == null) {
            playListDao.insert(
                PlaylistEntity(
                    id = FAVORITE_PLAYLIST_ID,
                    title = application.getString(R.string.favorites_playlist_title),
                    createdTimeStamp = System.currentTimeMillis(),
                    isDefault = false,
                    iconId = R.drawable.is_favorite,
                    songIds = songIds
                )
            )
        } else {
            addSongsToPlaylist(songIds, favoritePlaylist.toPlaylist())
        }

    }

    override suspend fun deleteSongsFromFavoritePlaylist(songIds: List<Long>) {
        val favoritePlaylist = playListDao.getPlaylistById(
            FAVORITE_PLAYLIST_ID
        ) ?: return
        deleteSongsFromPlaylist(songIds, favoritePlaylist.toPlaylist())
    }

    override suspend fun deleteSongsFromPlaylist(songIds: List<Long>, playlist: Playlist) {
        val newSongIds = playlist.songIds.toMutableList()
        newSongIds.removeAll(songIds)
        val updatedPlaylistEntity = playlist.copy(songIds = newSongIds).toPlaylistEntity()
        playListDao.update(updatedPlaylistEntity)
    }


}