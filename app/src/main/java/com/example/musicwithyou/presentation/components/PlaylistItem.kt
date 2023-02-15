package com.example.musicwithyou.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.Playlist


@Composable
fun PlaylistItem(
    playlist: Playlist,
    onOptionsClicked: ((Playlist) -> Unit)?,
    modifier: Modifier = Modifier,
) {

    val songs = playlist.songs

    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.CenterVertically)
        ) {
            AsyncImage(
                model = playlist.iconId,
                contentDescription = stringResource(id = R.string.song_image_desc),
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.unknown_song)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(
                    text = playlist.title,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                Text(
                    text = "${songs.size} ${stringResource(id = R.string.songs)}",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            if (onOptionsClicked != null) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.options),
                        contentDescription = stringResource(R.string.options_desc),
                        tint = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(30.dp)
                            .padding(end = 5.dp)
                            .clip(CircleShape)
                            .clickable {
                                onOptionsClicked(playlist)
                            }
                    )
                }
            }
        }
    }
}

