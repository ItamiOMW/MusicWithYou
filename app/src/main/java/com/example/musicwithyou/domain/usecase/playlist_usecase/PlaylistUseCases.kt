package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.Playlist
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
) {

    fun getPlaylists(): Flow<List<Playlist>> = getPlaylistsUseCase.invoke()


    suspend fun getPlaylist(
        id: Int,
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
        songIds: List<Long>,
        playlist: Playlist,
    ) = addSongsToPlaylistUseCase.invoke(songIds, playlist)


    suspend fun addSongsToFavoritePlaylist(
        songIds: List<Long>,
    ) = addSongsToFavoritePlaylistUseCase.invoke(songIds)


    suspend fun deleteSongsFromFavoritePlaylist(
        songIds: List<Long>,
    ) = deleteSongsFromFavoritePlaylistUseCase.invoke(songIds)


    suspend fun deleteSongsFromPlaylist(
        songIds: List<Long>,
        playlist: Playlist,
    ) = deleteSongsFromPlaylistUseCase.invoke(songIds, playlist)

}