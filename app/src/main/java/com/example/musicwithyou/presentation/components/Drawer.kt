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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.musicwithyou.R
import com.example.musicwithyou.presentation.utils.NavigationItem


@Composable
fun Drawer(
    items: List<NavigationItem>,
    onItemClick: (NavigationItem) -> Unit
) {
    Column {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, top = 60.dp, bottom = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.secondary
            )
        }
        Divider(modifier = Modifier.fillMaxWidth(), )
        LazyColumn() {
            items(items) {item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(item)
                        }
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = stringResource(id = R.string.icon_desc),
                        modifier = Modifier.size(23.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}