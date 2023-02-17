package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.PlaylistPreview
import com.example.musicwithyou.domain.repository.PlaylistRepository
import javax.inject.Inject

class DeletePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository,
) {

    suspend operator fun invoke(playlistPreview: PlaylistPreview) =
        repository.deletePlaylist(playlistPreview)

}