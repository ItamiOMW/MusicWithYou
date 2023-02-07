package com.example.musicwithyou.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.utils.EMPTY_STRING


@Composable
fun BottomBarPlayer(
    song: Song?,
    isSongPlaying: Boolean,
    onPlayOrPause: () -> Unit,
    onSkipForward: () -> Unit,
    onSkipBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.align(Alignment.CenterVertically).weight(1f).padding(end = 5.dp)
        ) {
            Text(
                text = song?.title ?: EMPTY_STRING,
                style = MaterialTheme.typography.subtitle2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.primary,
            )
            Text(
                text = song?.artistName ?: EMPTY_STRING,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Clip,
                maxLines = 1,
                color = MaterialTheme.colors.secondaryVariant
            )
        }
        Row(
            modifier = Modifier.align(Alignment.CenterVertically).wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.skip_back),
                contentDescription = stringResource(R.string.skip_back_desc),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(20.dp)
                    .clickable {
                        onSkipBack()
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.skip_next),
                contentDescription = stringResource(R.string.skip_forward_desc),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(20.dp)
                    .clickable {
                        onSkipForward()
                    }
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.primary)
                    .clickable {
                        onPlayOrPause()
                    }
            ) {
                val drawablePlayOrStop = if (isSongPlaying) R.drawable.pause else R.drawable.play
                Icon(
                    painter = painterResource(id = drawablePlayOrStop),
                    contentDescription = stringResource(R.string.play_or_stop_desc),
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .padding(5.dp)
                        .align(Alignment.Center)
                        .fillMaxSize(0.5f)
                )
            }
        }
    }
}
