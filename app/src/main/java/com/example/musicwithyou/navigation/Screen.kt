package com.example.musicwithyou.navigation

sealed class Screen(val route: String) {

    object SongsScreen : Screen(SONGS_SCREEN_ROUTE)

    object PlaylistsScreen : Screen(PLAYLISTS_SCREEN_ROUTE)

    object AlbumsScreen : Screen(ALBUMS_SCREEN_ROUTE)

    object CurrentSongScreen : Screen(CURRENT_SONG_SCREEN_ROUTE)


    companion object {

        private const val SONGS_SCREEN_ROUTE = "songs"

        private const val PLAYLISTS_SCREEN_ROUTE = "playlists"

        private const val ALBUMS_SCREEN_ROUTE = "albums"

        private const val CURRENT_SONG_SCREEN_ROUTE = "current_song"

    }

}