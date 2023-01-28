package com.example.musicwithyou.presentation.screens.songs

import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.utils.OrderType
import com.example.musicwithyou.domain.utils.SongOrder

data class SongsState(
    val songs: List<Song> = emptyList(),
    val songOrder: SongOrder = SongOrder.Title(OrderType.Ascending),
    val isRefreshing: Boolean = false,
)