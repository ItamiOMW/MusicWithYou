package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.PlaylistPreview
import com.example.musicwithyou.domain.repository.PlaylistRepository
import com.example.musicwithyou.utils.InvalidTitleException
import javax.inject.Inject

class UpdatePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(playlistPreview: PlaylistPreview) {
        if (playlistPreview.title.isBlank() && playlistPreview.title.isEmpty()) {
            throw InvalidTitleException
        }
        repository.updatePlaylist(playlistPreview)
    }

}