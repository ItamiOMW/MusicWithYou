package com.example.musicwithyou.presentation.screens.album_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwithyou.domain.models.Album
import com.example.musicwithyou.domain.usecase.album_usecase.AlbumUseCases
import com.example.musicwithyou.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumInfoViewModel @Inject constructor(
    private val albumsUseCases: AlbumUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {


    var album by mutableStateOf<Album?>(null)
        private set

    init {
        savedStateHandle.get<Long>(Screen.PLAYLIST_ID_ARG)?.let { id ->
            viewModelScope.launch {
                album = albumsUseCases.getAlbumById(id)
            }
        }
    }


}