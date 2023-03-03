package com.example.musicwithyou.navigation

sealed class Screen(protected val route: String, vararg params: String) {

    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    object MainTabsScreen : Screen(route = MAIN_TABS_SCREEN_ROUTE)

    object PlayingNowScreen : Screen(route = CURRENT_PLAYING_SONG_SCREEN_ROUTE)

    object CurrentQueueScreen : Screen(route = CURRENT_QUEUE_SCREEN_ROUTE)

    object PlaylistInfoScreen : Screen(
        route = PLAYLIST_INFO_SCREEN_ROUTE,
        PLAYLIST_ID_ARG
    ) {
        fun getRouteWithArgs(playlistId: Long): String = route.appendParams(
            PLAYLIST_ID_ARG to playlistId
        )

    }

    object AlbumInfoScreen : Screen(
        route = ALBUM_INFO_SCREEN_ROUTE,
        ALBUM_ID_ARG
    ) {
        fun getRouteWithArgs(albumId: Long): String = route.appendParams(
            ALBUM_ID_ARG to albumId
        )
    }

    object ArtistInfoScreen : Screen(
        route = ARTIST_INFO_SCREEN_ROUTE,
        ARTIST_ID_ARG
    ) {
        fun getRouteWithArgs(artistId: Long): String = route.appendParams(
            ARTIST_ID_ARG to artistId
        )
    }


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

    internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
        val builder = StringBuilder(this)

        params.forEach {
            it.second?.toString()?.let { arg ->
                builder.append("/$arg")
            }
        }

        return builder.toString()
    }

}