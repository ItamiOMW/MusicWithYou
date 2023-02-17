package com.example.musicwithyou.presentation.screens.main_tabs.albums

import com.example.musicwithyou.domain.models.AlbumPreview

data class AlbumsState(
    val albumPreviews: List<AlbumPreview> = emptyList(),
    val isRefreshing: Boolean = false
)
