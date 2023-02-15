package com.example.musicwithyou.presentation.screens.main_tabs.playlists

import com.example.musicwithyou.domain.models.Playlist

sealed class PlaylistsEvent {

    data class DeletePlaylist(val playlist: Playlist) : PlaylistsEvent()

    data class RenamePlaylist(val playlist: Playlist, val newTitle: String) : PlaylistsEvent()

    data class ShowDeletePlaylistDialog(val playlist: Playlist) : PlaylistsEvent()

    object HideDeletePlaylistDialog : PlaylistsEvent()

    data class ShowRenamePlaylistDialog(val playlist: Playlist) : PlaylistsEvent()

    object HideRenamePlaylistDialog : PlaylistsEvent()

}