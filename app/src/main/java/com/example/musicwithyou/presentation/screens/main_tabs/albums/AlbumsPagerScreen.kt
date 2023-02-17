package com.example.musicwithyou.presentation.screens.main_tabs.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.musicwithyou.R
import com.example.musicwithyou.navigation.Screen
import com.example.musicwithyou.presentation.components.AddToPlaylistSheetContent
import com.example.musicwithyou.presentation.components.AlbumActionsSheetContent
import com.example.musicwithyou.presentation.components.AlbumItem
import com.example.musicwithyou.presentation.screens.MainViewModel
import com.example.musicwithyou.presentation.utils.ActionItem
import com.example.musicwithyou.utils.EMPTY_STRING
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumsPagerScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    albumsViewModel: AlbumsViewModel = hiltViewModel(),
) {

    //States
    val albums = albumsViewModel.state.albumPreviews
    val isRefreshing = albumsViewModel.state.isRefreshing
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    val bottomSheetScope = rememberCoroutineScope()

    val sheetInitialContent: @Composable (() -> Unit) = { Text(EMPTY_STRING) }

    var customSheetContent by remember { mutableStateOf(sheetInitialContent) }

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
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    albumsViewModel.onEvent(AlbumsEvent.RefreshAlbums)
                }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(albums, key = { it.id }) { album ->
                        AlbumItem(
                            albumPreview = album,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp)
                                .background(
                                    MaterialTheme.colors.secondaryVariant.copy(alpha = 0.3f),
                                    RoundedCornerShape(15.dp)
                                )
                                .clickable {
                                    navController.navigate(
                                        route = Screen.AlbumInfoScreen.route +
                                                "?${Screen.ALBUM_ID_ARG}=${album.id}",
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
                                }
                                .padding(10.dp),
                            imageSize = 150.dp,
                            onOptionsClicked = {
                                bottomSheetScope.launch {
                                    customSheetContent = {
                                        AlbumActionsSheetContent(
                                            albumPreview = album,
                                            actionItems = listOf(
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.play_next),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.playNext(album)
                                                            bottomSheetState.hide()
                                                        }
                                                    },
                                                    iconId = R.drawable.play_next
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_queue),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.addToQueue(album)
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
                                                                    playlistPreviews = mainViewModel.playlistPreviews,
                                                                    onCreateNewPlaylist = {
                                                                        mainViewModel.onShowCreatePlaylistDialog(
                                                                            album
                                                                        )
                                                                        bottomSheetScope.launch {
                                                                            bottomSheetState.hide()
                                                                        }
                                                                    },
                                                                    onPlaylistClick = {
                                                                        mainViewModel.addToPlaylist(
                                                                            album,
                                                                            it.id
                                                                        )
                                                                        bottomSheetScope.launch {
                                                                            bottomSheetState.hide()
                                                                        }
                                                                    }
                                                                )
                                                            }
                                                            bottomSheetScope.launch {
                                                                bottomSheetState.show()
                                                            }
                                                        }
                                                    },
                                                    iconId = R.drawable.add_to_playlist
                                                ),
                                            )
                                        )
                                    }
                                    bottomSheetState.show()
                                }


                            })
                    }
                }
            }

        }
    }
}