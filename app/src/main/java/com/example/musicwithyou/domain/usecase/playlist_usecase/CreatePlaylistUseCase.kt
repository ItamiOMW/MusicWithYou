package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.repository.PlaylistRepository
import com.example.musicwithyou.utils.InvalidTitleException
import javax.inject.Inject

class CreatePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(playlist: Playlist) {
        if (playlist.title.isBlank() && playlist.title.isEmpty()) {
            throw InvalidTitleException
        }
        repository.createPlaylist(playlist)
    }

}