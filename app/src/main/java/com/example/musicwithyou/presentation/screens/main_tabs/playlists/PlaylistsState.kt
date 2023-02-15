package com.example.musicwithyou.presentation.screens.main_tabs.playlists

import com.example.musicwithyou.domain.models.Playlist

data class PlaylistsState(
    val playlists: List<Playlist> = emptyList(),

    val showDeletePlaylistDialog: Boolean = false,
    val playlistToDelete: Playlist? = null,

    val showRenamePlaylistDialog: Boolean = false,
    val playlistToRename: Playlist? = null
)
