package com.example.musicwithyou.domain.usecase.song_usecase

import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.repository.SongRepository
import com.example.musicwithyou.domain.utils.OrderType
import com.example.musicwithyou.domain.utils.SongOrder
import javax.inject.Inject

class GetSongsByIdsUseCase @Inject constructor(
    private val repository: SongRepository,
) {
    suspend operator fun invoke(
        songIds: List<Long>,
        songOrder: SongOrder = SongOrder.Title(OrderType.Ascending),
    ): List<Song> {
        val songs = repository.getSongsByIds(songIds)
        return when (songOrder.orderType) {
            is OrderType.Ascending -> {
                when (songOrder) {
                    is SongOrder.Artist -> songs.sortedBy { it.artistName.lowercase() }
                    is SongOrder.Album -> songs.sortedBy { it.albumName.lowercase() }
                    is SongOrder.Title -> songs.sortedBy { it.title.lowercase() }
                    is SongOrder.Duration -> songs.sortedBy { it.duration }
                }

            }
            is OrderType.Descending -> {
                when (songOrder) {
                    is SongOrder.Artist -> songs.sortedByDescending { it.artistName.lowercase() }
                    is SongOrder.Album -> songs.sortedByDescending { it.albumName.lowercase() }
                    is SongOrder.Title -> songs.sortedByDescending { it.title.lowercase() }
                    is SongOrder.Duration -> songs.sortedByDescending { it.duration }
                }
            }

        }
    }

}