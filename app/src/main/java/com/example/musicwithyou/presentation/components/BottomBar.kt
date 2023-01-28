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
    isCurrentSongVisible: Boolean,
    currentSong: Song? = null
) {

    val navStackBackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navStackBackEntry?.destination

    if (isVisible) {
        BottomAppBar(
            modifier = Modifier,
            backgroundColor = MaterialTheme.colors.secondary,
            contentPadding = AppBarDefaults.ContentPadding,
            cutoutShape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
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

@Composable
fun RowScope.AddItem(
    item: NavigationItem,
    currentDestination: NavDestination?,
    navController: NavController,
) {
    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

    val background = if (selected) MaterialTheme.colors.secondary else Color.Transparent

    val contentColor = if (selected) MaterialTheme.colors.primary
    else MaterialTheme.colors.secondaryVariant

    Box(
        modifier = Modifier
            .wrapContentHeight()
            .clip(CircleShape)
            .background(background)
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
