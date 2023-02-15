package com.example.musicwithyou.data.repository

import com.example.musicwithyou.data.local.room.dao.PlaylistDao
import com.example.musicwithyou.data.local.room.models.SongPlaylistCrossRef
import com.example.musicwithyou.data.mapper.toPlaylist
import com.example.musicwithyou.data.mapper.toPlaylistEntity
import com.example.musicwithyou.data.mapper.toSong
import com.example.musicwithyou.data.mapper.toSongEntity
import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.repository.PlaylistRepository
import com.example.musicwithyou.utils.FAVORITE_PLAYLIST_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaylistRepositoryImpl @Inject constructor(
    private val playListDao: PlaylistDao,
) : PlaylistRepository {

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playListDao.getPlaylistsWithSongs().map { list ->
            list.map { playlistWithSongs ->
                val songs = playlistWithSongs.songs.map { songEntity ->
                    songEntity.toSong()
                }
                val playlistEntity = playlistWithSongs.playlistEntity
                playlistEntity.toPlaylist(songs = songs)
            }
        }
    }

    override suspend fun getPlaylist(id: Long): Playlist? {
        val playlistWithSongs = playListDao.getPlaylistWithSongsById(id) ?: return null
        val songs = playlistWithSongs.songs.map {
            it.toSong()
        }
        val playlistEntity = playlistWithSongs.playlistEntity
        return playlistEntity.toPlaylist(songs = songs)
    }

    override suspend fun moveSong(playlist: Playlist, from: Int, to: Int) {
        playListDao.move(playlist.id, from, to)
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        val playlistEntity = playlist.toPlaylistEntity()
        val songs = playlist.songs.map { song ->
            song.toSongEntity()
        }
        playListDao.createPlaylist(playlistEntity, songs)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        if (playlist.isDefault) {
            return
        }
        val playlistEntity = playlist.toPlaylistEntity()
        playListDao.updatePlaylist(playlistEntity)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        if (playlist.isDefault) {
            return
        }
        playListDao.deletePlaylist(playlist.id)
    }

    override suspend fun addSongsToPlaylist(songs: List<Song>, playlist: Playlist) {
        val playlistWithSongs = playListDao.getPlaylistWithSongsById(playlist.id) ?: return
        val refs = songs.map {
            SongPlaylistCrossRef(
                songId = it.id,
                playlistId = playlistWithSongs.playlistEntity.id,
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
        val favoritePlaylist = playListDao.getPlaylistWithSongsById(
            FAVORITE_PLAYLIST_ID
        )
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
        deleteSongsFromPlaylist(songs, playlist)
    }

    override suspend fun deleteSongsFromPlaylist(songs: List<Song>, playlist: Playlist) {
        playListDao.getPlaylistWithSongsById(playlist.id) ?: return
        songs.forEach {
            playListDao.deleteSongPlaylistRefById(playlist.id, it.id)
        }
    }

}