package com.example.musicwithyou.presentation.screens.main_tabs.songs

import com.example.musicwithyou.domain.utils.SongOrder

sealed class SongsEvent {

    object RefreshSongs : SongsEvent()

    data class OrderChange(val songOrder: SongOrder) : SongsEvent()

}