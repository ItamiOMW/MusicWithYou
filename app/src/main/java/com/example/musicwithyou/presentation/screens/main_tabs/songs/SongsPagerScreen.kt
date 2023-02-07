package com.example.musicwithyou.presentation.screens.main_tabs.songs

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicwithyou.R
import com.example.musicwithyou.presentation.MainViewModel
import com.example.musicwithyou.presentation.components.SongCard
import com.example.musicwithyou.presentation.screens.main_tabs.songs.components.SongActionsSheetContent
import com.example.musicwithyou.presentation.screens.main_tabs.songs.components.SongOrderSectionSheetContent
import com.example.musicwithyou.presentation.utils.ActionItem
import com.example.musicwithyou.utils.EMPTY_STRING
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongsPagerScreen(
    navController: NavController,
    songsViewModel: SongsViewModel,
    mainViewModel: MainViewModel,
) {

    //States
    val songs = songsViewModel.state.songs
    val songOrder = songsViewModel.state.songOrder
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = songsViewModel.state.isRefreshing
    )


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
        sheetState = bottomSheetState,
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
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            mainViewModel.playShuffled(songs)
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
                        text = stringResource(R.string.shuffle),
                        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondaryVariant),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
                Row() {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.sort
                        ),
                        contentDescription = stringResource(R.string.sort_song_icon_desc),
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                customSheetContent = {
                                    SongOrderSectionSheetContent(
                                        songOrder = songOrder,
                                        onOrderChange = { newOrder ->
                                            songsViewModel.onEvent(SongsEvent.OrderChange(newOrder))
                                            bottomSheetScope.launch {
                                                bottomSheetState.hide()
                                            }
                                        },
                                        onCancel = {
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
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(
                            id = R.drawable.picker
                        ),
                        contentDescription = stringResource(R.string.songs_picker_desc),
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                //Todo launch song picker screen
                            }
                    )
                }
            }
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    songsViewModel.onEvent(SongsEvent.RefreshSongs)
                }
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(songs) { song ->
                        SongCard(
                            song = song,
                            onSongClicked = {
                                mainViewModel.playSong(
                                    currentSong = it,
                                    songs
                                )
                            },
                            isSongPlaying = song == mainViewModel.currentPlayingSong.value,
                            onOptionsClicked = {
                                bottomSheetScope.launch {
                                    customSheetContent = {
                                        SongActionsSheetContent(
                                            song = song,
                                            items = listOf(
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.play_next),
                                                    itemClicked = {
                                                        //Todo add song to queue as next song to play
                                                    },
                                                    iconId = R.drawable.play_next
                                                ),
                                                ActionItem(
                                                    actionTitle = stringResource(R.string.add_to_queue),
                                                    itemClicked = {
                                                        //Todo add song to the end of current queue
                                                    },
                                                    iconId = R.drawable.queue
                                                ),
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
                                                    actionTitle = stringResource(R.string.delete_from_device),
                                                    itemClicked = {
                                                        songsViewModel.onEvent(
                                                            SongsEvent.DeleteSong(
                                                                song
                                                            )
                                                        )
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