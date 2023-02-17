package com.example.musicwithyou.presentation.screens.artist_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.domain.models.ArtistDetail
import com.example.musicwithyou.domain.usecase.artist_usecase.ArtistUseCases
import com.example.musicwithyou.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistInfoViewModel @Inject constructor(
    private val artistUseCases: ArtistUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {


    var artistDetail by mutableStateOf<ArtistDetail?>(null)
        private set

    init {
        savedStateHandle.get<Long>(Screen.ARTIST_ID_ARG)?.let { id ->
            viewModelScope.launch {
                artistDetail = artistUseCases.getArtistDetail(id)
            }
        }
    }


}