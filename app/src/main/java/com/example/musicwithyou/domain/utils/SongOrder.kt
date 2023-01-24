package com.example.musicwithyou.domain.utils

sealed class SongOrder(val orderType: OrderType) {

    class Duration(orderType: OrderType) : SongOrder(orderType)

    class Title(orderType: OrderType) : SongOrder(orderType)

    class Album(orderType: OrderType) : SongOrder(orderType)

    class Artist(orderType: OrderType) : SongOrder(orderType)

    fun copy(orderType: OrderType): SongOrder {
        return when (this) {
            is Duration -> Duration(orderType)
            is Title -> Title(orderType)
            is Album -> Album(orderType)
            is Artist -> Artist(orderType)
        }
    }

}