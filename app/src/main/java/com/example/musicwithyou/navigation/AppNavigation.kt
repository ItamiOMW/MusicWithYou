package com.example.musicwithyou.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.musicwithyou.presentation.MainViewModel
import com.example.musicwithyou.presentation.screens.main_tabs.MainTabsScreen
import com.example.musicwithyou.presentation.screens.playing_now.PlayingNowScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController,
    mainViewModel: MainViewModel,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.MainTabsScreen.route
    ) {
        composable(
            Screen.MainTabsScreen.route
        ) {
            MainTabsScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(
            Screen.PlayingNowScreen.route
        ) {
            PlayingNowScreen(navController = navController, mainViewModel = mainViewModel)
        }
    }
}