package com.example.musicwithyou.navigation

sealed class Screen(val route: String) {

    object MainTabsScreen : Screen(MAIN_TABS_SCREEN_ROUTE)

    object PlayingNowScreen : Screen(CURRENT_PLAYING_SONG_SCREEN_ROUTE)

    object CurrentQueueScreen : Screen(CURRENT_QUEUE_SCREEN_ROUTE)

    object PlaylistInfoScreen : Screen(PLAYLIST_INFO_SCREEN_ROUTE)

    object AlbumInfoScreen : Screen(ALBUM_INFO_SCREEN_ROUTE)

    object ArtistInfoScreen : Screen(ARTIST_INFO_SCREEN_ROUTE)


    companion object {

        private const val MAIN_TABS_SCREEN_ROUTE = "tab_screen"

        private const val CURRENT_PLAYING_SONG_SCREEN_ROUTE = "playing_now"

        private const val CURRENT_QUEUE_SCREEN_ROUTE = "current_queue"

        private const val PLAYLIST_INFO_SCREEN_ROUTE = "playlist_info"

        private const val ALBUM_INFO_SCREEN_ROUTE = "album_info"

        private const val ARTIST_INFO_SCREEN_ROUTE = "artist_info"

        const val PLAYLIST_ID_ARG = "playlistId"

        const val ALBUM_ID_ARG = "playlistId"

        const val ARTIST_ID_ARG = "artistId"

    }

}