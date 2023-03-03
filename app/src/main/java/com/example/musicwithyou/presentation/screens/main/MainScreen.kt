package com.example.musicwithyou.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.musicwithyou.navigation.AppNavigation
import com.example.musicwithyou.navigation.Screen
import com.example.musicwithyou.presentation.components.BottomBarPlayer
import com.example.musicwithyou.presentation.components.CreatePlaylistDialog
import com.example.musicwithyou.presentation.components.CustomDrawer
import com.example.musicwithyou.presentation.components.CustomTopBar
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    userSettingsManager: UserSettingsManager,
    navController: NavHostController,
) {

    var topBarIsVisible by rememberSaveable { (mutableStateOf(true)) }

    var bottomBarIsVisible by rememberSaveable { (mutableStateOf(true)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val scaffoldState = rememberScaffoldState()

    val coroutineScope = rememberCoroutineScope()

    val isDarkTheme = userSettingsManager.isDarkTheme.collectAsState(initial = false)

    if (mainViewModel.showCreatePlaylistDialog.value) {
        CreatePlaylistDialog(
            onConfirm = { title ->
                mainViewModel.createPlaylist(title)
            },
            onDismiss = {
                mainViewModel.onDismissCreatePlaylistDialog()
            }
        )
    }

    when (navBackStackEntry?.destination?.route) {
        Screen.MainTabsScreen.fullRoute -> {
            bottomBarIsVisible = true
            topBarIsVisible = true
        }
        Screen.PlayingNowScreen.fullRoute -> {
            bottomBarIsVisible = false
            topBarIsVisible = false
        }
        Screen.CurrentQueueScreen.fullRoute -> {
            bottomBarIsVisible = false
            topBarIsVisible = false
        }
        else -> {
            bottomBarIsVisible = true
            topBarIsVisible = false
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            if (bottomBarIsVisible) {
                AnimatedVisibility(
                    visible = mainViewModel.currentPlayingSong.value != null,
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                ) {
                    BottomBarPlayer(
                        song = mainViewModel.currentPlayingSong.value,
                        isSongPlaying = mainViewModel.isSongPlaying.value,
                        onPlayOrPause = {
                            mainViewModel.currentPlayingSong.value?.let {
                                mainViewModel.playSong(
                                    it,
                                    mainViewModel.songQueue.value
                                )
                            }
                        },
                        onSkipForward = {
                            mainViewModel.skipToNext()
                        },
                        onSkipBack = {
                            mainViewModel.skipToPrevious()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(MaterialTheme.colors.secondary)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 17.dp,
                                    topEnd = 17.dp
                                )
                            )
                            .clickable {
                                navController.navigate(
                                    route = Screen.PlayingNowScreen.fullRoute,
                                ) {
                                    popUpTo(
                                        id = navBackStackEntry?.destination?.id
                                            ?: navController.graph.findStartDestination().id
                                    ) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            .padding(10.dp)
                    )
                }
            }
        },
        topBar = {
            CustomTopBar(
                isVisible = topBarIsVisible,
                onNavigationIconClicked = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                onSearchFieldClicked = {
                    //Todo launch search screen
                }
            )
        },
        drawerContent = {
            CustomDrawer(
                items = listOf(
                    //Navigation items
                ),
                onItemClick = { item ->
                    navController.navigate(
                        route = item.route,
                    ) {
                        popUpTo(
                            id = navController.currentBackStackEntry?.destination?.id
                                ?: navController.graph.findStartDestination().id
                        ) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                isDarkTheme = isDarkTheme.value,
                onDarkThemeSwitchCheckedChange = {
                    coroutineScope.launch {
                        userSettingsManager.setIsDarkTheme(!isDarkTheme.value)
                    }
                }
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            AppNavigation(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
    }

}