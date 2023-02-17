package com.example.musicwithyou.presentation.screens.main_tabs.artists

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.musicwithyou.R
import com.example.musicwithyou.presentation.components.AddToPlaylistSheetContent
import com.example.musicwithyou.presentation.components.ArtistItem
import com.example.musicwithyou.presentation.components.ArtistsActionsSheetContent
import com.example.musicwithyou.presentation.screens.MainViewModel
import com.example.musicwithyou.presentation.utils.ActionItem
import com.example.musicwithyou.utils.EMPTY_STRING
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun ArtistsPagerScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    artistsViewModel: ArtistsViewModel = hiltViewModel(),
) {

    //States
    val artists = artistsViewModel.state.artists
    val isRefreshing = artistsViewModel.state.isRefreshing
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
                onRefresh = { artistsViewModel.onEvent(ArtistsEvent.RefreshArtist) }
            ) {
                LazyColumn() {
                    items(artists, key = { it.id }) { artist ->
                        ArtistItem(
                            artist = artist,
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 10.dp)
                                .fillMaxSize()
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Transparent)
                                .animateItemPlacement(animationSpec = tween(500))
                                .clickable {
                                    //Todo navigate to artist info
//                                    navController.navigate(
//                                        route = Screen.AlbumInfoScreen.route +
//                                                "?${Screen.ALBUM_ID_ARG}=${album.id}",
//                                    ) {
//                                        popUpTo(
//                                            id = navController.currentBackStackEntry?.destination?.id
//                                                ?: navController.graph.findStartDestination().id
//                                        ) {
//                                            saveState = true
//                                        }
//                                        launchSingleTop = true
//                                        restoreState = true
//                                    }
                                },
                            onOptionsClicked = {
                                bottomSheetScope.launch {
                                    customSheetContent = {
                                        ArtistsActionsSheetContent(
                                            artistPreview = artist,
                                            actionItems = listOf(
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.play_next),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.playNext(artist)
                                                            bottomSheetState.hide()
                                                        }
                                                    },
                                                    iconId = R.drawable.play_next
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_queue),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.addToQueue(artist)
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
                                                                            artist
                                                                        )
                                                                        bottomSheetScope.launch {
                                                                            bottomSheetState.hide()
                                                                        }
                                                                    },
                                                                    onPlaylistClick = {
                                                                        mainViewModel.addToPlaylist(
                                                                            artist,
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
                            },
                        )
                    }
                }
            }
        }
    }

}