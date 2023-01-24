package com.example.musicwithyou.domain.utils

sealed class OrderType {

    object Ascending: OrderType()

    object Descending: OrderType()

}
