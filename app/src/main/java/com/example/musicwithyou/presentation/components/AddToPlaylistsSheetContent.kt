package com.example.musicwithyou.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.PlaylistPreview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddToPlaylistSheetContent(
    playlistPreviews: List<PlaylistPreview>,
    onCreateNewPlaylist: () -> Unit,
    onPlaylistClick: (PlaylistPreview) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.add_to_playlist),
            style = MaterialTheme.typography.subtitle1,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 15.dp, top = 15.dp, bottom = 5.dp)
        )
        Divider(modifier = Modifier.fillMaxWidth())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            item {
                CustomItem(
                    iconId = R.drawable.add,
                    text = stringResource(id = R.string.create_new_playlist),
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Transparent)
                        .animateItemPlacement(animationSpec = tween(500))
                        .clickable {
                            onCreateNewPlaylist()
                        },
                )
            }
            items(items = playlistPreviews) {
                PlaylistItem(
                    playlistPreview = it,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Transparent)
                        .animateItemPlacement(animationSpec = tween(500))
                        .clickable {
                            onPlaylistClick(it)
                        },
                    onOptionsClicked = null
                )
            }
        }
    }

}
