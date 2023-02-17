package com.example.musicwithyou.presentation.screens.main_tabs.artists

import com.example.musicwithyou.domain.models.ArtistPreview

data class ArtistsState(
    val artists: List<ArtistPreview> = emptyList(),
    val isRefreshing: Boolean = false,
)
