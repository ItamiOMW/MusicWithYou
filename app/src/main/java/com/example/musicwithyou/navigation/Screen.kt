package com.example.musicwithyou.navigation

sealed class Screen(val route: String) {

    object MainTabsScreen : Screen(MAIN_TABS_SCREEN_ROUTE)

    object PlayingNowScreen : Screen(CURRENT_PLAYING_SONG_SCREEN_ROUTE)

    object CurrentQueueScreen : Screen(CURRENT_QUEUE_SCREEN_ROUTE)


    companion object {

        private const val MAIN_TABS_SCREEN_ROUTE = "tab_screen"

        private const val CURRENT_PLAYING_SONG_SCREEN_ROUTE = "playing_now"

        private const val CURRENT_QUEUE_SCREEN_ROUTE = "current_queue"

    }

}