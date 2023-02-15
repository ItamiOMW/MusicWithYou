package com.example.musicwithyou.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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


@Composable
fun CustomItem(
    iconId: Int,
    text: String,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(RoundedCornerShape(10.dp))
                .align(Alignment.CenterVertically)
        ) {
            AsyncImage(
                model = iconId,
                contentDescription = stringResource(R.string.item_icon_desc),
                modifier = Modifier.fillMaxSize(),
                error = painterResource(id = R.drawable.unknown_song)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }

}