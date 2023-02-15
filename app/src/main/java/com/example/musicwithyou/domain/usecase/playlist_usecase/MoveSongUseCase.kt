package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.repository.PlaylistRepository
import javax.inject.Inject

class MoveSongUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke(
        from: Int,
        to: Int,
        playlist: Playlist,
    ) = repository.moveSong(playlist, from, to)

}