package com.example.musicwithyou.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.musicwithyou.navigation.Screen.Companion.ALBUM_ID_ARG
import com.example.musicwithyou.navigation.Screen.Companion.ARTIST_ID_ARG
import com.example.musicwithyou.navigation.Screen.Companion.PLAYLIST_ID_ARG
import com.example.musicwithyou.presentation.screens.MainViewModel
import com.example.musicwithyou.presentation.screens.album_info.AlbumInfoScreen
import com.example.musicwithyou.presentation.screens.artist_info.ArtistInfoScreen
import com.example.musicwithyou.presentation.screens.current_queue.CurrentQueueScreen
import com.example.musicwithyou.presentation.screens.main_tabs.MainTabsScreen
import com.example.musicwithyou.presentation.screens.playing_now.PlayingNowScreen
import com.example.musicwithyou.presentation.screens.playlist_info.PlaylistInfoScreen
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
                slideInVertically(initialOffsetY = { it }, animationSpec = tween(500))
                    .plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(500))
                    .plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInVertically(initialOffsetY = { -it }, animationSpec = tween(500))
                    .plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutVertically(targetOffsetY = { it }, animationSpec = tween(500))
                    .plus(fadeOut(tween(500)))
            }
        ) {
            PlayingNowScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(
            Screen.CurrentQueueScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            CurrentQueueScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(
            route = Screen.PlaylistInfoScreen.route + "?${PLAYLIST_ID_ARG}={$PLAYLIST_ID_ARG}",
            arguments = listOf(
                navArgument(PLAYLIST_ID_ARG) {
                    type = NavType.LongType
                    nullable = false
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            PlaylistInfoScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(
            route = Screen.AlbumInfoScreen.route + "?${ALBUM_ID_ARG}={$ALBUM_ID_ARG}",
            arguments = listOf(
                navArgument(ALBUM_ID_ARG) {
                    type = NavType.LongType
                    nullable = false
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            AlbumInfoScreen(navController = navController, mainViewModel = mainViewModel)
        }
        composable(
            route = Screen.ArtistInfoScreen.route + "?${ARTIST_ID_ARG}={$ARTIST_ID_ARG}",
            arguments = listOf(
                navArgument(ARTIST_ID_ARG) {
                    type = NavType.LongType
                    nullable = false
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it }, animationSpec = tween(500)
                ).plus(fadeIn(tween(500)))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it }, animationSpec = tween(500)
                ).plus(fadeOut(tween(500)))
            }
        ) {
            ArtistInfoScreen(navController = navController, mainViewModel = mainViewModel)
        }
    }
}