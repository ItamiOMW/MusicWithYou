package com.example.musicwithyou.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.AlbumPreview
import com.example.musicwithyou.presentation.utils.ActionItem

@Composable
fun AlbumActionsSheetContent(
    albumPreview: AlbumPreview,
    actionItems: List<ActionItem>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = albumPreview.title,
            style = MaterialTheme.typography.subtitle1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, start = 10.dp, end = 10.dp)
        )
        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp))
        LazyColumn() {
            items(items = actionItems) { actionItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            actionItem.itemClicked()
                        }
                        .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = actionItem.iconId),
                        contentDescription = stringResource(id = R.string.icon_desc),
                        modifier = Modifier.size(23.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = actionItem.actionTitle,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }

}