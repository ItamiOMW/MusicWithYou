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
import com.example.musicwithyou.domain.models.ArtistPreview


@Composable
fun ArtistItem(
    artist: ArtistPreview,
    onOptionsClicked: (ArtistPreview) -> Unit,
    modifier: Modifier = Modifier,
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
                model = artist.imageUri,
                contentDescription = stringResource(id = R.string.song_image_desc),
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.artist)
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
                    text = artist.name,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                Text(
                    text = stringResource(
                        id = R.string.albums_and_song_count,
                        artist.albumCount,
                        artist.songsCount
                    ),
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Clip,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
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
                                onOptionsClicked(artist)
                            }
                    )
                }
            }
        }
    }
}