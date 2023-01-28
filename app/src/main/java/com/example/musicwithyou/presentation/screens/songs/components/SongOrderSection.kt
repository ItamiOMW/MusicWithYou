package com.example.musicwithyou.presentation.screens.songs.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.utils.OrderType
import com.example.musicwithyou.domain.utils.SongOrder
import com.example.musicwithyou.presentation.components.DefaultRadioButton


@Composable
fun SongOrderSection(
    modifier: Modifier = Modifier,
    songOrder: SongOrder,
    onOrderChange: (SongOrder) -> Unit,
    onCancel: () -> Unit,
) {
    var currentSongOrder by remember {
        mutableStateOf(songOrder)
    }

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.sort_by),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp, start = 10.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp)
        )
        Column() {
            DefaultRadioButton(
                text = stringResource(R.string.song_title),
                selected = currentSongOrder is SongOrder.Title,
                onSelect = {
                    currentSongOrder = SongOrder.Title(songOrder.orderType)
                },
                horizontalArrangement = Arrangement.SpaceBetween
            )
            DefaultRadioButton(
                text = stringResource(R.string.artist),
                selected = currentSongOrder is SongOrder.Artist,
                onSelect = {
                    currentSongOrder = SongOrder.Artist(songOrder.orderType)
                },
                horizontalArrangement = Arrangement.SpaceBetween
            )
            DefaultRadioButton(
                text = stringResource(R.string.album),
                selected = currentSongOrder is SongOrder.Album,
                onSelect = {
                    currentSongOrder = SongOrder.Album(currentSongOrder.orderType)
                },
                horizontalArrangement = Arrangement.SpaceBetween
            )
            DefaultRadioButton(
                text = stringResource(R.string.duration),
                selected = currentSongOrder is SongOrder.Duration,
                onSelect = {
                    currentSongOrder = SongOrder.Duration(currentSongOrder.orderType)
                },
                horizontalArrangement = Arrangement.SpaceBetween
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            DefaultRadioButton(
                text = stringResource(R.string.ascending),
                selected = currentSongOrder.orderType is OrderType.Ascending,
                onSelect = {
                    currentSongOrder = currentSongOrder.copy(orderType = OrderType.Ascending)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = stringResource(R.string.descending),
                selected = currentSongOrder.orderType is OrderType.Descending,
                onSelect = {
                    currentSongOrder = currentSongOrder.copy(orderType = OrderType.Descending)
                }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    onCancel()
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.body1
                )
            }
            Button(
                onClick = {
                    onOrderChange(currentSongOrder)
                }
            ) {
                Text(
                    text = stringResource(R.string.okay),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}