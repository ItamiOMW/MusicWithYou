package com.example.musicwithyou.presentation.screens.current_queue

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicwithyou.R
import com.example.musicwithyou.presentation.components.AddToPlaylistSheetContent
import com.example.musicwithyou.presentation.components.SongActionsSheetContent
import com.example.musicwithyou.presentation.components.SongItem
import com.example.musicwithyou.presentation.components.drag_drop.ReorderableItem
import com.example.musicwithyou.presentation.components.drag_drop.detectReorderAfterLongPress
import com.example.musicwithyou.presentation.components.drag_drop.rememberReorderableLazyListState
import com.example.musicwithyou.presentation.components.drag_drop.reorderable
import com.example.musicwithyou.presentation.screens.main.MainViewModel
import com.example.musicwithyou.presentation.utils.ActionItem
import com.example.musicwithyou.utils.EMPTY_STRING
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrentQueueScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    val reordableState = rememberReorderableLazyListState(onMove = { from, to ->
        mainViewModel.moveSong(from.index, to.index)
    })

    val currentQueue = mainViewModel.songQueue.value

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
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
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(35.dp),
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left),
                        contentDescription = stringResource(R.string.close_current_queue_desc),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(35.dp)
                    )
                }
                Text(
                    text = stringResource(R.string.current_queue_title),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                state = reordableState.listState,
                modifier = Modifier
                    .fillMaxSize()
                    .reorderable(reordableState)
                    .detectReorderAfterLongPress(reordableState)
            ) {
                items(currentQueue, { it.id }) { song ->
                    ReorderableItem(
                        reorderableState = reordableState,
                        key = song.id,
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
                                        currentQueue
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
                                                                playlistPreviews = mainViewModel.playlistPreviews.value,
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
                                                    actionTitle = stringResource(R.string.delete_from_queue),
                                                    itemClicked = {
                                                        bottomSheetScope.launch {
                                                            mainViewModel.deleteFromQueue(song)
                                                            bottomSheetState.hide()
                                                        }
                                                    },
                                                    iconId = R.drawable.remove
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.delete_from_device),
                                                    itemClicked = {
                                                        //Todo delete from device
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