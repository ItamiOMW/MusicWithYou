package com.example.musicwithyou.presentation.utils

import androidx.annotation.DrawableRes

data class NavigationItem(
    val title: String,
    val route: String,
    @DrawableRes
    val iconId: Int
)
