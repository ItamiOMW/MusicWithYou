package com.example.musicwithyou.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    navHostController: NavHostController
) {

    AnimatedNavHost(
        navController = navHostController,
        startDestination = "" //Todo add Screens
    ) {

    }
}