package com.example.musicwithyou.presentation.screens.playlist_info

import com.example.musicwithyou.domain.models.Song

sealed class PlaylistInfoEvent {

    data class MoveSong(val from: Int, val to: Int): PlaylistInfoEvent()

    data class DeleteSong(val song: Song): PlaylistInfoEvent()

}