package com.example.musicwithyou.domain.usecase.playlist_usecase

import com.example.musicwithyou.domain.models.PlaylistDetail
import com.example.musicwithyou.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPlaylistDetailUseCase @Inject constructor(
    private val repository: PlaylistRepository,
) {

    suspend operator fun invoke(id: Long): Flow<PlaylistDetail?> = repository.getPlaylistDetail(id)

}