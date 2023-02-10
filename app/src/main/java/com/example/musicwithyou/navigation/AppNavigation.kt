package com.example.musicwithyou.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.musicwithyou.presentation.MainViewModel
import com.example.musicwithyou.presentation.screens.current_queue.CurrentQueueScreen
import com.example.musicwithyou.presentation.screens.playing_now.PlayingNowScreen
import com.example.musicwithyou.presentation.screens.main_tabs.MainTabsScreen
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
            Screen.MainTabsScreen.route,
        ) {
            MainTabsScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(
            Screen.PlayingNowScreen.route,
            enterTransition = {
                slideInVertically(initialOffsetY = { it  }, animationSpec = tween(500))
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -it  }, animationSpec = tween(500))
            },
            popEnterTransition = {
                slideInVertically(initialOffsetY = { -it }, animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { it  }, animationSpec = tween(500))
            }
        ) {
            PlayingNowScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(
            Screen.CurrentQueueScreen.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it  }, animationSpec = tween(500))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it  }, animationSpec = tween(500))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(500))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { it  }, animationSpec = tween(500))
            }
        ) {
            CurrentQueueScreen(navController = navController, mainViewModel = mainViewModel)
        }
    }
}