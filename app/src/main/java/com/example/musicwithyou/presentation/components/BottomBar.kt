package com.example.musicwithyou.presentation.components


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.musicwithyou.R
import com.example.musicwithyou.domain.models.Song
import com.example.musicwithyou.presentation.utils.NavigationItem

@Composable
fun BottomNavBar(
    items: List<NavigationItem>,
    navController: NavController,
    isVisible: Boolean,
    currentSong: Song? = null,
    isCurrentSongPlaying: Boolean = false,
    onBottomBarPlayerClicked: () -> Unit,
    onPlayOrPause: () -> Unit,
    onSkipForward: () -> Unit,
    onSkipBack: () -> Unit,
) {

    val navStackBackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navStackBackEntry?.destination

    if (isVisible) {
        Column(
            modifier = Modifier.background(MaterialTheme.colors.background)
        ) {
            AnimatedVisibility(
                visible = currentSong != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                BottomBarPlayer(
                    song = currentSong,
                    isSongPlaying = isCurrentSongPlaying,
                    onPlayOrPause = {
                        onPlayOrPause()
                    },
                    onSkipForward = {
                        onSkipForward()
                    },
                    onSkipBack = {
                        onSkipBack()
                    },
                    modifier = Modifier
                        .padding(start = 3.dp, end = 3.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(topStart = 17.dp, topEnd = 17.dp))
                        .background(MaterialTheme.colors.secondary)
                        .clickable {
                            onBottomBarPlayerClicked()
                        }
                        .padding(10.dp)
                )
            }
            BottomAppBar(
                modifier = Modifier
                    .wrapContentHeight(unbounded = true)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                contentPadding = AppBarDefaults.ContentPadding,
                backgroundColor = MaterialTheme.colors.background,
                elevation = 10.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    items.forEach { item ->
                        AddItem(
                            item = item,
                            currentDestination = currentDestination,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RowScope.AddItem(
    item: NavigationItem,
    currentDestination: NavDestination?,
    navController: NavController,
) {
    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

    val contentColor = if (selected) MaterialTheme.colors.secondary
    else MaterialTheme.colors.secondaryVariant

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .clip(CircleShape)
            .background(Color.Transparent)
            .clickable(onClick = {
                navController.navigate(item.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            })
    ) {
        Column {
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = item.iconId),
                    contentDescription = stringResource(R.string.icon_desc),
                    tint = contentColor,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                )
                AnimatedVisibility(
                    visible = selected,
                ) {
                    Text(
                        text = item.title,
                        color = contentColor,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
