package com.example.musicwithyou.domain.repository

import com.example.musicwithyou.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylist(id: Int): Playlist?

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun addSongsToPlaylist(songIds: List<Long>, playlist: Playlist)

    suspend fun addSongsToFavoritePlaylist(songIds: List<Long>)

    suspend fun deleteSongsFromFavoritePlaylist(songIds: List<Long>)

    suspend fun deleteSongsFromPlaylist(songIds: List<Long>, playlist: Playlist)

}