package com.example.musicwithyou.domain.repository

import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.models.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylist(id: Long): Flow<Playlist?>

    suspend fun moveSong(playlist: Playlist, from: Int, to: Int)

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun addSongsToPlaylist(songs: List<Song>, playlist: Playlist)

    suspend fun getFavoritePlaylistSongs(): Flow<List<Song>>

    suspend fun addSongsToFavoritePlaylist(songs: List<Song>)

    suspend fun deleteSongsFromFavoritePlaylist(songs: List<Song>)

    suspend fun deleteSongsFromPlaylist(songs: List<Song>, playlist: Playlist)

}