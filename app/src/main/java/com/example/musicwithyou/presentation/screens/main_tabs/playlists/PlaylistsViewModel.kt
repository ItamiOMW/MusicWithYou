package com.example.musicwithyou.presentation.screens.main_tabs.playlists

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.Playlist
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
                deletePlaylist(event.playlist)
            }
            is PlaylistsEvent.HideDeletePlaylistDialog -> {
                onDismissDeletePlaylistDialog()
            }
            is PlaylistsEvent.ShowDeletePlaylistDialog -> {
                onShowDeletePlaylistDialog(event.playlist)
            }
            is PlaylistsEvent.RenamePlaylist -> {
                renamePlaylist(event.playlist, event.newTitle)
            }
            is PlaylistsEvent.ShowRenamePlaylistDialog -> {
                onShowRenamePlaylistDialog(event.playlist)
            }
            is PlaylistsEvent.HideRenamePlaylistDialog -> {
                onDismissRenamePlaylistDialog()
            }
        }
    }


    private fun getPlaylists() {
        viewModelScope.launch {
            playlistUseCases.getPlaylists().collect { playlists ->
                state = state.copy(playlists = playlists)
            }
        }
    }

    private fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistUseCases.deletePlaylist(playlist)
            onDismissDeletePlaylistDialog()
        }
    }

    private fun renamePlaylist(
        playlist: Playlist,
        newTitle: String,
    ) {
        viewModelScope.launch {
            try {
                val newPlaylist = playlist.copy(title = newTitle)
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

    private fun onShowDeletePlaylistDialog(playlist: Playlist) {
        state = state.copy(showDeletePlaylistDialog = true, playlistToDelete = playlist)
    }

    private fun onDismissDeletePlaylistDialog() {
        state = state.copy(showDeletePlaylistDialog = false, playlistToDelete = null)
    }

    private fun onShowRenamePlaylistDialog(playlist: Playlist) {
        state = state.copy(showRenamePlaylistDialog = true, playlistToRename = playlist)
    }

    private fun onDismissRenamePlaylistDialog() {
        state = state.copy(showRenamePlaylistDialog = false, playlistToRename = null)
    }

}