package com.example.musicwithyou.domain.usecase.song_usecase

import com.example.musicwithyou.domain.utils.SongOrder
import javax.inject.Inject

class SongUseCases @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase,
    private val getSongsByIdsUseCase: GetSongsByIdsUseCase
) {

    suspend fun getSongs(
        songOrder: SongOrder
    ) = getSongsUseCase(songOrder)


    suspend fun getSongsByIds(
        songIds: List<Long>,
        songOrder: SongOrder,
    ) = getSongsByIdsUseCase.invoke(songIds, songOrder)
}