package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.PlaylistDetail
import com.example.musicwithyou.domain.repository.PlaylistRepository
import com.example.musicwithyou.utils.InvalidTitleException
import javax.inject.Inject

class CreatePlaylistUseCase @Inject constructor(
    private val repository: PlaylistRepository,
) {

    suspend operator fun invoke(
        playlistDetail: PlaylistDetail,
    ) {
        if (playlistDetail.title.isBlank() && playlistDetail.title.isEmpty()) {
            throw InvalidTitleException
        }
        repository.createPlaylist(playlistDetail)
    }

}