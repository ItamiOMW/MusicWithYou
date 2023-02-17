package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.PlaylistDetail
import com.example.musicwithyou.domain.models.PlaylistPreview
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
    private val getPlaylistPreviewsUseCase: GetPlaylistPreviewsUseCase,
    private val getPlaylistDetailUseCase: GetPlaylistDetailUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val getFavoritePlaylistSongsUseCase: GetFavoritePlaylistSongsUseCase,
    private val moveSongUseCase: MoveSongUseCase,
) {

    suspend fun getPlaylistPreviews(): Flow<List<PlaylistPreview>> =
        getPlaylistPreviewsUseCase.invoke()

    suspend fun getFavoritePlaylistSongs(): Flow<List<Song>> =
        getFavoritePlaylistSongsUseCase.invoke()

    suspend fun moveSong(
        from: Int,
        to: Int,
        playlistId: Long,
    ) = moveSongUseCase.invoke(from, to, playlistId)

    suspend fun getPlaylistDetail(
        playlistId: Long,
    ): Flow<PlaylistDetail?> = getPlaylistDetailUseCase.invoke(playlistId)


    suspend fun createPlaylist(
        playlistDetail: PlaylistDetail,
    ) = createPlaylistUseCase.invoke(playlistDetail)


    suspend fun updatePlaylist(
        playlistPreview: PlaylistPreview,
    ) = updatePlaylistUseCase.invoke(playlistPreview)


    suspend fun deletePlaylist(
        playlistPreview: PlaylistPreview,
    ) = deletePlaylistUseCase.invoke(playlistPreview)


    suspend fun addSongsToPlaylist(
        songs: List<Song>,
        playlistId: Long,
    ) = addSongsToPlaylistUseCase.invoke(songs, playlistId)


    suspend fun addSongsToFavoritePlaylist(
        songs: List<Song>,
    ) = addSongsToFavoritePlaylistUseCase.invoke(songs)


    suspend fun deleteSongsFromFavoritePlaylist(
        songs: List<Song>,
    ) = deleteSongsFromFavoritePlaylistUseCase.invoke(songs)


    suspend fun deleteSongsFromPlaylist(
        songs: List<Song>,
        playlistId: Long,
    ) = deleteSongsFromPlaylistUseCase.invoke(songs, playlistId)

}