package com.example.musicwithyou.presentation.screens.main_tabs.playlists

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.PlaylistPreview
import com.example.musicwithyou.domain.usecase.playlist_usecase.PlaylistUseCases
import com.example.musicwithyou.utils.InvalidTitleException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaylistsViewModel @Inject constructor(
    private val playlistUseCases: PlaylistUseCases,
    private val application: Application,
) : ViewModel() {


    var state by mutableStateOf(PlaylistsState())
        private set

    init {
        getPlaylists()
    }

    fun onEvent(event: PlaylistsEvent) {
        when (event) {
            is PlaylistsEvent.DeletePlaylist -> {
                deletePlaylist(event.playlistPreview)
            }
            is PlaylistsEvent.HideDeletePlaylistDialog -> {
                onDismissDeletePlaylistDialog()
            }
            is PlaylistsEvent.ShowDeletePlaylistDialog -> {
                onShowDeletePlaylistDialog(event.playlistPreview)
            }
            is PlaylistsEvent.RenamePlaylist -> {
                renamePlaylist(event.playlistPreview, event.newTitle)
            }
            is PlaylistsEvent.ShowRenamePlaylistDialog -> {
                onShowRenamePlaylistDialog(event.playlistPreview)
            }
            is PlaylistsEvent.HideRenamePlaylistDialog -> {
                onDismissRenamePlaylistDialog()
            }
        }
    }


    private fun getPlaylists() {
        viewModelScope.launch {
            playlistUseCases.getPlaylistPreviews().collect { playlists ->
                state = state.copy(playlistPreviews = playlists)
            }
        }
    }

    private fun deletePlaylist(playlistPreview: PlaylistPreview) {
        viewModelScope.launch {
            playlistUseCases.deletePlaylist(playlistPreview)
            onDismissDeletePlaylistDialog()
        }
    }

    private fun renamePlaylist(
        playlistPreview: PlaylistPreview,
        newTitle: String,
    ) {
        viewModelScope.launch {
            try {
                val newPlaylist = playlistPreview.copy(title = newTitle)
                playlistUseCases.updatePlaylist(newPlaylist)
                onDismissRenamePlaylistDialog()
            } catch (e: InvalidTitleException) {
                Toast.makeText(
                    application,
                    application.getText(R.string.title_is_empty),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onShowDeletePlaylistDialog(playlistPreview: PlaylistPreview) {
        state = state.copy(showDeletePlaylistDialog = true, playlistToDelete = playlistPreview)
    }

    private fun onDismissDeletePlaylistDialog() {
        state = state.copy(showDeletePlaylistDialog = false, playlistToDelete = null)
    }

    private fun onShowRenamePlaylistDialog(playlistPreview: PlaylistPreview) {
        state = state.copy(showRenamePlaylistDialog = true, playlistToRename = playlistPreview)
    }

    private fun onDismissRenamePlaylistDialog() {
        state = state.copy(showRenamePlaylistDialog = false, playlistToRename = null)
    }

}