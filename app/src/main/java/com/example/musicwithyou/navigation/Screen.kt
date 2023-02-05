package com.example.musicwithyou.navigation

sealed class Screen(val route: String) {

    object MainTabsScreen : Screen(MAIN_TABS_SCREEN_ROUTE)

    object PlayingNowScreen : Screen(PLAYING_NOW_SCREEN_ROUTE)


    companion object {

        private const val MAIN_TABS_SCREEN_ROUTE = "tab_screen"

        private const val PLAYING_NOW_SCREEN_ROUTE = "playing_now"

    }

}