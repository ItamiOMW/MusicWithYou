package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.models.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class PlaylistUseCases @Inject constructor(
    private val addSongsToFavoritePlaylistUseCase: AddSongsToFavoritePlaylistUseCase,
    private val addSongsToPlaylistUseCase: AddSongsToPlaylistUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val deleteSongsFromFavoritePlaylistUseCase: DeleteSongsFromFavoritePlaylistUseCase,
    private val deleteSongsFromPlaylistUseCase: DeleteSongsFromPlaylistUseCase,
    private val getPlaylistsUseCase: GetPlaylistsUseCase,
    private val getPlaylistUseCase: GetPlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val getFavoritePlaylistSongsUseCase: GetFavoritePlaylistSongsUseCase,
) {

    suspend fun getPlaylists(): Flow<List<Playlist>> = getPlaylistsUseCase.invoke()

    suspend fun getFavoritePlaylistSongs(): Flow<List<Song>> =
        getFavoritePlaylistSongsUseCase.invoke()


    suspend fun getPlaylist(
        id: Long,
    ): Playlist? = getPlaylistUseCase.invoke(id)


    suspend fun createPlaylist(
        playlist: Playlist,
    ) = createPlaylistUseCase.invoke(playlist)


    suspend fun updatePlaylist(
        playlist: Playlist,
    ) = updatePlaylistUseCase.invoke(playlist)


    suspend fun deletePlaylist(
        playlist: Playlist,
    ) = deletePlaylistUseCase.invoke(playlist)


    suspend fun addSongsToPlaylist(
        songs: List<Song>,
        playlist: Playlist,
    ) = addSongsToPlaylistUseCase.invoke(songs, playlist)


    suspend fun addSongsToFavoritePlaylist(
        songs: List<Song>,
    ) = addSongsToFavoritePlaylistUseCase.invoke(songs)


    suspend fun deleteSongsFromFavoritePlaylist(
        songs: List<Song>,
    ) = deleteSongsFromFavoritePlaylistUseCase.invoke(songs)


    suspend fun deleteSongsFromPlaylist(
        songs: List<Song>,
        playlist: Playlist,
    ) = deleteSongsFromPlaylistUseCase.invoke(songs, playlist)

}