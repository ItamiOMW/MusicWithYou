package com.example.musicwithyou.presentation.screens.main_tabs.playlists

import com.example.musicwithyou.domain.models.PlaylistPreview

data class PlaylistsState(
    val playlistPreviews: List<PlaylistPreview> = emptyList(),

    val showDeletePlaylistDialog: Boolean = false,
    val playlistToDelete: PlaylistPreview? = null,

    val showRenamePlaylistDialog: Boolean = false,
    val playlistToRename: PlaylistPreview? = null
)
