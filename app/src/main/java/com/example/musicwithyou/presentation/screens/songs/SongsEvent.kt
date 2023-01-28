package com.example.musicwithyou.presentation.screens.songs

import com.example.musicwithyou.domain.models.PlayList
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.utils.SongOrder

sealed class SongsEvent {

    object RefreshSongs : SongsEvent()

    object PlayShuffledSongs : SongsEvent()

    data class PlaySong(val song: Song) : SongsEvent()

    data class NextSongToPlay(val song: Song) : SongsEvent()

    data class DeleteSong(val song: Song) : SongsEvent()

    data class AddToPlaylist(val song: Song, val playList: PlayList): SongsEvent()

    data class AddToQueue(val song: Song): SongsEvent()

    data class InstallSongAsRingtone(val song: Song) : SongsEvent()

    data class CreateNewPlaylist(val title: String, val song: Song?) : SongsEvent()

    data class OrderChange(val songOrder: SongOrder) : SongsEvent()

}