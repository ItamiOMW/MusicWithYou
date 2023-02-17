package com.example.musicwithyou.presentation.screens.main_tabs.albums

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.domain.usecase.album_usecase.AlbumUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val albumUseCases: AlbumUseCases,
) : ViewModel() {


    var state by mutableStateOf(AlbumsState())
        private set

    private var getAlbumsJob: Job? = null

    init {
        getAlbums()
    }

    fun onEvent(event: AlbumsEvent) {
        when(event) {
            is AlbumsEvent.RefreshAlbums -> {
                getAlbums()
            }
        }
    }

    private fun getAlbums() {
        getAlbumsJob?.cancel()
        getAlbumsJob = viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            val albums = albumUseCases.getAlbumPreviews()
            state = state.copy(albumPreviews = albums, isRefreshing = false)
        }
    }

}