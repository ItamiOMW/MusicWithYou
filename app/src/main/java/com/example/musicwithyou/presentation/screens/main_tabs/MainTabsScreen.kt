package com.example.musicwithyou.presentation.screens.main_tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.musicwithyou.R
import com.example.musicwithyou.presentation.screens.MainViewModel
import com.example.musicwithyou.presentation.screens.main_tabs.albums.AlbumsPagerScreen
import com.example.musicwithyou.presentation.screens.main_tabs.albums.AlbumsViewModel
import com.example.musicwithyou.presentation.screens.main_tabs.artists.ArtistsPagerScreen
import com.example.musicwithyou.presentation.screens.main_tabs.artists.ArtistsViewModel
import com.example.musicwithyou.presentation.screens.main_tabs.playlists.PlaylistsPagerScreen
import com.example.musicwithyou.presentation.screens.main_tabs.playlists.PlaylistsViewModel
import com.example.musicwithyou.presentation.screens.main_tabs.songs.SongsPagerScreen
import com.example.musicwithyou.presentation.screens.main_tabs.songs.SongsViewModel
import com.example.musicwithyou.presentation.utils.TabItem
import com.google.accompanist.pager.*
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class, ExperimentalSnapperApi::class)
@Composable
fun MainTabsScreen(
    navController: NavController,
    mainViewModel: MainViewModel,
    songsViewModel: SongsViewModel = hiltViewModel(),
    playlistsViewModel: PlaylistsViewModel = hiltViewModel(),
    albumsViewModel: AlbumsViewModel = hiltViewModel(),
    artistsViewModel: ArtistsViewModel = hiltViewModel(),
) {

    val mainTabs = listOf(
        TabItem(
            title = stringResource(id = R.string.songs),
            iconId = R.drawable.music,
            screen = {
                SongsPagerScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    songsViewModel = songsViewModel
                )
            }
        ),
        TabItem(
            title = stringResource(id = R.string.playlists),
            iconId = R.drawable.playlist,
            screen = {
                PlaylistsPagerScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    playlistsViewModel = playlistsViewModel
                )
            }
        ),
        TabItem(
            title = stringResource(id = R.string.albums),
            iconId = R.drawable.album,
            screen = {
                AlbumsPagerScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    albumsViewModel = albumsViewModel
                )
            }
        ),
        TabItem(
            title = stringResource(id = R.string.artists),
            iconId = R.drawable.artist,
            screen = {
                ArtistsPagerScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    artistsViewModel = artistsViewModel
                )
            }
        ),
    )

    val pagerState = rememberPagerState()

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                    color = MaterialTheme.colors.secondary
                )

            },
        ) {
            mainTabs.forEachIndexed { index, tabItem ->

                Tab(
                    modifier = Modifier.wrapContentHeight(),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            tabItem.title,
                            style = MaterialTheme.typography.body2
                        )

                    },
                    icon = {
                        Icon(
                            painterResource(id = tabItem.iconId),
                            contentDescription = stringResource(
                                R.string.tab_icon_desc
                            ),
                            modifier = Modifier.size(20.dp),
                        )
                    }
                )
            }
        }
        HorizontalPager(
            count = mainTabs.size,
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
            modifier = Modifier.fillMaxSize()
        ) { tabIndex ->
            mainTabs[tabIndex].screen()
        }
    }
}
