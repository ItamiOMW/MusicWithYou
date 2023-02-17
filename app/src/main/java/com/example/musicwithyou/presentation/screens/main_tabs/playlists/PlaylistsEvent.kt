package com.example.musicwithyou.presentation.screens.main_tabs.playlists

import com.example.musicwithyou.domain.models.PlaylistPreview

sealed class PlaylistsEvent {

    data class DeletePlaylist(val playlistPreview: PlaylistPreview) : PlaylistsEvent()

    data class RenamePlaylist(val playlistPreview: PlaylistPreview, val newTitle: String) : PlaylistsEvent()

    data class ShowDeletePlaylistDialog(val playlistPreview: PlaylistPreview) : PlaylistsEvent()

    object HideDeletePlaylistDialog : PlaylistsEvent()

    data class ShowRenamePlaylistDialog(val playlistPreview: PlaylistPreview) : PlaylistsEvent()

    object HideRenamePlaylistDialog : PlaylistsEvent()

}