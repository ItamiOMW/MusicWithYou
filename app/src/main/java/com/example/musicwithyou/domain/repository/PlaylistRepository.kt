package com.example.musicwithyou.domain.repository

import com.example.musicwithyou.domain.models.PlaylistDetail
import com.example.musicwithyou.domain.models.PlaylistPreview
import com.example.musicwithyou.domain.models.Song
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun getPlaylistPreviews(): Flow<List<PlaylistPreview>>

    suspend fun getPlaylistDetail(id: Long): Flow<PlaylistDetail?>

    suspend fun moveSong(playlistId: Long, from: Int, to: Int)

    suspend fun createPlaylist(playlist: PlaylistDetail)

    suspend fun updatePlaylist(playlistPreview: PlaylistPreview)

    suspend fun deletePlaylist(playlistPreview: PlaylistPreview)

    suspend fun addSongsToPlaylist(songs: List<Song>, playlistId: Long)

    suspend fun getFavoritePlaylistSongs(): Flow<List<Song>>

    suspend fun addSongsToFavoritePlaylist(songs: List<Song>)

    suspend fun deleteSongsFromFavoritePlaylist(songs: List<Song>)

    suspend fun deleteSongsFromPlaylist(songs: List<Song>, playlistId: Long)

}