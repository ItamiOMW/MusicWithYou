package com.example.musicwithyou.presentation.screens.current_queue

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.musicwithyou.R
import com.example.musicwithyou.presentation.MainViewModel
import com.example.musicwithyou.presentation.components.SongActionsSheetContent
import com.example.musicwithyou.presentation.components.SongCard
import com.example.musicwithyou.presentation.components.drag_drop.ReorderableItem
import com.example.musicwithyou.presentation.components.drag_drop.detectReorderAfterLongPress
import com.example.musicwithyou.presentation.components.drag_drop.rememberReorderableLazyListState
import com.example.musicwithyou.presentation.components.drag_drop.reorderable
import com.example.musicwithyou.presentation.utils.ActionItem
import com.example.musicwithyou.utils.EMPTY_STRING
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CurrentQueueScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    currentQueueViewModel: CurrentQueueViewModel = hiltViewModel(),
) {
    val reordableState = rememberReorderableLazyListState(onMove = { from, to ->
        mainViewModel.moveSong(from.index, to.index)
    })

    val currentQueue = mainViewModel.songQueue.value

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )

    val coroutineScope = rememberCoroutineScope()

    val sheetInitialContent: @Composable (() -> Unit) = { Text(EMPTY_STRING) }

    var customSheetContent by remember { mutableStateOf(sheetInitialContent) }

    ModalBottomSheetLayout(
        sheetContent = {
            customSheetContent()
        },
        sheetState = modalBottomSheetState
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
                    contentDescription = stringResource(R.string.close_current_queue_desc),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(35.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
                Text(
                    text = stringResource(R.string.current_queue_title),
                    style = MaterialTheme.typography.subtitle2,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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
                        SongCard(
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
                                coroutineScope.launch {
                                    customSheetContent = {
                                        SongActionsSheetContent(
                                            song = song,
                                            items = listOf(
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_playlist),
                                                    itemClicked = {
                                                        //Todo Launch new sheet content with playlists
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
                                                        coroutineScope.launch {
                                                            mainViewModel.deleteFromQueue(song)
                                                            modalBottomSheetState.hide()
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
                                    modalBottomSheetState.show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}