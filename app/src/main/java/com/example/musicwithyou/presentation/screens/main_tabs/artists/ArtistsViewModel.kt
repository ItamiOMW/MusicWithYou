package com.example.musicwithyou.presentation.screens.main_tabs.artists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.domain.usecase.artist_usecase.ArtistUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val artistUseCases: ArtistUseCases,
) : ViewModel() {


    var state by mutableStateOf(ArtistsState())
        private set

    private var getArtistsJob: Job? = null

    init {
        getArtists()
    }

    fun onEvent(event: ArtistsEvent) {
        when (event) {
            is ArtistsEvent.RefreshArtist -> {
                getArtists()
            }
        }
    }


    private fun getArtists() {
        getArtistsJob?.cancel()
        getArtistsJob = viewModelScope.launch {
            state = state.copy(isRefreshing = true)
            val artists = artistUseCases.getArtistPreviews()
            state = state.copy(artists = artists, isRefreshing = false)
        }
    }

}