package com.example.musicwithyou.presentation.screens.main_tabs.playlists

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.musicwithyou.R
import com.example.musicwithyou.navigation.Screen
import com.example.musicwithyou.navigation.Screen.Companion.PLAYLIST_ID_ARG
import com.example.musicwithyou.presentation.components.*
import com.example.musicwithyou.presentation.screens.MainViewModel
import com.example.musicwithyou.presentation.utils.ActionItem
import com.example.musicwithyou.utils.EMPTY_STRING
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PlaylistsPagerScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    playlistsViewModel: PlaylistsViewModel = hiltViewModel(),
) {

    //States
    val playlists = playlistsViewModel.state.playlists

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    val bottomSheetScope = rememberCoroutineScope()

    val sheetInitialContent: @Composable (() -> Unit) = { Text(EMPTY_STRING) }

    var customSheetContent by remember { mutableStateOf(sheetInitialContent) }

    if (playlistsViewModel.state.showDeletePlaylistDialog) {
        playlistsViewModel.state.playlistToDelete?.let {
            DeletePlaylistDialog(
                playlist = it,
                onConfirm = {
                    playlistsViewModel.onEvent(PlaylistsEvent.DeletePlaylist(it))
                },
                onDismiss = {
                    playlistsViewModel.onEvent(PlaylistsEvent.HideDeletePlaylistDialog)
                }
            )
        }
    }

    if (playlistsViewModel.state.showRenamePlaylistDialog) {
        playlistsViewModel.state.playlistToRename?.let {
            RenamePlaylistDialog(
                playlist = it,
                onConfirm = { newTitle ->
                    playlistsViewModel.onEvent(PlaylistsEvent.RenamePlaylist(it, newTitle))
                },
                onDismiss = {
                    playlistsViewModel.onEvent(PlaylistsEvent.HideDeletePlaylistDialog)
                }
            )
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            customSheetContent()
        },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
           Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween
           ) {
               Text(
                   text = "${stringResource(R.string.playlists)}(${playlists.size})",
                   style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondaryVariant),
                   modifier = Modifier.align(Alignment.CenterVertically)
               )
               Icon(
                   painter = painterResource(
                       id = R.drawable.add
                   ),
                   contentDescription = stringResource(R.string.songs_picker_desc),
                   tint = MaterialTheme.colors.secondaryVariant,
                   modifier = Modifier
                       .size(25.dp)
                       .align(Alignment.CenterVertically)
                       .clickable {
                           mainViewModel.onShowCreatePlaylistDialog(
                               emptyList()
                           )
                       }
               )
           }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(playlists, key = { it.id }) { playlist ->
                    PlaylistItem(
                        playlist = playlist,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp)
                            .fillMaxSize()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Transparent)
                            .animateItemPlacement(animationSpec = tween(500))
                            .clickable {
                                navController.navigate(
                                    route = Screen.PlaylistInfoScreen.route +
                                            "?$PLAYLIST_ID_ARG=${playlist.id}",
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
                            },
                        onOptionsClicked = {
                            bottomSheetScope.launch {
                                customSheetContent = {
                                    PlaylistActionsSheetContent(
                                        playlist = playlist,
                                        actionItems = if (playlist.isDefault) {
                                            listOf(
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.play_next),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.playNext(playlist.songs)
                                                            bottomSheetState.hide()
                                                        }
                                                    },
                                                    iconId = R.drawable.play_next
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_queue),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.addToQueue(playlist.songs)
                                                            bottomSheetState.hide()
                                                        }

                                                    },
                                                    iconId = R.drawable.queue
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_playlist),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            customSheetContent = {
                                                                AddToPlaylistSheetContent(
                                                                    modifier = Modifier
                                                                        .fillMaxHeight(0.5f),
                                                                    playlists = playlists,
                                                                    onCreateNewPlaylist = {
                                                                        mainViewModel.onShowCreatePlaylistDialog(
                                                                            playlist.songs
                                                                        )
                                                                        bottomSheetScope.launch {
                                                                            bottomSheetState.hide()
                                                                        }
                                                                    },
                                                                    onPlaylistClick = {
                                                                        mainViewModel.addToPlaylist(
                                                                            playlist.songs,
                                                                            it
                                                                        )
                                                                        bottomSheetScope.launch {
                                                                            bottomSheetState.hide()
                                                                        }
                                                                    }
                                                                )
                                                            }
                                                            bottomSheetState.show()
                                                        }
                                                    },
                                                    iconId = R.drawable.add_to_playlist
                                                ),
                                            )
                                        } else {
                                            listOf(
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.play_next),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.playNext(playlist.songs)
                                                            bottomSheetState.hide()
                                                        }
                                                    },
                                                    iconId = R.drawable.play_next
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_queue),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.addToQueue(playlist.songs)
                                                            bottomSheetState.hide()
                                                        }

                                                    },
                                                    iconId = R.drawable.queue
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_playlist),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            customSheetContent = {
                                                                AddToPlaylistSheetContent(
                                                                    modifier = Modifier
                                                                        .fillMaxHeight(0.5f),
                                                                    playlists = playlists,
                                                                    onCreateNewPlaylist = {
                                                                        mainViewModel.onShowCreatePlaylistDialog(
                                                                            playlist.songs
                                                                        )
                                                                        bottomSheetScope.launch {
                                                                            bottomSheetState.hide()
                                                                        }
                                                                    },
                                                                    onPlaylistClick = {
                                                                        mainViewModel.addToPlaylist(
                                                                            playlist.songs,
                                                                            it
                                                                        )
                                                                        bottomSheetScope.launch {
                                                                            bottomSheetState.hide()
                                                                        }
                                                                    }
                                                                )
                                                            }
                                                            bottomSheetState.show()
                                                        }
                                                    },
                                                    iconId = R.drawable.add_to_playlist
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.rename),
                                                    itemClicked = {
                                                        playlistsViewModel.onEvent(
                                                            PlaylistsEvent.ShowRenamePlaylistDialog(
                                                                playlist
                                                            )
                                                        )
                                                    },
                                                    iconId = R.drawable.edit
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.delete),
                                                    itemClicked = {
                                                        playlistsViewModel.onEvent(
                                                            PlaylistsEvent.ShowDeletePlaylistDialog(
                                                                playlist
                                                            )
                                                        )
                                                    },
                                                    iconId = R.drawable.delete
                                                ),
                                            )
                                        }
                                    )
                                }
                                bottomSheetState.show()
                            }
                        }
                    )
                }
            }
        }
    }

}