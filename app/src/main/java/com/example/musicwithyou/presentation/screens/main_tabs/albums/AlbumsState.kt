package com.example.musicwithyou.presentation.screens.main_tabs.albums

import com.example.musicwithyou.domain.models.Album

data class AlbumsState(
    val albums: List<Album> = emptyList(),
    val isRefreshing: Boolean = false
)
