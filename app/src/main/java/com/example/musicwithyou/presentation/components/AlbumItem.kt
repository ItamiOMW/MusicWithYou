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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.Album


@Composable
fun AlbumItem(
    album: Album,
    modifier: Modifier = Modifier,
    onOptionsClicked: (Album) -> Unit,
    imageSize: Dp = 200.dp
) {

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(15.dp))
                .padding(30.dp),
        ) {
            AsyncImage(
                model = album.imageUri,
                contentDescription = stringResource(R.string.image_of_album_desc),
                error = painterResource(id = R.drawable.album),
                modifier = Modifier.fillMaxSize()
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            ) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.body1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                Text(
                    text = album.artistName,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
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
                            onOptionsClicked(album)
                        }
                )
            }
        }
    }

}