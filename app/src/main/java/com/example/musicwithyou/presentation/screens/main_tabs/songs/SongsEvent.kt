package com.example.musicwithyou.presentation.screens.main_tabs.songs

import com.example.musicwithyou.domain.models.PlayList
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.utils.SongOrder

sealed class SongsEvent {

    object RefreshSongs : SongsEvent()

    data class DeleteSong(val song: Song) : SongsEvent()

    data class AddToPlaylist(val song: Song, val playList: PlayList): SongsEvent()

    data class CreateNewPlaylist(val title: String, val song: Song?) : SongsEvent()

    data class OrderChange(val songOrder: SongOrder) : SongsEvent()

}