package com.example.musicwithyou.presentation.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.musicwithyou.R
import com.example.musicwithyou.navigation.AppNavigation
import com.example.musicwithyou.navigation.Screen
import com.example.musicwithyou.presentation.components.BottomBarPlayer
import com.example.musicwithyou.presentation.components.CreatePlaylistDialog
import com.example.musicwithyou.presentation.components.CustomDrawer
import com.example.musicwithyou.presentation.components.CustomTopBar
import com.example.musicwithyou.presentation.ui.theme.MusicWithYouTheme
import com.example.musicwithyou.utils.UserSettingsManager
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userSettingsManager: UserSettingsManager

    @OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val isDarkTheme = userSettingsManager.isDarkTheme.collectAsState(initial = false)
            MusicWithYouTheme(darkTheme = isDarkTheme.value) {
                val permissionsState = rememberPermissionState(
                    permission = Manifest.permission.READ_EXTERNAL_STORAGE
                )

                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(
                    key1 = lifecycleOwner,
                    effect = {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_START) {
                                permissionsState.launchPermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)

                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }
                )

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (permissionsState.status.isGranted) {

                        val mainViewModel = viewModel(
                            modelClass = MainViewModel::class.java
                        )

                        val systemUiController = rememberSystemUiController().apply {
                            setSystemBarsColor(MaterialTheme.colors.primary)
                        }

                        val navController = rememberAnimatedNavController()

                        var topBarIsVisible by rememberSaveable { (mutableStateOf(true)) }

                        var bottomBarIsVisible by rememberSaveable { (mutableStateOf(true)) }

                        val navBackStackEntry by navController.currentBackStackEntryAsState()

                        val scaffoldState = rememberScaffoldState()

                        val drawerScope = rememberCoroutineScope()

                        if (mainViewModel.showCreatePlaylistDialog) {
                            CreatePlaylistDialog(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .border(1.dp, MaterialTheme.colors.secondaryVariant),
                                onConfirm = { title ->
                                    mainViewModel.createPlaylist(title)
                                },
                                onDismiss = {
                                    mainViewModel.onDismissCreatePlaylistDialog()
                                }
                            )
                        }

                        when (navBackStackEntry?.destination?.route) {
                            Screen.MainTabsScreen.route -> {
                                bottomBarIsVisible = true
                                topBarIsVisible = true
                            }
                            Screen.PlayingNowScreen.route -> {
                                bottomBarIsVisible = false
                                topBarIsVisible = false
                            }
                            Screen.CurrentQueueScreen.route -> {
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
                                                        route = Screen.PlayingNowScreen.route,
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
                                        drawerScope.launch {
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
                                        drawerScope.launch {
                                            scaffoldState.drawerState.close()
                                        }
                                    },
                                    isDarkTheme = isDarkTheme.value,
                                    onDarkThemeSwitchCheckedChange = {
                                        drawerScope.launch {
                                            userSettingsManager.setIsDarkTheme(!isDarkTheme.value)
                                        }
                                    }
                                )
                            },
                        ) {
                            Column() {
                                Box(modifier = Modifier.padding(it)) {
                                    AppNavigation(
                                        navController = navController,
                                        mainViewModel = mainViewModel
                                    )
                                }
                            }

                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column() {
                                Text(
                                    text = getString(R.string.app_name),
                                    style = MaterialTheme.typography.subtitle1
                                )
                                Text(
                                    text = getString(R.string.permission_was_denied),
                                    style = MaterialTheme.typography.subtitle2
                                )
                                Text(
                                    text = getString(R.string.to_listen_to_music_need),
                                    style = MaterialTheme.typography.body1
                                )
                                Text(
                                    text = getString(R.string.first_open_settings),
                                    style = MaterialTheme.typography.body1
                                )
                                Text(
                                    text = getString(R.string.second_open_permissions),
                                    style = MaterialTheme.typography.body1
                                )
                                Text(
                                    text = getString(R.string.third_turn_on_storage),
                                    style = MaterialTheme.typography.body1
                                )
                                Button(
                                    onClick = {
                                        val intent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                        intent.data =
                                            Uri.parse("package:" + applicationContext.packageName)
                                        startActivity(intent)
                                    },
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.secondaryVariant
                                    )
                                ) {
                                    Text(
                                        text = getString(R.string.open_settings),
                                        style = MaterialTheme.typography.body1,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


