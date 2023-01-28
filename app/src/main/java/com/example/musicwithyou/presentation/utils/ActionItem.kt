package com.example.musicwithyou.presentation.utils

import androidx.annotation.DrawableRes

data class ActionItem(
    val actionTitle: String,
    val itemClicked: () -> Unit,
    @DrawableRes
    val iconId: Int
)
