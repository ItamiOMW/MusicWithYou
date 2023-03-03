package com.example.musicwithyou.presentation.screens.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicwithyou.R
import com.example.musicwithyou.presentation.ui.theme.MusicWithYouTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
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

                val systemUiController = rememberSystemUiController().apply {
                    setSystemBarsColor(MaterialTheme.colors.primary)
                }

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

                        val navController = rememberAnimatedNavController()

                        MainScreen(
                            mainViewModel = mainViewModel,
                            userSettingsManager = userSettingsManager,
                            navController = navController
                        )

                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
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


