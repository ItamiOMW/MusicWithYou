package com.example.musicwithyou.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.musicwithyou.presentation.screens.albums.AlbumsScreen
import com.example.musicwithyou.presentation.screens.current_song.CurrentSongScreen
import com.example.musicwithyou.presentation.screens.playlists.PlaylistsScreen
import com.example.musicwithyou.presentation.screens.songs.SongsScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.SongsScreen.route
    ) {
        composable(
            Screen.SongsScreen.route
        ) {
            SongsScreen(navController = navController)
        }
        composable(
            Screen.PlaylistsScreen.route
        ) {
            PlaylistsScreen(navController = navController)
        }
        composable(
            Screen.AlbumsScreen.route
        ) {
            AlbumsScreen(navController = navController)
        }
        composable(
            Screen.CurrentSongScreen.route,
        ) {
            CurrentSongScreen(navController = navController)
        }
    }
}