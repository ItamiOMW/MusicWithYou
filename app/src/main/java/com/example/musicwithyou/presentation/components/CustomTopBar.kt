package com.example.musicwithyou.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.musicwithyou.R


@Composable
fun CustomTopBar(
    isVisible: Boolean,
    onNavigationIconClicked: () -> Unit,
    onSearchFieldClicked: () -> Unit,
) {
    if (isVisible) {

        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            elevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(10.dp, RoundedCornerShape(bottomEnd = 18.dp, bottomStart = 18.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp),
            ) {
                IconButton(
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.CenterVertically),
                    onClick = {
                        onNavigationIconClicked()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.navigation),
                        contentDescription = stringResource(R.string.drawer_desc),
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .size(26.dp)
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.CenterVertically)
                        .clip(RoundedCornerShape(13.dp))
                        .background(MaterialTheme.colors.surface)
                        .clickable {
                            onSearchFieldClicked()
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = stringResource(R.string.search_desc),
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(26.dp)
                            .align(Alignment.CenterEnd)
                            .clip(CircleShape)
                    )
                }
            }
        }

    }
}