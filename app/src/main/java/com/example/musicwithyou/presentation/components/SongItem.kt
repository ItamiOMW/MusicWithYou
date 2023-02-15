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
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.presentation.components.waveform.WaveformAnim
import com.example.musicwithyou.utils.timestampToDuration


@Composable
fun SongItem(
    song: Song,
    onOptionsClicked: (Song) -> Unit,
    modifier: Modifier = Modifier,
    isCurrentSong: Boolean = false,
    isSongPlaying: Boolean = false,
) {


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
                model = song.imageUri,
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
                    text = song.title,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                Row() {
                    Text(
                        text = song.artistName,
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Clip,
                        color = MaterialTheme.colors.secondaryVariant
                    )
                    Text(
                        text = stringResource(id = R.string.dot),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondaryVariant,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                    Text(
                        text = song.duration.timestampToDuration(),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Bottom)
                        .size(50.dp),
                ) {
                    if (isCurrentSong) {
                        WaveformAnim(
                            modifier = Modifier.fillMaxSize(),
                            barWidth = 3.dp,
                            gapWidth = 2.dp,
                            isAnimating = isSongPlaying,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
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
                                onOptionsClicked(song)
                            }
                    )
                }
            }
        }
    }
}