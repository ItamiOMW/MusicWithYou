package com.example.musicwithyou.presentation.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable



data class TabItem(
    val title: String,
    @DrawableRes
    val iconId: Int,
    val screen: @Composable () -> Unit,
)