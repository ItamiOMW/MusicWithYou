package com.example.musicwithyou.presentation.screens.album_info

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.AsyncImage
import com.example.musicwithyou.R
import com.example.musicwithyou.navigation.Screen
import com.example.musicwithyou.presentation.components.AddToPlaylistSheetContent
import com.example.musicwithyou.presentation.components.SongActionsSheetContent
import com.example.musicwithyou.presentation.components.SongItem
import com.example.musicwithyou.presentation.screens.MainViewModel
import com.example.musicwithyou.presentation.utils.ActionItem
import com.example.musicwithyou.utils.EMPTY_STRING
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun AlbumInfoScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    albumInfoViewModel: AlbumInfoViewModel = hiltViewModel(),
) {

    val album = albumInfoViewModel.albumDetail

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    val bottomSheetScope = rememberCoroutineScope()

    val sheetInitialContent: @Composable (() -> Unit) = { Text(EMPTY_STRING) }

    var customSheetContent by remember { mutableStateOf(sheetInitialContent) }

    if (album != null) {
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
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = stringResource(R.string.close_playlist_info_desc),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(35.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                    Text(
                        text = stringResource(R.string.album),
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        AsyncImage(
                            model = album.imageUri,
                            contentDescription = stringResource(id = R.string.image_of_album_desc),
                            error = painterResource(id = R.drawable.album),
                            modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxSize(0.9f)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.album_info,
                                album.songsCount,
                                album.year
                            ),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.secondaryVariant
                        )
                        Text(
                            text = album.title,
                            style = MaterialTheme.typography.subtitle1,
                            color = MaterialTheme.colors.secondary,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = album.artistName,
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.secondaryVariant,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                navController.navigate(
                                    route = Screen.ArtistInfoScreen.route +
                                            "?${Screen.ARTIST_ID_ARG}=${album.artistId}",
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
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            mainViewModel.playShuffled(album.songs)
                        }
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.shuffle
                        ),
                        contentDescription = stringResource(R.string.shuffle_icon_desc),
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically)
                            .padding(end = 5.dp)
                    )
                    BasicText(
                        text = stringResource(R.string.shuffle, album.songs.size),
                        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondaryVariant),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(album.songs, key = { it.id }) { song ->
                        SongItem(
                            song = song,
                            isCurrentSong = song == mainViewModel.currentPlayingSong.value,
                            isSongPlaying = mainViewModel.isSongPlaying.value,
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 10.dp)
                                .fillMaxSize()
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Transparent)
                                .animateItemPlacement(animationSpec = tween(500))
                                .clickable {
                                    mainViewModel.playSong(
                                        song = song,
                                        album.songs
                                    )
                                },
                            onOptionsClicked = {
                                bottomSheetScope.launch {
                                    customSheetContent = {
                                        SongActionsSheetContent(
                                            song = song,
                                            items = listOf(
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.play_next),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.playNext(song)
                                                            bottomSheetState.hide()
                                                        }
                                                    },
                                                    iconId = R.drawable.play_next
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_queue),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.addToQueue(song)
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
                                                                            listOf(song)
                                                                        )
                                                                        bottomSheetScope.launch {
                                                                            bottomSheetState.hide()
                                                                        }
                                                                    },
                                                                    onPlaylistClick = {
                                                                        mainViewModel.addToPlaylist(
                                                                            listOf(song),
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
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.install_as_ringtone),
                                                    itemClicked = {
                                                        //Todo install song as ringtone
                                                    },
                                                    iconId = R.drawable.bell
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.delete_from_device),
                                                    itemClicked = {
                                                        //Delete from device
                                                    },
                                                    iconId = R.drawable.delete
                                                )
                                            )
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
}