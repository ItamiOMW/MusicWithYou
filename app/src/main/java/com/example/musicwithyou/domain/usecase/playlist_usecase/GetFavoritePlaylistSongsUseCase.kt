package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.repository.PlaylistRepository
import javax.inject.Inject

class GetFavoritePlaylistSongsUseCase @Inject constructor(
    private val repository: PlaylistRepository
) {

    suspend operator fun invoke() = repository.getFavoritePlaylistSongs()

}