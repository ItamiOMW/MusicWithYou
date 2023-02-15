package com.example.musicwithyou.presentation.screens.playlist_info

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.musicwithyou.R
import com.example.musicwithyou.presentation.components.AddToPlaylistSheetContent
import com.example.musicwithyou.presentation.components.SongActionsSheetContent
import com.example.musicwithyou.presentation.components.SongItem
import com.example.musicwithyou.presentation.components.drag_drop.ReorderableItem
import com.example.musicwithyou.presentation.components.drag_drop.detectReorderAfterLongPress
import com.example.musicwithyou.presentation.components.drag_drop.rememberReorderableLazyListState
import com.example.musicwithyou.presentation.components.drag_drop.reorderable
import com.example.musicwithyou.presentation.screens.MainViewModel
import com.example.musicwithyou.presentation.utils.ActionItem
import com.example.musicwithyou.utils.EMPTY_STRING
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlaylistInfoScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    playlistInfoViewModel: PlaylistInfoViewModel = hiltViewModel(),
) {

    val playlist = playlistInfoViewModel.playlist

    val reordableState = rememberReorderableLazyListState(onMove = { from, to ->
        playlistInfoViewModel.onEvent(PlaylistInfoEvent.MoveSong(from.index, to.index))
    })

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    val bottomSheetScope = rememberCoroutineScope()

    val sheetInitialContent: @Composable (() -> Unit) = { Text(EMPTY_STRING) }

    var customSheetContent by remember { mutableStateOf(sheetInitialContent) }

    if (playlist != null) {
        ModalBottomSheetLayout(
            sheetContent = { customSheetContent() },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
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
                        text = playlist.title,
                        style = MaterialTheme.typography.subtitle1,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                Row(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            mainViewModel.playShuffled(playlist.songs)
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
                        text = "${stringResource(R.string.shuffle)}(${playlist.songs.size})",
                        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondaryVariant),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                LazyColumn(
                    state = reordableState.listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .reorderable(reordableState)
                        .detectReorderAfterLongPress(reordableState)
                ) {
                    items(playlist.songs, key = { it.id }) { song ->
                        ReorderableItem(
                            reorderableState = reordableState,
                            key = song.id
                        ) { isDragging ->
                            val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp)
                            SongItem(
                                song = song,
                                isSongPlaying = mainViewModel.isSongPlaying.value,
                                isCurrentSong = song == mainViewModel.currentPlayingSong.value,
                                modifier = Modifier
                                    .padding(top = 10.dp, bottom = 10.dp)
                                    .fillMaxSize()
                                    .fillMaxHeight()
                                    .shadow(elevation.value)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colors.background)
                                    .clickable {
                                        mainViewModel.playSong(
                                            song = song,
                                            playlist.songs
                                        )
                                    },
                                onOptionsClicked = {
                                    bottomSheetScope.launch {
                                        customSheetContent = {
                                            SongActionsSheetContent(
                                                song = song,
                                                items = listOf(
                                                    ActionItem(
                                                        actionTitle = stringResource(R.string.add_to_playlist),
                                                        itemClicked = {
                                                            customSheetContent = {
                                                                AddToPlaylistSheetContent(
                                                                    modifier = Modifier
                                                                        .fillMaxHeight(0.5f),
                                                                    playlists = mainViewModel.playlists,
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
                                                                            it
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
                                                        actionTitle = stringResource(R.string.delete_from_playlist),
                                                        itemClicked = {
                                                            bottomSheetScope.launch {
                                                                playlistInfoViewModel.onEvent(
                                                                    PlaylistInfoEvent.DeleteSong(
                                                                        song
                                                                    )
                                                                )
                                                            }
                                                        },
                                                        iconId = R.drawable.delete
                                                    ),
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

}