package com.example.musicwithyou.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp


fun Dp.toPxf(density: Density): Float = with(density) { this@toPxf.toPx() }

@Composable
fun Dp.toPxf(): Float = toPxf(LocalDensity.current)


fun lerpF(start: Float, stop: Float, fraction: Float): Float =
    (1 - fraction) * start + fraction * stop
