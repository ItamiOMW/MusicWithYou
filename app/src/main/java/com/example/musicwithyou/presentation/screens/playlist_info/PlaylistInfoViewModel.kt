package com.example.musicwithyou.presentation.screens.playlist_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.domain.models.Playlist
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.domain.usecase.playlist_usecase.PlaylistUseCases
import com.example.musicwithyou.navigation.Screen.Companion.PLAYLIST_ID_ARG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistInfoViewModel @Inject constructor(
    private val playlistUseCases: PlaylistUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var playlist by mutableStateOf<Playlist?>(null)
        private set

    init {
        savedStateHandle.get<Long>(PLAYLIST_ID_ARG)?.let { id ->
            viewModelScope.launch {
                playlistUseCases.getPlaylist(id).collect { playlistById ->
                    playlist = playlistById
                }
            }
        }
    }

    fun onEvent(event: PlaylistInfoEvent) {
        when (event) {
            is PlaylistInfoEvent.MoveSong -> {
                moveSongInPlaylist(event.from, event.to)
            }
            is PlaylistInfoEvent.DeleteSong -> {
                deleteSong(event.song)
            }
        }
    }

    private fun deleteSong(song: Song) {
        viewModelScope.launch {
            playlist?.let {
                playlistUseCases.deleteSongsFromPlaylist(
                    songs = listOf(song),
                    playlist = it
                )
            }
        }
    }


    private fun moveSongInPlaylist(from: Int, to: Int) {
        viewModelScope.launch {
            playlist?.let { playlistUseCases.moveSong(from, to, it) }
        }
    }

}